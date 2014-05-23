package sujoo.games.spacegame.gui;

public enum BattleFeedbackEnum {
    COMPONENT_DAMAGE,
    COMPONENT_REPAIR,
    SHIELD_HIT,
    SHIELD_RECHARGE,
    SHIP_DESTROYED,
    ESCAPE;

    private String code;

    private BattleFeedbackEnum() {
        this.code = "";
    }

    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String toString() {
        return code;
    }
}
