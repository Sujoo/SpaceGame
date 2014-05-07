package sujoo.games.spacegame.datatype.command;

import java.util.List;

import com.google.common.collect.Lists;

public enum AttackSubCommand {
    ENGINE("Engine", new String[] { "words" }),
    WEAPON("Weapon", new String[] { "words" }),
    SHIELD("Shield", new String[] { "words" }),
    HULL("Hull", new String[] { "words" });

    private String code;
    private String[] explanation;

    private AttackSubCommand(String code, String[] explanation) {
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
     * Attempts to match an input String with a command If no match is found,
     * the null is returned
     * 
     * @param code
     * @return
     */
    public static AttackSubCommand toCommand(String code) {
        AttackSubCommand result = null;
        for (AttackSubCommand attackSubCommand : AttackSubCommand.values()) {
            if (attackSubCommand.getCode().equalsIgnoreCase(code)) {
                result = attackSubCommand;
            }
        }
        return result;
    }

    public static boolean isAttackSubCommand(String code) {
        if (toCommand(code) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static List<AttackSubCommand> getList() {
        return Lists.newArrayList(AttackSubCommand.values());
    }
}
