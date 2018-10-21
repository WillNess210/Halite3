package hlt;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Player{
	public final PlayerId id;
	public final Shipyard shipyard;
	public int halite;
	public Map<EntityId, Ship> ships = new LinkedHashMap<>();
	public Map<EntityId, Dropoff> dropoffs = new LinkedHashMap<>();
	public Map<Integer, Ship> shipyardPlanner = new LinkedHashMap<>();
	private Player(final PlayerId id, final Shipyard shipyard){
		this.id = id;
		this.shipyard = shipyard;
	}
	public void runTurn(Game game){
		// gameMap
		GameMap gameMap = game.gameMap;
		// LOG HOW MANY TURTLES WE HAVE
		Log.logVar("NumTurtles", ships.size() + "");
		// FIND A GOOD MINIMUM HALITE WALL VALUE
		int minHaliteWall = 0;
		do{
			minHaliteWall += 25;
			gameMap.fillTunnelMap(this, minHaliteWall);
		}while(gameMap.numWalls < ships.size() && minHaliteWall < 1000);
		Log.logVar("NumWalls", gameMap.numWalls + "");
		Log.logVarln("MinHaliteWall", minHaliteWall + "");
		// DETERMINE A MOVE FOR EACH SHIP
		for(final Ship ship : ships.values()){
			ship.log();
			int turnsHome = ship.position.distanceTo(this.shipyard.position);
			if(ship.turnsStill > 5){  // CHECK IF STUCK
				CommandQueue.add(ship.getTurnRandomMove(this, gameMap));
			}else if((ship.halite > 800 && !shipyardPlanner.containsKey(turnsHome)) || ship.shouldGoDeposit){ // CHECK IF I SHOULD GO DEPOSIT
				ship.shouldGoDeposit = true;
				shipyardPlanner.put(turnsHome, ship);
				CommandQueue.add(ship.getTurnDeposit(this, gameMap));
			}else if(gameMap.at(ship).halite >= minHaliteWall && !ship.isFull()){ // CHECK IF SHOULD KEEP MINING
				CommandQueue.add(ship.getTurnMine(this, gameMap));
			}else if(ship.isFull()){ // Can't do anything :( TODO implement this as a system to create more dropoffs
				CommandQueue.add(ship.getTurnMine(this, gameMap));
			}else{ // OTHERWISE FIND SOMEWHERE TO MINE
				CommandQueue.add(ship.getTurnFindMine(this, gameMap));
			}
			Log.logln();
		}
		gameMap.logTunnelMap(this);
		this.logShipyardPlanner();
		// DETERMINE IF WE SHOULD SPAWN
		if(game.turnNumber <= (3 * game.getNumTurns()) / 4 && this.halite >= Constants.SHIP_COST
				&& !gameMap.at(shipyard).isOccupied()){
			CommandQueue.add(shipyard.spawn());
			Log.logln("SPAWNING");
		}
	}
	void _update(final int numShips, final int numDropoffs, final int halite){
		this.halite = halite;
		// update, add, remove ships
		ArrayList<EntityId> shipsThatDied = new ArrayList<EntityId>(ships.size());
		shipsThatDied.addAll(ships.keySet());
		for(int i = 0; i < numShips; ++i){
			final Ship generatedShip = Ship._generate(id);
			if(ships.keySet().contains(generatedShip.id)){
				ships.get(generatedShip.id)._update(this, generatedShip.position.x, generatedShip.position.y,
						generatedShip.halite);
			}else{
				ships.put(generatedShip.id, generatedShip);
			}
			shipsThatDied.remove(generatedShip.id);
		}
		for(EntityId shipId : shipsThatDied){
			ships.remove(shipId);
		}
		// It's fine if these clear & re-implement every turn
		dropoffs.clear();
		for(int i = 0; i < numDropoffs; ++i){
			final Dropoff dropoff = Dropoff._generate(id);
			dropoffs.put(dropoff.id, dropoff);
		}
		shipyardPlanner.clear();
	}
	public void logShipyardPlanner(){
		for(int key : shipyardPlanner.keySet()){
			Log.logln(key + ": " + shipyardPlanner.get(key).id.id);
		}
	}
	static Player _generate(){
		final Input input = Input.readInput();
		final PlayerId playerId = new PlayerId(input.getInt());
		final int shipyard_x = input.getInt();
		final int shipyard_y = input.getInt();
		return new Player(playerId, new Shipyard(playerId, new Position(shipyard_x, shipyard_y)));
	}
}
