package sujoo.games.spacegame.datatype.ship;

import sujoo.games.spacegame.datatype.command.AttackSubCommand;

public enum ShipComponentEnum implements ShipComponentEnumIntf {
    BASE_HULL(100, 3, 1, AttackSubCommand.HULL),
    BASE_WEAPON(25, 3, 4, AttackSubCommand.WEAPON),
    BASE_ENGINE(20, 3, 3, AttackSubCommand.ENGINE);

    private int absoluteMaxValue;
    private int repairFraction;
    private int toughness;
    private AttackSubCommand location;

    private ShipComponentEnum(int absoluteMaxValue, int repairFraction, int toughness, AttackSubCommand location) {
        this.absoluteMaxValue = absoluteMaxValue;
        this.repairFraction = repairFraction;
        this.toughness = toughness;
        this.location = location;
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
