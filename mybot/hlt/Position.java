package hlt;
import java.util.ArrayList;

public class Position{
	public int x, y;
	public Position(final int x, final int y){
		this.x = x;
		this.y = y;
	}
	Position getHighestHaliteEmptyNeighbour(Player me, GameMap gameMap){
		ArrayList<Position> nbrs = this.getEmptyNeighbours(me, gameMap);
		if(nbrs.size() == 0){
			return new Position(gameMap.width - 1, gameMap.height - 1);
		}
		Position highestHalite = nbrs.get(0);
		for(Position pos : nbrs){
			if(gameMap.at(pos).halite > gameMap.at(highestHalite).halite){
				highestHalite = pos;
			}
		}
		return highestHalite;
	}
	ArrayList<Position> getEmptyNeighbours(Player me, GameMap gameMap){
		ArrayList<Position> toReturn = new ArrayList<Position>();
		Position[] ns = this.getNeighbours(gameMap);
		for(Position n : ns){
			if(gameMap.at(n).canMoveOn(me)){
				toReturn.add(n);
			}
		}
		return toReturn;
	}
	Position[] getNeighbours(GameMap gameMap){
		Position[] ns = new Position[4];
		ns[0] = this.directionalOffset(Direction.NORTH, gameMap);
		ns[1] = this.directionalOffset(Direction.EAST, gameMap);
		ns[2] = this.directionalOffset(Direction.SOUTH, gameMap);
		ns[3] = this.directionalOffset(Direction.WEST, gameMap);
		return ns;
	}
	Position directionalOffset(final Direction d, GameMap gameMap){
		final int dx;
		final int dy;
		switch(d){
			case NORTH:
				dx = 0;
				dy = -1;
				break;
			case SOUTH:
				dx = 0;
				dy = 1;
				break;
			case EAST:
				dx = 1;
				dy = 0;
				break;
			case WEST:
				dx = -1;
				dy = 0;
				break;
			case STILL:
				dx = 0;
				dy = 0;
				break;
			default:
				throw new IllegalStateException("Unknown direction " + d);
		}
		Position next = new Position(x + dx, y + dy);
		if(next.x < 0){
			next.x = gameMap.width - 1;
		}else if(next.x >= gameMap.width){
			next.x = 0;
		}
		if(next.y < 0){
			next.y = gameMap.height - 1;
		}else if(next.y >= gameMap.height){
			next.y = 0;
		}
		return next;
	}
	public void logln(){
		Log.logln(this.toString());
	}
	public void log(){
		Log.log(this.toString());
	}
	public String toString(){
		return "(" + this.x + ", " + this.y + ")";
	}
	public int distanceTo(Position b, GameMap gameMap){
		int dx1 = b.x - this.x < 0 ? b.x - this.x + gameMap.width : b.x - this.x;
		int dx2 = this.x - b.x < 0 ? this.x - b.x + gameMap.width : this.x - b.x;
		int dy1 = b.y - this.y < 0 ? b.y - this.y + gameMap.height : b.y - this.y;
		int dy2 = this.y - b.y < 0 ? this.y - b.y + gameMap.height : this.y - b.y;
		return Math.min(dx1, dx2) + Math.min(dy1, dy2);
	}
	public boolean samePosition(Position b){
		return this.x == b.x && this.y == b.y;
	}
	@Override
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		Position position = (Position) o;
		if(x != position.x)
			return false;
		return y == position.y;
	}
	@Override
	public int hashCode(){
		int result = x;
		result = 31 * result + y;
		return result;
	}
}
