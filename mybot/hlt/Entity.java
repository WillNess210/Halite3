package hlt;
public class Entity extends Position{
	public final PlayerId owner;
	public final EntityId id;
	public Entity(final int x, final int y, final PlayerId owner, final EntityId id){
		super(x, y);
		this.owner = owner;
		this.id = id;
	}
	@Override
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		Entity entity = (Entity) o;
		if(!owner.equals(entity.owner))
			return false;
		if(!id.equals(entity.id))
			return false;
		return this.x == entity.x && this.y == entity.y;
	}
	@Override
	public int hashCode(){
		int result = owner.hashCode();
		result = 31 * result + id.hashCode();
		result = 31 * result + (31 * this.x + this.y);
		return result;
	}
}
