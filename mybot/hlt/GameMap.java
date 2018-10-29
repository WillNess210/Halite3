package hlt;
import java.util.ArrayList;

public class GameMap{
	public final int width;
	public final int height;
	public int[][] wallMap;
	public int numWalls;
	public MapCell[][] cells;
	public GameMap(final int width, final int height){
		this.width = width;
		this.height = height;
		cells = new MapCell[height][];
		for(int y = 0; y < height; ++y){
			cells[y] = new MapCell[width];
		}
		wallMap = new int[width][height];
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				wallMap[i][j] = -1;
			}
		}
		numWalls = 0;
	}
	public MapCell at(final Position position){
		final Position normalized = normalize(position);
		return cells[normalized.y][normalized.x];
	}
	public MapCell at(final Entity entity){
		return at(entity);
	}
	public MapCell at(final int x, final int y){
		return cells[y][x];
	}
	public int getTotalHalite(){
		int toReturn = 0;
		for(int j = 0; j < this.height; j++){
			for(int i = 0; i < this.width; i++){
				toReturn += this.at(i, j).halite;
			}
		}
		return toReturn;
	}
	public void fillTunnelMap(Player me, int minHaliteWall){
		// reset tunnel map
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				wallMap[i][j] = -1;
			}
		}
		numWalls = 0;
		// call recursive functions
		this.tunnelRecursiveHelper(me.shipyard, minHaliteWall);
	}
	public void tunnelRecursiveHelper(Position root, int minHaliteWall){
		if(this.at(root).halite < minHaliteWall){
			wallMap[root.x][root.y] = 0;
			for(Position nbr : root.getNeighbours(this)){
				if(wallMap[nbr.x][nbr.y] == -1){
					this.tunnelRecursiveHelper(nbr, minHaliteWall);
				}
			}
		}else{
			wallMap[root.x][root.y] = 1;
			this.numWalls++;
		}
	}
	public void logTunnelMap(Player me){
		int minX = -1, maxX = -1, minY = -1, maxY = -1;
		// determining min & max x
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				if(wallMap[i][j] >= 0){
					maxX = i;
					if(minX == -1){
						minX = i;
					}
					continue;
				}
			}
		}
		for(int j = 0; j < height; j++){
			for(int i = 0; i < width; i++){
				if(wallMap[i][j] >= 0){
					maxY = j;
					if(minY == -1){
						minY = j;
					}
					continue;
				}
			}
		}
		Log.logln();
		Log.logln("-- TUNNEL MAP --");
		for(int j = minY; j <= maxY; j++){
			for(int i = minX; i <= maxX; i++){
				if(me.shipyard.x == i && me.shipyard.y == j){
					Log.log("* ");
				}else if(wallMap[i][j] == -1){
					Log.log("  ");
				}else if(wallMap[i][j] == 0){
					Log.log(". ");
				}else if(wallMap[i][j] == 1){
					Log.log("[]");
				}else if(wallMap[i][j] - 100 >= 10){
					Log.log((wallMap[i][j] - 100) + "");
				}else{
					Log.log((wallMap[i][j] - 100) + " ");
				}
			}
			Log.logln("");
		}
		Log.logln("----------------");
	}
	public Position getOptimizedPointToTunnelMapWall(Player me, Ship s){
		Position closest = new Position(1000, 1000);
		double bestScore = 999999;
		for(int i = 0; i < this.width; i++){
			for(int j = 0; j < this.height; j++){
				if(wallMap[i][j] == 1 && this.at(i, j).canMoveOn(me)){
					Position test = new Position(i, j);
					double score = s.distanceTo(test, this) + 3 * me.shipyard.distanceTo(test, this)
							- 1.5 * this.at(test).factorOfHundred();
					if(score < bestScore){
						closest = new Position(i, j);
						bestScore = score;
					}
				}
			}
		}
		if(closest.x == 1000){
			return null;
		}else{
			wallMap[closest.x][closest.y] = 100 + s.id.id;
			return closest;
		}
	}
	public Position getClosestToTunnelMapWall(Ship s){
		Position closest = new Position(1000, 1000);
		for(int i = 0; i < this.width; i++){
			for(int j = 0; j < this.height; j++){
				if(wallMap[i][j] == 1 && s.distanceTo(new Position(i, j), this) < s.distanceTo(closest, this)){
					closest = new Position(i, j);
				}
			}
		}
		if(closest.x == 1000){
			return null;
		}else{
			wallMap[closest.x][closest.y] = 100 + s.id.id;
			return closest;
		}
	}
	public int calculateDistance(final Position source, final Position target){
		final Position normalizedSource = normalize(source);
		final Position normalizedTarget = normalize(target);
		final int dx = Math.abs(normalizedSource.x - normalizedTarget.x);
		final int dy = Math.abs(normalizedSource.y - normalizedTarget.y);
		final int toroidal_dx = Math.min(dx, width - dx);
		final int toroidal_dy = Math.min(dy, height - dy);
		return toroidal_dx + toroidal_dy;
	}
	public Position normalize(final Position position){
		final int x = ((position.x % width) + width) % width;
		final int y = ((position.y % height) + height) % height;
		return new Position(x, y);
	}
	public ArrayList<Direction> getUnsafeMoves(final Position source, final Position destination){
		final ArrayList<Direction> possibleMoves = new ArrayList<>();
		final Position normalizedSource = normalize(source);
		final Position normalizedDestination = normalize(destination);
		final int dx = Math.abs(normalizedSource.x - normalizedDestination.x);
		final int dy = Math.abs(normalizedSource.y - normalizedDestination.y);
		final int wrapped_dx = width - dx;
		final int wrapped_dy = height - dy;
		if(normalizedSource.x < normalizedDestination.x){
			possibleMoves.add(dx > wrapped_dx ? Direction.WEST : Direction.EAST);
		}else if(normalizedSource.x > normalizedDestination.x){
			possibleMoves.add(dx < wrapped_dx ? Direction.WEST : Direction.EAST);
		}
		if(normalizedSource.y < normalizedDestination.y){
			possibleMoves.add(dy > wrapped_dy ? Direction.NORTH : Direction.SOUTH);
		}else if(normalizedSource.y > normalizedDestination.y){
			possibleMoves.add(dy < wrapped_dy ? Direction.NORTH : Direction.SOUTH);
		}
		return possibleMoves;
	}
	public Direction naiveNavigateSuicide(final Ship ship, final Position destination){
		// getUnsafeMoves normalizes for us
		for(final Direction direction : getUnsafeMoves(ship, destination)){
			return direction;
		}
		return Direction.STILL;
	}
	public Direction naiveNavigate(final Ship ship, final Position destination, Player me){
		// getUnsafeMoves normalizes for us
		for(final Direction direction : getUnsafeMoves(ship, destination)){
			final Position targetPos = ship.directionalOffset(direction, this);
			if(this.at(targetPos).canMoveOn(me)){
				return direction;
			}
		}
		return Direction.STILL;
	}
	void _update(){
		for(int y = 0; y < height; ++y){
			for(int x = 0; x < width; ++x){
				cells[y][x].ship = null;
			}
		}
		final int updateCount = Input.readInput().getInt();
		for(int i = 0; i < updateCount; ++i){
			final Input input = Input.readInput();
			final int x = input.getInt();
			final int y = input.getInt();
			cells[y][x].halite = input.getInt();
		}
	}
	static GameMap _generate(){
		final Input mapInput = Input.readInput();
		final int width = mapInput.getInt();
		final int height = mapInput.getInt();
		final GameMap map = new GameMap(width, height);
		for(int y = 0; y < height; ++y){
			final Input rowInput = Input.readInput();
			for(int x = 0; x < width; ++x){
				final int halite = rowInput.getInt();
				map.cells[y][x] = new MapCell(new Position(x, y), halite);
			}
		}
		return map;
	}
}
