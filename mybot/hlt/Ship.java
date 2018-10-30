package hlt;
import java.util.ArrayList;
import java.util.Random;

public class Ship extends Entity{
	public int halite, turnsAlive, turnsStill, distanceToDropoff;
	public boolean shouldGoDeposit;
	public Position endGoal;
	public Ship(final PlayerId owner, final EntityId id, final Position position, final int halite){
		super(owner, position, id);
		this.halite = halite;
		this.turnsAlive = 0;
		this.turnsStill = 0;
		this.shouldGoDeposit = false;
		this.distanceToDropoff = 0;
		this.endGoal = null;
	}
	public Position randomEmptyNeighbor(Player me, GameMap gameMap){
		Random rng = new Random();
		ArrayList<Position> ns = this.position.getEmptyNeighbours(me, gameMap);
		if(ns.size() == 0){
			return this.position;
		}
		return ns.get(rng.nextInt(ns.size()));
	}
	public Position getMineSpot(Player me, GameMap gameMap){
		return gameMap.getOptimizedPointToTunnelMapWall(me, this);
	}
	// TODO update to go to closest dropoff/shipyard
	public Position goToShipyard(Player me, GameMap gameMap){
		return me.shipyard.position;
	}
	public Command moveTowards(GameMap gameMap, Position goal, Player me){
		return this.move(AStar.aStar(this, gameMap, me, goal), gameMap, me);
	}
	public Command moveSwap(final Direction direction, GameMap gameMap, Player me){ // only call when both have enough halite to move
		this.turnsStill = 0;
		Position next = this.position.directionalOffset(direction, gameMap);
		this.position.x = next.x;
		this.position.y = next.y;
		return Command.move(this.id, direction);
	}
	public Command move(final Direction direction, GameMap gameMap, Player me){
		if(this.halite >= gameMap.at(this).halite / 10
				&& gameMap.at(this.position.directionalOffset(direction, gameMap)).canMoveOn(me)){
			gameMap.at(this).markSafe();
			Position meNext = this.position.directionalOffset(direction, gameMap);
			this.position.x = meNext.x;
			this.position.y = meNext.y;
			gameMap.at(this).markUnsafe(this);
			this.turnsStill = 0;
			return Command.move(id, direction);
		}else if(me.startSuicide && this.halite >= gameMap.at(this).halite / 10
				&& this.position.directionalOffset(direction, gameMap).samePosition(me.shipyard.position)){
			return Command.move(id, direction);
		}else{
			return this.stayStill();
		}
	}
	public Command stayStill(){
		this.turnsStill++;
		return Command.move(id, Direction.STILL);
	}
	public int getTurnsToAfter(GameMap gameMap, Position b){
		return gameMap.at(b).aDistTraveled;
	}
	public int getTurnsTo(GameMap gameMap, Position b, Player me){
		AStar.aStar(this, gameMap, me, b);
		return this.getTurnsToAfter(gameMap, b);
	}
	public boolean canMove(GameMap gameMap){
		return this.halite > gameMap.at(this).halite / 10 || gameMap.at(this).halite < 10;
	}
	public boolean isFull(){
		return this.halite >= Constants.MAX_HALITE;
	}
	public void _update(Player me, Position position, int halite){
		this.position = position;
		this.halite = halite;
		this.turnsAlive++;
		if(this.position.samePosition(me.shipyard.position)){
			this.shouldGoDeposit = false;
		}
	}
	public void log(){
		Log.logVar("ID", this.id.id + "");
		Log.logVar("Pos", this.toString());
		Log.logVar("Halite", this.halite + "");
		Log.logVar("Turns from Dropoff", this.distanceToDropoff + "");
	}
	static Ship _generate(final PlayerId playerId){
		final Input input = Input.readInput();
		final EntityId shipId = new EntityId(input.getInt());
		final int x = input.getInt();
		final int y = input.getInt();
		final int halite = input.getInt();
		return new Ship(playerId, shipId, new Position(x, y), halite);
	}
	@Override
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		if(!super.equals(o))
			return false;
		Ship ship = (Ship) o;
		return halite == ship.halite;
	}
	@Override
	public int hashCode(){
		int result = super.hashCode();
		result = 31 * result + halite;
		return result;
	}
}
