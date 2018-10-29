package hlt;

import java.util.ArrayList;

public class CommandQueue{
	public static ArrayList<Command> commandQueue = new ArrayList<Command>();
	public static void add(Command c) {
		commandQueue.add(c);
	}
	public static void clear() {
		commandQueue.clear();
	}
	public static String getLast() {
		if(commandQueue.size() == 0) {
			return "NO COMMANDS IN";
		}else {
			return commandQueue.get(commandQueue.size() - 1).command;
		}
	}
}
