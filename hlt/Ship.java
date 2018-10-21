package hlt;
import java.util.ArrayList;
import java.util.Random;

public class Ship extends Entity{
	public int halite, turnsAlive, turnsStill;
	public boolean shouldGoDeposit;
	public Ship(final PlayerId owner, final EntityId id, final Position position, final int halite){
		super(owner, id, position);
		this.halite = halite;
		this.turnsAlive = 0;
		this.turnsStill = 0;
		this.shouldGoDeposit = false;
	}
	public Command getTurnSuicide(Player me, GameMap gameMap){
		Log.logVar("INTENT", "MOVE TO SUICIDE");
		if(this.position.distanceTo(me.shipyard.position) == 1){
			Direction d = gameMap.naiveNavigateSuicide(this, me.shipyard.position);
			return this.moveSuicide(d, gameMap);
		}else{
			Direction d = AStar.aStar(this, gameMap, me.shipyard.position);
			return this.move(d, gameMap);
		}
	}
	public Command getTurnDeposit(Player me, GameMap gameMap){
		Log.logVar("INTENT", "MOVE TO DEPOSIT");
		Log.logVar("GOAL", me.shipyard.position.toString());
		this.shouldGoDeposit = true;
		Direction d = AStar.aStar(this, gameMap, me.shipyard.position);
		return this.move(d, gameMap);
	}
	public Command getTurnFindMine(Player me, GameMap gameMap){
		Log.logVar("INTENT", "FIND TO MINE");
		Position closestWall = gameMap.getOptimizedPointToTunnelMapWall(me, this);
		Log.logVar("GOAL", closestWall.toString());
		return this.move(AStar.aStar(this, gameMap, closestWall), gameMap);
		//return this.move(gameMap.naiveNavigate(this, closestWall), gameMap);
	}
	public Command getTurnMine(Player me, GameMap gameMap){
		Log.logVar("INTENT", "KEEP MINING");
		this.turnsStill = -1;
		gameMap.wallMap[this.position.x][this.position.y] = 100 + this.id.id;
		return this.stayStill();
	}
	public Command getTurnStill(Player me, GameMap gameMap){
		Log.logVar("INTENT", "IDLE");
		this.turnsStill = -1;
		return this.stayStill();
	}
	public Command getTurnRandomMove(Player me, GameMap gameMap){
		Log.log("STUCK");
		Random rng = new Random();
		ArrayList<Position> ns = this.position.getEmptyNeighbours(gameMap);
		if(ns.size() == 0){
			return this.stayStill();
		}
		return this.move(gameMap.naiveNavigate(this, ns.get(rng.nextInt(ns.size()))), gameMap);
	}
	public boolean isFull(){
		return halite >= Constants.MAX_HALITE;
	}
	public Command makeDropoff(){
		return Command.transformShipIntoDropoffSite(id);
	}
	public Command moveTowards(GameMap gameMap, Position goal){
		return this.move(AStar.aStar(this, gameMap, goal), gameMap);
	}
	public Command moveSuicide(final Direction direction, GameMap gameMap){
		if(this.halite >= gameMap.at(this).halite / 10){
			gameMap.at(this).markSafe();
			this.position = this.position.directionalOffset(direction);
			gameMap.at(this).markUnsafe(this);
			this.turnsStill = 0;
			return Command.move(id, direction);
		}else{
			return this.stayStill();
		}
	}
	public Command move(final Direction direction, GameMap gameMap){
		if(this.halite >= gameMap.at(this).halite / 10
				&& gameMap.at(this.position.directionalOffset(direction)).canMoveOn()){
			gameMap.at(this).markSafe();
			this.position = this.position.directionalOffset(direction);
			gameMap.at(this).markUnsafe(this);
			this.turnsStill = 0;
			return Command.move(id, direction);
		}else{
			return this.stayStill();
		}
	}
	public Command stayStill(){
		this.turnsStill++;
		return Command.move(id, Direction.STILL);
	}
	public void _update(Player me, int x, int y, int halite){
		this.position.x = x;
		this.position.y = y;
		this.halite = halite;
		this.turnsAlive++;
		if(this.position.samePosition(me.shipyard.position)){
			this.shouldGoDeposit = false;
		}
	}
	public void log(){
		Log.logVar("ID", this.id.id + "");
		Log.logVar("Pos", this.position.toString());
		Log.logVar("Halite", this.halite + "");
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
