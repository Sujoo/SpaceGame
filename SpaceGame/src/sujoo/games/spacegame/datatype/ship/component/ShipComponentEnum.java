package sujoo.games.spacegame.datatype.ship.component;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

public enum ShipComponentEnum implements ShipComponentEnumIntf {
    BASE_HULL(100, 0.33, 1, ShipLocationCommand.HULL, 10, new CargoEnum[] { CargoEnum.FUEL }, new int[] { 100 }),
    BASE_WEAPON(25, 0.33, 0.25, ShipLocationCommand.WEAPON, 10, new CargoEnum[] { CargoEnum.FUEL }, new int[] { 100 }),
    BASE_ENGINE(20, 0.33, 0.33, ShipLocationCommand.ENGINE, 10, new CargoEnum[] { CargoEnum.FUEL }, new int[] { 100 });

    private int absoluteMaxValue;
    private double repairFraction;
    private double toughness;
    private ShipLocationCommand location;
    private int price;
    private CargoEnum[] materials;
    private int[] materialPrices;

    private ShipComponentEnum(int absoluteMaxValue, double repairFraction, double toughness, ShipLocationCommand location,
            int price, CargoEnum[] materials, int[] materialPrices) {
        this.absoluteMaxValue = absoluteMaxValue;
        this.repairFraction = repairFraction;
        this.toughness = toughness;
        this.location = location;
        this.price = price;
        this.materials = materials;
        this.materialPrices = materialPrices;
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
