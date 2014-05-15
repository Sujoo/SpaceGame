package sujoo.games.spacegame.gui;

public enum BattleFeedbackEnum {
    COMPONENT_DAMAGE("@'s @ took @ damage from @'s weapons"),
    COMPONENT_REPAIR("@'s @ repaired @ damage"),
    SHIELD_HIT("@'s shield absored @ damage from @'s weapons"),
    SHIELD_RECHARGE("@'s shields recharged by @"),
    SHIP_DESTROYED("@ was destroyed"),
    ESCAPE("@ escaped");

    private String code;

    private BattleFeedbackEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
    public String toString() {
        return code;
    }
}
