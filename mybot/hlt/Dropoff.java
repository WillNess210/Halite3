package hlt;
public class Dropoff extends Entity{
	public Dropoff(final int x, final int y, final PlayerId owner, final EntityId id){
		super(x, y, owner, id);
	}
	static Dropoff _generate(final PlayerId playerId){
		final Input input = Input.readInput();
		final EntityId dropoffId = new EntityId(input.getInt());
		final int x = input.getInt();
		final int y = input.getInt();
		return new Dropoff(x, y, playerId, dropoffId);
	}
}
