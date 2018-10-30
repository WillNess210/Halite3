package hlt;
public class Dropoff extends Entity{
	public Dropoff(final PlayerId owner, final Position position, final EntityId id){
		super(owner, position, id);
	}
	static Dropoff _generate(final PlayerId playerId){
		final Input input = Input.readInput();
		final EntityId dropoffId = new EntityId(input.getInt());
		final int x = input.getInt();
		final int y = input.getInt();
		return new Dropoff(playerId, new Position(x, y), dropoffId);
	}
}
