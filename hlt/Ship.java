package hlt;
import java.util.ArrayList;
import java.util.Random;
import wln.*;

public class Ship extends Entity{
	public int halite, turnsSinceDeposit, turnsStill;
	public boolean shouldDeposit;
	public final int minHalite = 900;
	public Ship(final PlayerId owner, final EntityId id, final Position position, final int halite){
		super(owner, id, position);
		this.halite = halite;
		this.turnsSinceDeposit = 0;
		this.shouldDeposit = false;
		this.turnsStill = 0;
	}
	public boolean isFull(){
		return halite >= Constants.MAX_HALITE;
	}
	public void updateStats(Player me){
		// UPDATING DEPOSIT NUMBERS
		this.turnsSinceDeposit++;
		// CHECKING IF WE DEPOSITED THIS TURN
		ArrayList<Position> depositPoints = new ArrayList<Position>();
		depositPoints.add(me.shipyard.position);
		for(Dropoff dropoff : me.dropoffs.values()){
			depositPoints.add(dropoff.position);
		}
		for(Position pos : depositPoints){
			if(pos.equals(this.position)){
				this.turnsSinceDeposit = 0;
				this.shouldDeposit = false;
			}
		}
	}
	public Command getCommand(Player me, GameMap gameMap, Random rng){
		// CHECK IF STUCK
		if(this.turnsStill > 5 && (this.halite > 900 || gameMap.at(this).halite < Constants.MAX_HALITE/10)) { // I'M STUCK
			ArrayList<Position> ns = this.position.getNeighbors(gameMap);
			for(Position p : ns) {
				if(gameMap.at(p).ship == null && CollisionAvoidance.isSafe(p)) {
					return this.move(gameMap.naiveNavigate(this, p));
				}
			}
		}
		// If there is significant Halite underneath me, I should mine
		if(gameMap.at(this).halite > Constants.MAX_HALITE / 10 && !this.isFull()){
			me.tunnelMap[this.getX()][this.getY()] = 2;
			return this.stayStill();
		}
		// I should find out if I should deposit
		if(this.halite > this.minHalite){
			this.shouldDeposit = true;
		}
		// If I'm on my way to deposit, keep going
		if(this.shouldDeposit){
			return this.move(gameMap.naiveNavigate(this, me.shipyard.position));
		}
		// Otherwise, I should find something to mine
		int[][] tunnelMap = me.tunnelMap;
		Position goal = new Position(rng.nextInt(gameMap.width), rng.nextInt(gameMap.height));
		double closest = 1000;
		for(int i = 0; i < gameMap.width; i++){
			for(int j = 0; j < gameMap.height; j++){
				if(tunnelMap[i][j] == 1 && this.position.distanceTo(new Position(i, j)) < closest){
					closest = this.position.distanceTo(new Position(i, j));
					goal = new Position(i, j);
				}
			}
		}
		me.tunnelMap[goal.x][goal.y] = 2;
		return this.move(gameMap.naiveNavigate(this, goal));
	}
	public Command makeDropoff(){
		return Command.transformShipIntoDropoffSite(id);
	}
	public Command move(final Direction direction){
		CollisionAvoidance.add(this.position.directionalOffset(direction));
		return Command.move(id, direction);
	}
	public Command stayStill(){
		return Command.move(id, Direction.STILL);
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
	@Override
	public String toString(){
		return this.id.id + " (" + this.position.toString() + ") : " + this.halite;
	}
	public void log(){
		Log.log(this.toString());
	}
}
