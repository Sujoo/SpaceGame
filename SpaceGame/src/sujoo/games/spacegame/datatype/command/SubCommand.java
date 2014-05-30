package sujoo.games.spacegame.datatype.command;

import java.util.List;

import com.google.common.collect.Lists;

public enum SubCommand {
    BACK("Back", new String[] { "Back" }),
    MAX("Max", new String[] { "Results in max buy/sell amount of cargo type", "Usage: <buy/sell> max <cargo>",
            "Example: buy max fuel" }),
    ALL("All", new String[] { "Results in buy/sell all of cargo type", "Usage: <buy/sell> all <cargo>",
            "Example: sell all fuel" }),
    HELP("Help", new String[] { "Use this command to learn the others", "Usage: help <command>", "Example: help jump",
            "Note: type " + BACK.getCode() + " to exit help menu" }),
    H("H", new String[] { HELP.getCode() }),
    YES("Yes", new String[] { "Yes" }),
    Y("Y", new String[] { YES.getCode() });

    private String code;
    private String[] explanation;

    private SubCommand(String code, String[] explanation) {
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
    public static SubCommand toCommand(String code) {
        SubCommand result = null;
        for (SubCommand command : SubCommand.values()) {
            if (command.getCode().equalsIgnoreCase(code)) {
                result = command;
            }
        }
        return result;
    }

    public static boolean isMaxAllCommand(String code) {
        SubCommand command = toCommand(code);
        if (command != null && (command == MAX || command == ALL)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isHelpCommand(String code) {
        SubCommand command = toCommand(code);
        if (command != null && (command == HELP || command == H)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isYesCommand(String code) {
        SubCommand command = toCommand(code);
        if (command != null && (command == YES || command == Y)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBackCommand(String code) {
        SubCommand command = toCommand(code);
        if (command != null && (command == BACK)) {
            return true;
        } else {
            return false;
        }
    }

    public static List<SubCommand> getList() {
        return Lists.newArrayList(SubCommand.values());
    }
}
