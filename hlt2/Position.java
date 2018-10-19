package hlt2;
public class Position{
	public int x, y;
	public Position(final int x, final int y){
		this.x = x;
		this.y = y;
	}
	Position[] getNeighbours(){
		Position[] ns = new Position[4];
		ns[0] = this.directionalOffset(Direction.NORTH);
		ns[1] = this.directionalOffset(Direction.EAST);
		ns[2] = this.directionalOffset(Direction.SOUTH);
		ns[3] = this.directionalOffset(Direction.WEST);
		return ns;
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
	public void logln(){
		Log.logln(this.toString());
	}
	public void log(){
		Log.log(this.toString());
	}
	public String toString(){
		return "(" + this.x + ", " + this.y + ")";
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