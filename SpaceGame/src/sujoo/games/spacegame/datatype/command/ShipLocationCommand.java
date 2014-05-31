package sujoo.games.spacegame.datatype.command;

import java.util.List;

import com.google.common.collect.Lists;

public enum ShipLocationCommand {
    ENGINE("Engine", new String[] { "words" }, true),
    WEAPON("Weapon", new String[] { "words" }, true),
    SHIELD("Shield", new String[] { "words" }, true),
    HULL("Hull", new String[] { "words" }, true),
    CARGOHOLD("CargoHold", new String[] { "words" }, false),
    FUELTANK("FuelTank", new String[] { "words" }, false),
    MAGAZINE("Magazine", new String[] { "words" }, false);

    private String code;
    private String[] explanation;
    private boolean targetable;

    private ShipLocationCommand(String code, String[] explanation, boolean targetable) {
        this.code = code;
        this.explanation = explanation;
        this.targetable = targetable;
    }
    
    public boolean isTargetable() {
        return targetable;
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
     * Attempts to match an input String with a command If no match is found,
     * the null is returned
     * 
     * @param code
     * @return
     */
    public static ShipLocationCommand toCommand(String code) {
        ShipLocationCommand result = null;
        for (ShipLocationCommand attackSubCommand : ShipLocationCommand.values()) {
            if (attackSubCommand.getCode().equalsIgnoreCase(code)) {
                result = attackSubCommand;
            }
        }
        return result;
    }

    public static boolean isShipLocationCommand(String code) {
        if (toCommand(code) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static List<ShipLocationCommand> getList() {
        return Lists.newArrayList(ShipLocationCommand.values());
    }
}
