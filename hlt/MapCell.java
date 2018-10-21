package hlt;
public class MapCell{
	public final Position position;
	public int halite, aDistTraveled, aDistFrom;
	public MapCell parent;
	public Ship ship;
	public Entity structure;
	public MapCell(final Position position, final int halite){
		this.position = position;
		this.halite = halite;
		this.aDistTraveled = 0;
		this.aDistFrom = 9999;
		this.parent = null;
	}
	MapCell[] getNeighbours(GameMap gameMap){
		Position[] ns = this.position.getNeighbours(gameMap);
		MapCell[] nsm = new MapCell[ns.length];
		for(int i = 0; i < nsm.length; i++){
			nsm[i] = gameMap.at(ns[i]);
		}
		return nsm;
	}
	public int AStarScore(){
		return this.aDistFrom + this.aDistTraveled;
	}
	public int factorOfHundred(){
		return this.halite / 100;
	}
	public boolean isEmpty(){
		return ship == null && structure == null;
	}
	public boolean canMoveOn(Player me){
		return ship == null || (me.shipyard.position.samePosition(this.position) && this.ship != null && this.ship.owner.id != me.id.id);
	}
	public boolean isOccupied(){
		return ship != null;
	}
	public boolean hasStructure(){
		return structure != null;
	}
	public void markSafe(){
		this.ship = null;
	}
	public void markUnsafe(final Ship ship){
		this.ship = ship;
	}
}
