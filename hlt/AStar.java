package hlt;
import java.util.ArrayList;

public class AStar{
	public static Direction aStar(Ship s, GameMap gameMap, Player me, Position goal){
		if(s.position.distanceTo(goal) <= 1){ // if we're 1 away, don't bother with A*
			Log.logVar("A*", "REROUTED TO NATIVE");
			gameMap.at(goal).aDistTraveled = 0;
			return gameMap.naiveNavigate(s, goal, me);
		}
		for(int j = 0; j < gameMap.height; j++){
			for(int i = 0; i < gameMap.width; i++){
				gameMap.at(i, j).aDistTraveled = 0;
				gameMap.at(i, j).aDistFrom = gameMap.at(i, j).position.distanceTo(goal);
				gameMap.at(i, j).parent = null;
			}
		}
		ArrayList<MapCell> open = new ArrayList<MapCell>();
		ArrayList<MapCell> closed = new ArrayList<MapCell>();
		gameMap.at(s.position.x, s.position.y).aDistTraveled = 0;
		open.add(gameMap.at(s.position.x, s.position.y));
		MapCell target = null;
		while(open.size() > 0){
			MapCell lowest = open.get(0);
			for(MapCell test : open){
				if(test.AStarScore() < lowest.AStarScore()){
					lowest = test;
				}
			}
			open.remove(lowest);
			closed.add(lowest);
			if(lowest.position.x == goal.x && lowest.position.y == goal.y){
				target = lowest;
				break;
			}
			for(MapCell nbr : lowest.getNeighbours(gameMap)){
				if(closed.contains(nbr) || (!nbr.canMoveOn(me) && s.position.distanceTo(nbr.position) < 2)){
					continue;
				}
				int distanceScore = lowest.aDistTraveled + 1;
				if(!open.contains(nbr)){
					open.add(nbr);
				}else if(distanceScore >= nbr.aDistTraveled){
					continue;
				}
				nbr.parent = lowest;
				nbr.aDistTraveled = distanceScore;
			}
		}
		if(target == null || target.parent == null){
			Log.logVar("A*", "Fail");
			return Direction.STILL;
		}else{
			while(target.parent.parent != null){
				target = target.parent;
			}
			Log.logVar("A*", target.position.toString());
			return gameMap.naiveNavigate(s, target.position, me);
		}
	}
}
