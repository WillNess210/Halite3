package wln;

import java.util.ArrayList;
import hlt.*;

public class CollisionAvoidance{
	public static ArrayList<Position> futurePositions = new ArrayList<Position>();

	public static void clear() {
		futurePositions.clear();
	}
	public static void add(Position a) {
		futurePositions.add(a);
	}
	public static boolean isSafe(Position a) {
		return !futurePositions.contains(a);
	}
}
