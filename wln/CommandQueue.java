package wln;

import java.util.ArrayList;
import hlt.*;

public class CommandQueue{
	private static ArrayList<Command> commandQueue;
	public static void init() {
		commandQueue = new ArrayList<Command>();
	}
	public static void add(Command a) {
		commandQueue.add(a);
	}
	public static void addSpawn(Player me) {
		commandQueue.add(me.shipyard.spawn());
	}
	public static void clear() {
		commandQueue.clear();
	}
	public static ArrayList<Command> getCommands(){
		return commandQueue;
	}
}
