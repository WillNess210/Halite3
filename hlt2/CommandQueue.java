package hlt2;

import java.util.ArrayList;

public class CommandQueue{
	public static ArrayList<Command> commandQueue = new ArrayList<Command>();
	public static void add(Command c) {
		commandQueue.add(c);
	}
	public static void clear() {
		commandQueue.clear();
	}
}
