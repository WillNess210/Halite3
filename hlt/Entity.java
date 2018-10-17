package hlt;

public class Entity {
    public final PlayerId owner;
    public final EntityId id;
    public final Position position;

    public Entity(final PlayerId owner, final EntityId id, final Position position) {
        this.owner = owner;
        this.id = id;
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        if (!owner.equals(entity.owner)) return false;
        if (!id.equals(entity.id)) return false;
        return position.equals(entity.position);
    }
    public void setX(int x) {
    	this.position.x = x;
    }
    public int getX() {
    	return this.position.x;
    }
    public void setY(int y) {
    	this.position.y = y;
    }
    public int getY() {
    	return this.position.y;
    }
    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + position.hashCode();
        return result;
    }
}
