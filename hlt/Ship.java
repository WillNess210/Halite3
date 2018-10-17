package hlt;
import java.util.ArrayList;
import java.util.Random;

public class Ship extends Entity{
	public int halite;
	public int turnsSinceDeposit;
	public boolean shouldDeposit;
	public final int minHalite = 900;
	public Ship(final PlayerId owner, final EntityId id, final Position position, final int halite){
		super(owner, id, position);
		this.halite = halite;
		turnsSinceDeposit = 0;
		this.shouldDeposit = false;
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
	public Command getCommand(Player me, GameMap gameMap, Random rng) {
		if(gameMap.at(this).halite < Constants.MAX_HALITE / 10 || this.isFull()){
			Position rP = new Position(rng.nextInt(gameMap.width), rng.nextInt(gameMap.height));
			return this.move(gameMap.naiveNavigate(this, rP));
		}else if(this.turnsSinceDeposit > 20) {
			Position home = me.shipyard.position;
			return this.move(gameMap.naiveNavigate(this, home));
		}else{
			return this.stayStill();
		}
	}
	public Command makeDropoff(){
		return Command.transformShipIntoDropoffSite(id);
	}
	public Command move(final Direction direction){
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
