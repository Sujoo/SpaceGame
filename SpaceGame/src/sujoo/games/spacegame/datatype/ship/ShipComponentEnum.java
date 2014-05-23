package sujoo.games.spacegame.datatype.ship;

import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

public enum ShipComponentEnum implements ShipComponentEnumIntf {
    BASE_HULL(100, 3, 1, ShipLocationCommand.HULL),
    BASE_WEAPON(25, 3, 4, ShipLocationCommand.WEAPON),
    BASE_ENGINE(20, 3, 3, ShipLocationCommand.ENGINE);

    private int absoluteMaxValue;
    private int repairFraction;
    private int toughness;
    private ShipLocationCommand location;

    private ShipComponentEnum(int absoluteMaxValue, int repairFraction, int toughness, ShipLocationCommand location) {
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

    public ShipLocationCommand getLocation() {
        return location;
    }
}
