package sujoo.games.spacegame.datatype.ship.component;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

public enum ShipComponentEnum implements ShipComponentEnumIntf {
    BASE_HULL("starter hull", 100, 0.33, 1, ShipLocationCommand.HULL, 10, new CargoEnum[] { CargoEnum.FUEL },
            new int[] { 100 }),
    ADV_HULL("adv hull", 150, 0.33, 1, ShipLocationCommand.HULL, 5000, new CargoEnum[] { CargoEnum.ALLOY, CargoEnum.PARTS },
            new int[] { 100, 50 }),
    BASE_WEAPON("starter weapon", 25, 0.33, 0.25, ShipLocationCommand.WEAPON, 10, new CargoEnum[] { CargoEnum.FUEL },
            new int[] { 100 }),
    BASE_ENGINE("starter engine", 20, 0.33, 0.33, ShipLocationCommand.ENGINE, 10, new CargoEnum[] { CargoEnum.FUEL },
            new int[] { 100 });

    private String name;
    private int absoluteMaxValue;
    private double repairFraction;
    private double toughness;
    private ShipLocationCommand location;
    private int price;
    private CargoEnum[] materials;
    private int[] materialPrices;

    private ShipComponentEnum(String name, int absoluteMaxValue, double repairFraction, double toughness,
            ShipLocationCommand location, int price, CargoEnum[] materials, int[] materialPrices) {
        this.name = name;
        this.absoluteMaxValue = absoluteMaxValue;
        this.repairFraction = repairFraction;
        this.toughness = toughness;
        this.location = location;
        this.price = price;
        this.materials = materials;
        this.materialPrices = materialPrices;
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
