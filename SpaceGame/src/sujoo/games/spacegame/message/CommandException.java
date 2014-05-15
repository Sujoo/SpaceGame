package sujoo.games.spacegame.message;

public class CommandException extends Exception {

    private static final long serialVersionUID = -320068272995021302L;
    
    private String command;
    
    public CommandException(String command) {
        this.command = command;
    }
    
    @Override
    public String getMessage() {
        return command;
    }
}
