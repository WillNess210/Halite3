package hlt;
public class Shipyard extends Entity{
	public Shipyard(final PlayerId owner, final Position position){
		super(owner, position, EntityId.NONE);
	}
	// TODO decrement player halite by 1000 when calling this function
	public Command spawn(){
		return Command.spawnShip();
	}
}
