package sujoo.games.spacegame.datatype.command;

import java.util.List;

import com.google.common.collect.Lists;

public enum Command {
	JUMP("Jump", new String[]{"Travel between systems","Usage: jump <system>","Example: jump 1000"}),
	SCAN("Scan", new String[]{"Display system information", "Usage: scan", "", "Display player information", "Usage: scan <player name>", "Example: scan sujoo"}),
	STATUS("Status", new String[]{"Display your information", "Usage: status"}),
	DOCK("Dock", new String[]{"Display station information","Usage: dock"}),
	BUY("Buy", new String[]{"Buy cargo from a station", "Usage: buy <number> or <all> or <max> <cargo>", "Example: buy 10 fuel", "Example: buy max ammo"}),
	SELL("Sell", new String[]{"Sell cargo to a station", "Usage: sell <number> or <all> or <max> <cargo>", "Example: sell 10 fuel", "Example: sell all ammo"}),
	MAP("Map", new String[]{"Display system map, but think scan is better"}),
    FULL_MAP("Fullscan", new String[]{"Secret Developer Haxxor"}),
    WAIT("Wait", new String[]{"Do nothing and let AI do their thing"}),
    SCORE("Score", new String[]{"Display score screen", "Usage: score"}),
    HELP("Help", new String[]{"Use this command to learn the others", "Usage: help <command>", "Example: help jump"});

	private String code;
	private String[] explanation;
    private Command(String code, String[] explanation) {
        this.code = code;
        this.explanation = explanation;
    }
    
    public String getCode() {
        return code;
    }
    
    public List<String> getExplanation() {
    	return Lists.newArrayList(explanation);
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
