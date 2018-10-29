package hlt;
public class Shipyard extends Entity{
	public Shipyard(final int x, final int y, final PlayerId owner){
		super(x, y, owner, EntityId.NONE);
	}
	// TODO decrement player halite by 1000 when calling this function
	public Command spawn(){
		return Command.spawnShip();
	}
}
