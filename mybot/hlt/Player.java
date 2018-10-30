package hlt;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Player{
	public final PlayerId id;
	public final Shipyard shipyard;
	public int halite;
	public boolean startSuicide;
	public Map<EntityId, Ship> ships = new LinkedHashMap<>();
	public Map<EntityId, Dropoff> dropoffs = new LinkedHashMap<>();
	public Map<Integer, Ship> shipyardPlanner = new LinkedHashMap<>();
	private Player(final PlayerId id, final Shipyard shipyard){
		this.id = id;
		this.shipyard = shipyard;
		this.startSuicide = false;
	}
	public void runTurn(Game game, double haliteRatio){
		// gameMap
		GameMap gameMap = game.gameMap;
		// LOG HOW MANY TURTLES WE HAVE
		Log.logVar("NumTurtles", ships.size() + "");
		int totalHaliteAvailable = gameMap.getTotalHalite();
		Log.logVar("# Halite", totalHaliteAvailable + "");
		// FIND A GOOD MINIMUM HALITE WALL VALUE
		int minHaliteWall = -5;
		do{
			minHaliteWall = Math.min(1000, minHaliteWall + 35);
			gameMap.fillTunnelMap(this, minHaliteWall);
		}while(gameMap.numWalls < ships.size() && minHaliteWall < 1000);
		Log.logVar("NumWalls", gameMap.numWalls + "");
		Log.logVarln("MinHaliteWall", minHaliteWall + "");
		// CHECK FOR CREATING DROPOFFS
		if(game.turnNumber > 100 && game.turnNumber % 50 == 0){
		}
		// DETERMINE A MOVE FOR EACH SHIP
		Log.logln();
		for(final Ship ship : ships.values()){
			ship.distanceToDropoff = ship.getTurnsTo(gameMap, this.shipyard.position, this);
			if(ship.turnsStill >= 5){ // STUCK, CHOOSE RANDOM SAFE NEIGHBOR
				ship.endGoal = ship.randomEmptyNeighbor(this, gameMap);
				continue;
			}else if(this.startSuicide || ship.distanceToDropoff + 10 > game.getNumTurnsLeft()){ // IF SUICIDE
				this.startSuicide = true;
				ship.endGoal = ship.goToShipyard(this, gameMap);
				continue;
			}else if(ship.isFull() == false && ship.shouldGoDeposit == false
					&& gameMap.at(ship).halite >= minHaliteWall){
				ship.endGoal = ship.position;
				continue;
			}else if(ship.halite > 800 || (ship.halite > 400 && ships.size() < 10) || ship.shouldGoDeposit){ // IF SHOULD DEPOSIT
				// CHECK IF DEPOSIT AVAILABLE
				if(!shipyardPlanner.containsKey(ship.distanceToDropoff)
						&& (gameMap.at(ship).halite < minHaliteWall || ship.isFull() || ship.shouldGoDeposit)){
					ship.shouldGoDeposit = true;
					ship.endGoal = ship.goToShipyard(this, gameMap);
					shipyardPlanner.put(ship.distanceToDropoff, ship);
					continue;
				}else if(ship.isFull()){
					// CAN'T DO ANYTHING, COLLISION
					ship.endGoal = this.shipyard.position;
					continue;
				}
			}
			// FIND SOMEWHERE TO MINE
			ship.endGoal = ship.getMineSpot(this, gameMap);
		}
		for(final Ship ship : ships.values()){
			CommandQueue.add(ship.moveTowards(gameMap, ship.endGoal, this));
		}
		gameMap.logTunnelMap(this);
		//this.logShipyardPlanner();
		// DETERMINE IF WE SHOULD SPAWN
		if(totalHaliteAvailable >= (haliteRatio * game.ogTotalHalite) && this.halite >= Constants.SHIP_COST
				&& !gameMap.at(shipyard).isOccupied()){
			CommandQueue.add(shipyard.spawn());
			Log.logln("SPAWNING");
		}
		Log.logVarln("SHIPYARD PLANNER SIZE", shipyardPlanner.size() + "");
	}
	void _update(final int numShips, final int numDropoffs, final int halite){
		this.halite = halite;
		// update, add, remove ships
		ArrayList<EntityId> shipsThatDied = new ArrayList<EntityId>(ships.size());
		shipsThatDied.addAll(ships.keySet());
		for(int i = 0; i < numShips; ++i){
			final Ship generatedShip = Ship._generate(id);
			if(ships.keySet().contains(generatedShip.id)){
				ships.get(generatedShip.id)._update(this,
						new Position(generatedShip.position.x, generatedShip.position.y), generatedShip.halite);
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
	public ArrayList<Position> getDropoffPoints(){
		ArrayList<Position> dropoffPoints = new ArrayList<Position>();
		dropoffPoints.add(this.shipyard.position);
		for(Dropoff dropoff : this.dropoffs.values()){
			dropoffPoints.add(dropoff.position);
		}
		return dropoffPoints;
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
