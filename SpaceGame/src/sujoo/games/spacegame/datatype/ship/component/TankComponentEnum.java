package sujoo.games.spacegame.datatype.ship.component;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

public enum TankComponentEnum implements ShipComponentEnumIntf {
    BASE_FUEL_TANK("starter fuel tank", 20, 0.33, 0.33, ShipLocationCommand.ENGINE, 10, new CargoEnum[] { CargoEnum.FUEL },
            new int[] { 100 }),
    BASE_AMMO_TANK("starter ammo magazine", 20, 0.33, 0.33, ShipLocationCommand.ENGINE, 10,
            new CargoEnum[] { CargoEnum.FUEL },
            new int[] { 100 });

    private String name;
    private int absoluteMaxValue;
    private double repairFraction;
    private double toughness;
    private ShipLocationCommand location;
    private int price;
    private CargoEnum[] materials;
    private int[] materialPrices;

    private TankComponentEnum(String name, int absoluteMaxValue, double repairFraction, double toughness,
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
