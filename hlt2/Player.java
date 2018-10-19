package hlt2;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Player{
	public final PlayerId id;
	public final Shipyard shipyard;
	public int halite;
	public Map<EntityId, Ship> ships = new LinkedHashMap<>();
	public Map<EntityId, Dropoff> dropoffs = new LinkedHashMap<>();
	private Player(final PlayerId id, final Shipyard shipyard){
		this.id = id;
		this.shipyard = shipyard;
	}
	public void runTurn(Game game, GameMap gameMap) {
		Random rng = new Random();
		for(final Ship ship : ships.values()){
			if(gameMap.at(ship).halite < Constants.MAX_HALITE / 10 || ship.isFull()){
				final Direction randomDirection = Direction.ALL_CARDINALS.get(rng.nextInt(4));
				CommandQueue.add(ship.move(randomDirection, gameMap));
			}else{
				CommandQueue.add(ship.stayStill());
			}
			ship.log();
		}
		if(game.turnNumber <= 200 && this.halite >= Constants.SHIP_COST && !gameMap.at(shipyard).isOccupied()){
			CommandQueue.add(shipyard.spawn());
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
				ships.get(generatedShip.id)._update(generatedShip.position.x, generatedShip.position.y,
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
	}
	static Player _generate(){
		final Input input = Input.readInput();
		final PlayerId playerId = new PlayerId(input.getInt());
		final int shipyard_x = input.getInt();
		final int shipyard_y = input.getInt();
		return new Player(playerId, new Shipyard(playerId, new Position(shipyard_x, shipyard_y)));
	}
}
