package sujoo.games.spacegame.gui;

public enum ErrorEnum {
    INVALID_TRANSACTION_AMOUNT("Invalid transaction amount"),
    PLAYER_NO_CARGO_SPACE("@ has insufficient cargo space"),
    STATION_NO_CARGO_SPACE("@ has insufficient cargo space"),
    PLAYER_NO_MONEY("@ has insufficient funds"),
    STATION_NO_MONEY("@ has insufficient funds"),
    INVALID_PLAYER_NAME("@: Invalid player name"),
    PLAYER_NOT_IN_SYSTEM("@ is not in system"),
    INVALID_PRIMARY_COMMAND("@: Invalid primary command"),
    INVALID_SECONDARY_COMMAND("@: Invalid secondary command"),
    NOT_ENOUGH_INPUT("@: Command requires additional input");

    private String code;

    private ErrorEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String toString() {
        return code;
    }
}
