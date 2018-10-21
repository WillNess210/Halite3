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
		// SORT SHIPS BASED ON DISTANCE TO SHIPYARD
		ArrayList<Ship> shipsSorted = new ArrayList<Ship>();
		EntityId firstShipID = null;
		for(Ship ship : ships.values()){
			int turnsHome = ship.getTurnsTo(gameMap, this.shipyard.position, this);
			if(turnsHome == 0 && ship.position.samePosition(this.shipyard.position)){
				ship.distanceToShipyard = -1;
			}else{
				ship.distanceToShipyard = turnsHome;
			}
			shipsSorted.add(ship);
			if(firstShipID == null){
				firstShipID = ship.id;
			}
		}
		shipsSorted.sort((o1, o2) -> o1.distanceToShipyard - o2.distanceToShipyard);
		// DETERMINE A MOVE FOR EACH SHIP
		Log.logln();
		for(final Ship ship : shipsSorted){
			ship.log();
			if(ship.turnsStill > 5){ // CHECK IF STUCK
				CommandQueue.add(ship.getTurnRandomMove(this, gameMap));
			}else if(ship.distanceToShipyard + 10 > game.getNumTurnsLeft()){ // GO AND SUICIDE
				if(ship.id.id == firstShipID.id){ // first ship needs to get out of the way
					CommandQueue
							.add(ship.moveTowards(gameMap, new Position(gameMap.width - 1, gameMap.height - 1), this));
				}else{
					CommandQueue.add(ship.getTurnSuicide(this, gameMap));
				}
			}else if(((ship.halite > 800 || (ship.halite > 400 && ships.size() < 10))
					&& !shipyardPlanner.containsKey(ship.distanceToShipyard))
					&& gameMap.at(ship).halite >= minHaliteWall || ship.shouldGoDeposit){ // CHECK IF I SHOULD GO DEPOSIT
				ship.shouldGoDeposit = true;
				shipyardPlanner.put(ship.distanceToShipyard, ship);
				CommandQueue.add(ship.getTurnDeposit(this, gameMap));
			}else if(gameMap.at(ship).halite >= minHaliteWall && !ship.isFull()){ // CHECK IF SHOULD KEEP MINING
				CommandQueue.add(ship.getTurnMine(this, gameMap));
			}else if(ship.isFull()){ // Can't do anything :( TODO implement this as a system to create more dropoffs
				CommandQueue.add(ship.getTurnMine(this, gameMap));
			}else{ // OTHERWISE FIND SOMEWHERE TO MINE
				if(ship.mineSpot == null || !gameMap.at(ship.mineSpot).canMoveOn(this)){
					CommandQueue.add(ship.getTurnFindMine(this, gameMap));
				}else{
					CommandQueue.add(ship.moveTowardsMineSpot(gameMap, this));
				}
			}
			Log.logln();
		}
		gameMap.logTunnelMap(this);
		this.logShipyardPlanner();
		// DETERMINE IF WE SHOULD SPAWN
		if(game.turnNumber <= (3 * game.getNumTurns()) / 5 && this.halite >= Constants.SHIP_COST
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
