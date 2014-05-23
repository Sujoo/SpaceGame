package sujoo.games.spacegame.datatype.command;

import java.util.List;

import com.google.common.collect.Lists;

public enum AttackCommand {
	TARGET("Target", new String[]{"Fire weapons at a specific location","Usage: target <location>","Example: target hull"}),
	REPAIR("Repair", new String[]{"Repair a specific location","Usage: repair <location>","Example: repair weapon"}),
	ESCAPE("Escape", new String[]{"Attempt to escape combat","Usage: escape"});

	private String code;
	private String[] explanation;
    private AttackCommand(String code, String[] explanation) {
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
    public static AttackCommand toCommand(String code) {
    	AttackCommand result = null;
        for (AttackCommand attackCommand : AttackCommand.values()) {
            if (attackCommand.getCode().equalsIgnoreCase(code)) {
                result = attackCommand;
            }
        }
        return result;
    }
    
    public static boolean isAttackCommand(String code) {
    	if (toCommand(code) != null) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public static boolean isEscape(String code) {
    	if (toCommand(code) == ESCAPE) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public static List<AttackCommand> getList() {
    	return Lists.newArrayList(AttackCommand.values());
    }
    
    public static List<String> getCodeList() {
        List<String> codeList = Lists.newArrayList();
        AttackCommand[] list = AttackCommand.values();
        for (int i = 0; i < list.length; i++) {
            codeList.add(list[i].getCode());
        }
        return codeList;
    }
}
