package sujoo.games.spacegame.datatype.ship.component;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

public enum ShipShieldComponentEnum implements ShipComponentEnumIntf {
    BASE_SHIELD(50, 0.33, 1, ShipLocationCommand.SHIELD, 10, new CargoEnum[] { CargoEnum.FUEL }, new int[] { 100 }, 4, 0.5,
            0.25),
    ADVANCED_SHIELD(100, 0.33, 1, ShipLocationCommand.SHIELD, 10, new CargoEnum[] { CargoEnum.FUEL }, new int[] { 100 }, 1,
            0.33, 0.25);

    private int absoluteMaxValue;
    private double repairFraction;
    private double toughness;
    private ShipLocationCommand location;
    private int price;
    private CargoEnum[] materials;
    private int[] materialPrices;

    private int rechargeTime;
    private double maxValueToughness;
    private double restoreFraction;

    private ShipShieldComponentEnum(int absoluteMaxValue, double repairFraction, double toughness, ShipLocationCommand location, int price, CargoEnum[] materials, int[] materialPrices, int rechargeTime, double maxValueToughness, double restoreFraction) {
        this.absoluteMaxValue = absoluteMaxValue;
        this.repairFraction = repairFraction;
        this.toughness = toughness;
        this.location = location;
        this.price = price;
        this.materials = materials;
        this.materialPrices = materialPrices;
        
        this.rechargeTime = rechargeTime;
        this.maxValueToughness = maxValueToughness;
        this.restoreFraction = restoreFraction;
    }

    public int getRechargeTime() {
        return rechargeTime;
    }

    public double getMaxValueToughness() {
        return maxValueToughness;
    }

    public double getRestoreFraction() {
        return restoreFraction;
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
