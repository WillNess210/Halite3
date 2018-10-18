package wln;

import java.util.ArrayList;
import hlt.Position;

public class AStarNode extends Position{
	public AStarNode parent;
	public double distTraveled, distFrom;
	public AStarNode(int x, int y){
		super(x, y);
		parent = null;
		distTraveled = 9999;
		distFrom = 9999;
	}
	public ArrayList<AStarNode> neighbors(AStarNode[][] map){
		ArrayList<AStarNode> nbrs = new ArrayList<AStarNode>();
		nbrs.add(map[(((this.x + 1) % map.length) + map.length) % map.length][(((this.y) % map[0].length) + map[0].length) % map[0].length]);
		nbrs.add(map[(((this.x - 1) % map.length) + map.length) % map.length][(((this.y) % map[0].length) + map[0].length) % map[0].length]);
		nbrs.add(map[(((this.x) % map.length) + map.length) % map.length][(((this.y + 1) % map[0].length) + map[0].length) % map[0].length]);
		nbrs.add(map[(((this.x) % map.length) + map.length) % map.length][(((this.y - 1) % map[0].length) + map[0].length) % map[0].length]);
		return nbrs;
	}
	public double score() {
		return this.distFrom + this.distTraveled;
	}
}
