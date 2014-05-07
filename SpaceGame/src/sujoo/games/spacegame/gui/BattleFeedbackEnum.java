package sujoo.games.spacegame.gui;

public enum BattleFeedbackEnum {
    COMPONENT_DAMAGE("Component was damaged"),
    COMPONENT_REPAIR("Component was repaired"),
    SHIELD_HIT("Player's shield was hit instead"),
    SHIELD_RECHARGE("Player's shields have recharged"),
    SHIP_DESTROYED("Player was destroyed"),
    ESCAPE("Player escaped");

    private String code;

    private BattleFeedbackEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
