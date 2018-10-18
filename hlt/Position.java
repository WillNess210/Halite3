package hlt;

import java.util.ArrayList;

public class Position{
	public int x;
	public int y;
	public Position(final int x, final int y){
		this.x = x;
		this.y = y;
	}
	Position directionalOffset(final Direction d){
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
		return new Position(x + dx, y + dy);
	}
	public ArrayList<Position> getNeighbors(GameMap gameMap) {
		ArrayList<Position> ns = new ArrayList<Position>();
		ns.add(new Position(this.x - 1, this.y).normalized(gameMap));
		ns.add(new Position(this.x + 1, this.y).normalized(gameMap));
		ns.add(new Position(this.x, this.y - 1).normalized(gameMap));
		ns.add(new Position(this.x, this.y + 1).normalized(gameMap));
		return ns;
	}
	public Position normalized(GameMap gameMap) {
		int nx = ((this.x % gameMap.width) + gameMap.width) % gameMap.width;
		int ny = ((this.y % gameMap.height) + gameMap.height) % gameMap.height;
		return new Position(nx, ny);
	}
	public Direction getDirectionTo(Position b) {// if point b is one cardinal direction away, then it'll return the direction to that point
		if(this.x - 1== b.x && this.y == b.y) {
			return Direction.WEST;
		}else if(this.x + 1== b.x && this.y == b.y) {
			return Direction.EAST;
		}else if(this.x == b.x && this.y + 1== b.y) {
			return Direction.SOUTH;
		}else if(this.x == b.x && this.y - 1 == b.y) {
			return Direction.NORTH;
		}else {
			return Direction.STILL;
		}
	}
	public double distanceTo(Position b){
		return Math.hypot(b.x - this.x, b.y - this.y);
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
	@Override
	public String toString(){
		return this.x + ", " + this.y;
	}
}
