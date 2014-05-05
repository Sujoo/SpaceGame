package sujoo.games.spacegame.datatypes;

public enum Command {
	SCAN("scan"),
	MAP("map"),
    FULL_MAP("fullscan"),
    JUMP("jump"),
    DOCK("dock"),
    BUY("buy"),
    SELL("sell"),
    STATUS("status"),
    HELP("help"),
    SCORE("score"),
    UNKNOWN("unknown");

	private String code;
    private Command(String code) {
        this.code = code;
    }
    
    private String getCode() {
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
    	Command result = UNKNOWN;
        for (Command command : Command.values()) {
            if (command.getCode().equalsIgnoreCase(code)) {
                result = command;
            }
        }
        return result;
    }

    /**
     * Does the entered string match a command?
     * @param pos
     * @return
     */
    public static boolean isCommand(String command) {
        return isCommand(toCommand(command));
    }

    private static boolean isCommand(Command command) {
        if (command != UNKNOWN) {
            return true;
        } else {
            return false;
        }
    }
}
