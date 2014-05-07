package sujoo.games.spacegame.datatype.ship;

import sujoo.games.spacegame.datatype.command.AttackSubCommand;

public enum ShipShieldComponentEnum implements ShipComponentEnumIntf {
    BASE_SHIELD(50, 3, 1, 4, AttackSubCommand.SHIELD),
    ADVANCED_SHIELD(100, 3, 4, 1, AttackSubCommand.SHIELD);

    private int absoluteMaxValue;
    private int repairFraction;
    private int toughness;
    private int rechargeTime;
    private AttackSubCommand location;

    private ShipShieldComponentEnum(int absoluteMaxValue, int repairFraction, int toughness, int rechargeTime, AttackSubCommand location) {
        this.absoluteMaxValue = absoluteMaxValue;
        this.repairFraction = repairFraction;
        this.toughness = toughness;
        this.rechargeTime = rechargeTime;
        this.location = location;
    }
    
    public int getRechargeTime() {
        return rechargeTime;
    }

    public int getAbsoluteMaxValue() {
        return absoluteMaxValue;
    }

    public int getRepairFraction() {
        return repairFraction;
    }

    public int getToughness() {
        return toughness;
    }

    public AttackSubCommand getLocation() {
        return location;
    }
}
    
