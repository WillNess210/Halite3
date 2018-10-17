package hlt;
import java.util.ArrayList;
import wln.CollisionAvoidance;

public class GameMap{
	public final int width;
	public final int height;
	public final MapCell[][] cells;
	public final int minHaliteWall = 200;
	public GameMap(final int width, final int height){
		this.width = width;
		this.height = height;
		cells = new MapCell[height][];
		for(int y = 0; y < height; ++y){
			cells[y] = new MapCell[width];
		}
	}
	public MapCell at(final Position position){
		final Position normalized = normalize(position);
		return cells[normalized.y][normalized.x];
	}
	public MapCell at(final Entity entity){
		return at(entity.position);
	}
	public MapCell at(final int x, final int y){
		return cells[y][x];
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
	public int normalizeX(final int x){
		return ((x % this.width) + this.width) % this.width;
	}
	public int normalizeY(final int y){
		return ((y % this.height) + this.height) % this.height;
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
	public Direction naiveNavigate(final Ship ship, final Position destination){
		// getUnsafeMoves normalizes for us
		for(final Direction direction : getUnsafeMoves(ship.position, destination)){
			final Position targetPos = ship.position.directionalOffset(direction);
			if(!at(targetPos).isOccupied() && CollisionAvoidance.isSafe(targetPos)){
				at(targetPos).markUnsafe(ship);
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
	public int[][] getTunnelView(Player me){
		int[][] map = new int[this.width][this.height];
		for(int i = 0; i < this.width; i++){
			for(int j = 0; j < this.height; j++){
				map[i][j] = -1; // UNEXPLORED
			}
		}
		this.gradePosition(map, me.shipyard.getX(), me.shipyard.getY());
		return map;
	}
	public void logTunnelView(Player me, int[][] map){
		for(int j = 0; j < this.height; j++){
			String rowString = "";
			for(int i = 0; i < this.width; i++){
				String val = "";
				if(i == me.shipyard.getX() && j == me.shipyard.getY()){
					val = "*";
				}else if(map[i][j] == -1){
					val = " ";
				}else{
					val = map[i][j] + "";
				}
				val += " ";
				rowString += val;
			}
			Log.log(rowString);
		}
	}
	public void logTunnelView(Player me) {
		int[][] map = this.getTunnelView(me);
		this.logTunnelView(me, map);
	}
	public int[][] gradePosition(int[][] map, int x, int y){
		if(map[x][y] != -1){
			return map;
		}
		if(this.at(x, y).halite >= this.minHaliteWall){
			map[x][y] = 1; // THIS IS TO BE MINED
			return map;
		}else{
			map[x][y] = 0; // EMPTY, NO HALITE TO MINE
		}
		map = this.gradePosition(map, normalizeX(x + 1), y);
		map = this.gradePosition(map, normalizeX(x - 1), y);
		map = this.gradePosition(map, x, normalizeY(y + 1));
		map = this.gradePosition(map, x, normalizeY(y - 1));
		return map;
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
	public void log(){
		Log.log(this.at(0, 7).halite + "");
		for(int j = 0; j < this.height; j++){
			String rowString = "";
			for(int i = 0; i < this.width; i++){
				rowString += this.at(i, j).getFactorOfHundred();
				rowString += " ";
			}
			Log.log(rowString);
		}
	}
}
