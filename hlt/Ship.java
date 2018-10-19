package hlt;
import java.util.ArrayList;
import java.util.Random;
import wln.*;

public class Ship extends Entity{
	public int halite, turnsSinceDeposit, turnsStill;
	public boolean shouldDeposit;
	public final int minHalite = 900;
	public String logString;
	public Position mineGoal;
	public Ship(final PlayerId owner, final EntityId id, final Position position, final int halite){
		super(owner, id, position);
		this.halite = halite;
		this.turnsSinceDeposit = 0;
		this.shouldDeposit = false;
		this.turnsStill = 0;
		logString = "";
		mineGoal = null;
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
	public Command getCommand(Player me, GameMap gameMap, Game game, Random rng){
		// CHECK IF STUCK
		if(this.turnsStill > 5 && (this.halite > 900 || gameMap.at(this).halite < Constants.MAX_HALITE/10)) { // I'M STUCK
			this.logString += "STUCK. ";
			// RANDOMIZE ORDER OF NS
			ArrayList<Position> nsNonRandom = this.position.getNeighbors(gameMap);
			ArrayList<Position> ns = new ArrayList<Position>(40);
			while(nsNonRandom.size() > 0) {
				Position rand = nsNonRandom.get(rng.nextInt(nsNonRandom.size()));
				nsNonRandom.remove(rand);
				ns.add(rand);
			}
			for(Position p : ns) {
				if(gameMap.at(p).canMoveOn()) {
					return this.move(this.aStar(gameMap, p), gameMap);
				}
			}
		}
		// If I should be heading back to suicide, let's do so
		if(game.getTurnsLeft() < 100 && this.position.distanceTo(me.shipyard.position) + 8 >= game.getTurnsLeft()) {
			gameMap.at(me.shipyard.position).ship = null; // clear to allow for collisions
			return this.move(this.aStar(gameMap, me.shipyard.position), gameMap);
		}
		// If there is significant Halite underneath me, I should mine
		if((gameMap.at(this).halite >= Constants.MAX_HALITE / 10 || gameMap.at(this).halite >= gameMap.minHaliteWall) && !this.isFull()){
			this.logString += "KEEP MINING. ";
			me.tunnelMap[this.getX()][this.getY()] = 2;
			this.mineGoal = null; // I reached the goal so reset
			return this.stayStill();
		}
		// I should find out if I should deposit
		if(this.halite > this.minHalite){
			this.logString += "HEAD BACK TO DEPOSIT. ";
			this.shouldDeposit = true;
		}
		// If I'm on my way to deposit, keep going
		if(this.shouldDeposit){
			//return this.move(gameMap.naiveNavigate(this, me.shipyard.position));
			this.logString += "HEADING BACK TO DEPOSIT. ";
			Direction toMove = this.aStar(gameMap, me.shipyard.position);
			return this.move(toMove, gameMap);
		}
		// Otherwise, I should find something to mine
		if(this.mineGoal == null) {
			this.logString += "FIND TO MINE. ";
			int[][] tunnelMap = me.tunnelMap;
			this.mineGoal = new Position(rng.nextInt(gameMap.width), rng.nextInt(gameMap.height));
			double closest = 1000;
			for(int i = 0; i < gameMap.width; i++){
				for(int j = 0; j < gameMap.height; j++){
					if(tunnelMap[i][j] == 1 && this.position.distanceTo(new Position(i, j)) < closest && gameMap.at(i, j).canMoveOn()){
						closest = this.position.distanceTo(new Position(i, j));
						this.mineGoal = new Position(i, j);
					}
				}
			}
		}
		this.logString += " GOAL MINE IS AT " + this.mineGoal.toString() + ". ";
		me.tunnelMap[this.mineGoal.x][this.mineGoal.y] = 2;
		return this.move(this.aStar(gameMap, this.mineGoal), gameMap);
		
	}
	public Direction aStar(GameMap gameMap, Position goal) {
		AStarNode[][] map = new AStarNode[gameMap.width][gameMap.height];
		// initializing array
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				map[i][j] = new AStarNode(i, j);
				// TODO ADD IN WRAPPING LOGIC
				map[i][j].distFrom = (Math.abs(goal.x - i) + Math.abs(goal.y - j)) * 2 + gameMap.at(goal).getFactorOfHundred();
			}
		}
		ArrayList<AStarNode> open = new ArrayList<AStarNode>();
		ArrayList<AStarNode> closed = new ArrayList<AStarNode>();
		map[this.getX()][this.getY()].distTraveled = 0;
		open.add(map[this.getX()][this.getY()]);
		AStarNode target = null;
		while(open.size() > 0) {
			AStarNode lowest = open.get(0);
			for(AStarNode test : open) {
				if(test.score() < lowest.score()) {
					lowest = test;
				}
			}
			open.remove(lowest);
			closed.add(lowest);
			if(lowest.x == goal.x && lowest.y == goal.y) {
				target = lowest;
				break;
			}
			ArrayList<AStarNode> ns = lowest.neighbors(map);
			for(AStarNode nbr : ns){
				if(closed.contains(nbr) || (gameMap.at(nbr.x, nbr.y).isOccupied() && this.position.distanceTo(nbr) < 2)){
					continue;
				}
				int possibleDistanceScore = lowest.distTraveled + 1;
				if(!open.contains(nbr)){
					open.add(nbr);
				}else if(possibleDistanceScore >= nbr.distTraveled) {
					continue;
				}
				nbr.parent = lowest;
				nbr.distTraveled = possibleDistanceScore;
				
			}
		}
		if(target == null || target.parent == null) {
			Log.log("A* pathfinding couldn't find anything");
			return Direction.STILL;
		}else {
			while(target.parent.parent != null) {
				target = target.parent;
			}
			// target is now our next position
			return this.position.getDirectionTo(target);
		}
		
	}
	public Command makeDropoff(){
		return Command.transformShipIntoDropoffSite(id);
	}
	public Command move(final Direction direction, GameMap gameMap){
		if(this.halite >= gameMap.at(this).halite/10) { // This is what it costs
			gameMap.at(this.getX(), this.getY()).ship = null;
			Position next = this.position.directionalOffset(direction);
			gameMap.at(next.x, next.y).ship = this;
			this.halite -= gameMap.at(this).halite/10;
			return Command.move(id, direction);
		}else {
			this.logString += "Not enough Halite to complete requested move. ";
			return Command.move(id, Direction.STILL);
		}
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
	public void logLogString() {
		Log.log(this.id.id + " " + this.position.toString() + ": " + this.logString);
		this.logString = "";
	}
}
