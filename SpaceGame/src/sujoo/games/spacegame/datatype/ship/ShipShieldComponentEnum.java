package sujoo.games.spacegame.datatype.ship;

import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

public enum ShipShieldComponentEnum implements ShipComponentEnumIntf {
    BASE_SHIELD(50, 3, 1, ShipLocationCommand.SHIELD, 4, 2, 4),
    ADVANCED_SHIELD(100, 3, 1, ShipLocationCommand.SHIELD, 1, 3, 4);

    private int absoluteMaxValue;
    private int repairFraction;
    private int toughness;
    private ShipLocationCommand location;

    private int rechargeTime;
    private int maxValueToughness;
    private int restoreFraction;

    private ShipShieldComponentEnum(int absoluteMaxValue, int repairFraction, int toughness, ShipLocationCommand location, int rechargeTime,
            int maxValueToughness, int restoreFraction) {
        this.absoluteMaxValue = absoluteMaxValue;
        this.repairFraction = repairFraction;
        this.toughness = toughness;
        this.location = location;
        this.rechargeTime = rechargeTime;
        this.maxValueToughness = maxValueToughness;
        this.restoreFraction = restoreFraction;
    }

    public int getRechargeTime() {
        return rechargeTime;
    }

    public int getMaxValueToughness() {
        return maxValueToughness;
    }
    
    public int getRestoreFraction() {
        return restoreFraction;
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

    public ShipLocationCommand getLocation() {
        return location;
    }
}
