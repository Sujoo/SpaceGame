package sujoo.games.spacegame.datatypes;

import java.util.List;

import com.google.common.collect.Lists;

public enum Command {
	JUMP("Jump"),
	SCAN("Scan"),
	STATUS("Status"),
	DOCK("Dock"),
	BUY("Buy"),
	SELL("Sell"),
	MAP("Map"),
    FULL_MAP("Fullscan"),
    WAIT("Wait"),
    SCORE("Score"),
    HELP("Help");

	private String code;
    private Command(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    public String toString() {
    	return code;
    }

    /**
     * Attempts to match an input String with a command
     * If no match is found, the Unknown type is returned
     * 
     * @param code
     * @return
     */
    public static Command toCommand(String code) {
    	Command result = null;
        for (Command command : Command.values()) {
            if (command.getCode().equalsIgnoreCase(code)) {
                result = command;
            }
        }
        return result;
    }
    
    public static List<Command> getList() {
    	return Lists.newArrayList(Command.values());
    }
}
