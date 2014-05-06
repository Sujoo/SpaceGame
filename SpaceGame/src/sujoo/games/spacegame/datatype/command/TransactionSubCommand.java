package sujoo.games.spacegame.datatype.command;

import java.util.List;

import com.google.common.collect.Lists;

public enum TransactionSubCommand {
	MAX("Max", new String[]{"Results in max buy/sell amount of cargo type", "Usage: <buy/sell> max <cargo>", "Example: buy max fuel"}),
	ALL("All", new String[]{"Results in buy/sell all of cargo type", "Usage: <buy/sell> all <cargo>", "Example: sell all fuel"});

	private String code;
	private String[] explanation;
    private TransactionSubCommand(String code, String[] explanation) {
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
    public static TransactionSubCommand toCommand(String code) {
    	TransactionSubCommand result = null;
        for (TransactionSubCommand command : TransactionSubCommand.values()) {
            if (command.getCode().equalsIgnoreCase(code)) {
                result = command;
            }
        }
        return result;
    }
    
    public static boolean isMaxAllCommand(String code) {
    	TransactionSubCommand command = toCommand(code);
    	if (command != null && (command == MAX || command == ALL)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public static List<TransactionSubCommand> getList() {
    	return Lists.newArrayList(TransactionSubCommand.values());
    }
 }
