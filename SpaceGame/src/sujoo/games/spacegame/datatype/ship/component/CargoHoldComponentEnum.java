package sujoo.games.spacegame.datatype.ship.component;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

public enum CargoHoldComponentEnum implements ShipComponentEnumIntf {
    BASE_HOLD("starter hold", 100, 0.33, 1, ShipLocationCommand.CARGOHOLD, 10, new CargoEnum[] { CargoEnum.FUEL },
            new int[] { 100 }, 100, 4),
    STATION_BASE_HOLD("starter hold", 100, 0.33, 1, ShipLocationCommand.CARGOHOLD, 10, new CargoEnum[] { CargoEnum.FUEL },
            new int[] { 100 }, 30000, 4);

    private String name;
    private int absoluteMaxValue;
    private double repairFraction;
    private double toughness;
    private ShipLocationCommand location;
    private int price;
    private CargoEnum[] materials;
    private int[] materialPrices;
    private int holdSize;
    private int componentHoldSize;

    private CargoHoldComponentEnum(String name, int absoluteMaxValue, double repairFraction, double toughness,
            ShipLocationCommand location, int price, CargoEnum[] materials, int[] materialPrices, int holdSize,
            int componentHoldSize) {
        this.name = name;
        this.absoluteMaxValue = absoluteMaxValue;
        this.repairFraction = repairFraction;
        this.toughness = toughness;
        this.location = location;
        this.price = price;
        this.materials = materials;
        this.materialPrices = materialPrices;
        this.holdSize = holdSize;
        this.componentHoldSize = componentHoldSize;
    }

    public int getHoldSize() {
        return holdSize;
    }

    public int getComponentHoldSize() {
        return componentHoldSize;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAbsoluteMaxValue() {
        return absoluteMaxValue;
    }

    @Override
    public double getRepairFraction() {
        return repairFraction;
    }

    @Override
    public double getToughness() {
        return toughness;
    }

    @Override
    public ShipLocationCommand getLocation() {
        return location;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public CargoEnum[] getMaterials() {
        return materials;
    }

    @Override
    public int[] getMaterialPrices() {
        return materialPrices;
    }
}
