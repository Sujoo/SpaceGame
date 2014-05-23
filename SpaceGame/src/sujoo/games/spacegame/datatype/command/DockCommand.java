package sujoo.games.spacegame.datatype.command;

import java.util.List;

import com.google.common.collect.Lists;

public enum DockCommand {
    UNDOCK("Undock", new String[]{"Undock from station","Usage: undock"}),
    CARGO("Cargo", new String[]{"Display Trading Info","Usage: cargo"}),
    BUY("Buy", new String[]{"Buy cargo from a station", "Usage: buy <number> or <all> or <max> <cargo>", "Example: buy 10 fuel", "Example: buy max ammo"}),
    SELL("Sell", new String[]{"Sell cargo to a station", "Usage: sell <number> or <all> or <max> <cargo>", "Example: sell 10 fuel", "Example: sell all ammo"}),
    STORE("Store", new String[]{"Display station store","Usage: store"});

    private String code;
    private String[] explanation;
    private DockCommand(String code, String[] explanation) {
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
    public static DockCommand toCommand(String code) {
        DockCommand result = null;
        for (DockCommand dockCommand : DockCommand.values()) {
            if (dockCommand.getCode().equalsIgnoreCase(code)) {
                result = dockCommand;
            }
        }
        return result;
    }
    
    public static boolean isDockCommand(String code) {
        if (toCommand(code) != null) {
            return true;
        } else {
            return false;
        }
    }
    
    public static List<DockCommand> getList() {
        return Lists.newArrayList(DockCommand.values());
    }
    
    public static List<String> getCodeList() {
        List<String> codeList = Lists.newArrayList();
        DockCommand[] list = DockCommand.values();
        for (int i = 0; i < list.length; i++) {
            codeList.add(list[i].getCode());
        }
        return codeList;
    }
}
