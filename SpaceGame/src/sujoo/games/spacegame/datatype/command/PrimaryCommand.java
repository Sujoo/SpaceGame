package sujoo.games.spacegame.datatype.command;

import java.util.List;

import com.google.common.collect.Lists;

public enum PrimaryCommand {
	JUMP("Jump", new String[]{"Travel between systems","Usage: jump <system>","Example: jump 1000"}),
	ATTACK("Attack", new String[]{"Attack a player or station","Usage: attack <player>","Example: attack sujoo"}),
	SCAN("Scan", new String[]{"Display system information", "Usage: scan", "", "Display player information", "Usage: scan <player name>", "Example: scan sujoo"}),
	STATUS("Status", new String[]{"Display your information", "Usage: status"}),
	DOCK("Dock", new String[]{"Display station information","Usage: dock"}),
	MAP("Map", new String[]{"Display system map, but think scan is better"}),
    FULL_MAP("Fullscan", new String[]{"Secret Developer Haxxor"}),
    WAIT("Wait", new String[]{"Do nothing and let AI do their thing"}),
    SCORE("Score", new String[]{"Display score screen", "Usage: score"});

	private String code;
	private String[] explanation;
    private PrimaryCommand(String code, String[] explanation) {
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
     * If no match is found, the null is returned
     * 
     * @param code
     * @return
     */
    public static PrimaryCommand toCommand(String code) {
    	PrimaryCommand result = null;
        for (PrimaryCommand primaryCommand : PrimaryCommand.values()) {
            if (primaryCommand.getCode().equalsIgnoreCase(code)) {
                result = primaryCommand;
            }
        }
        return result;
    }
    
    public static boolean isPrimaryCommand(String code) {
    	if (toCommand(code) != null) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public static List<PrimaryCommand> getList() {
    	return Lists.newArrayList(PrimaryCommand.values());
    }
    
    public static List<String> getCodeList() {
        List<String> codeList = Lists.newArrayList();
        PrimaryCommand[] list = PrimaryCommand.values();
        for (int i = 0; i < list.length; i++) {
            codeList.add(list[i].getCode());
        }
        return codeList;
    }
}
