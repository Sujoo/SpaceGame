package sujoo.games.spacegame.datatype.ship.component;

import java.util.List;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;

import com.google.common.collect.Lists;

public class ShipComponent {

    private int currentValue;
    private int absoluteMaxValue;
    private double repairFraction;
    private double toughness;
    private CargoEnum[] materials;
    private int[] materialPrices;
    private int price;

    public ShipComponent(ShipComponentEnumIntf shipComponentEnum) {
        absoluteMaxValue = shipComponentEnum.getAbsoluteMaxValue();
        repairFraction = shipComponentEnum.getRepairFraction();
        toughness = shipComponentEnum.getToughness();
        restoreComponent();
    }
    
    public int getPrice() {
        return price;
    }
    
    public List<CargoEnum> getMaterials() {
        return Lists.newArrayList(materials);
    }
    
    public int getMaterialPrice(CargoEnum cargoEnum) {
        int result = 0;
        for (int i = 0; i < materials.length; i++) {
            if (materials[i] == cargoEnum) {
                result = materialPrices[i];
            }
        }
        return result;
    }

    public int takeDamage(int damage) {
        damage = (int) (damage * toughness);
        if (currentValue - damage >= 0) {
            currentValue -= damage;
        } else {
            damage = currentValue;
            currentValue = 0;
        }
        return damage;
    }

    public int repair() {
        int repair = (int) (absoluteMaxValue * repairFraction);
        if (currentValue + repair <= absoluteMaxValue) {
            currentValue += repair;
        } else {
            repair = absoluteMaxValue - currentValue;
            currentValue = absoluteMaxValue;
        }
        return repair;
    }

    public void restoreComponent() {
        currentValue = absoluteMaxValue;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int value) {
        currentValue = value;
    }

    public int getAbsoluteMaxValue() {
        return absoluteMaxValue;
    }
    
    public int getCurrentMaxValue() {
        return absoluteMaxValue;
    }

    public void setAbsoluteMaxValue(int value) {
        absoluteMaxValue = value;
    }

    public double getRepairFraction() {
        return repairFraction;
    }

    public double getToughness() {
        return toughness;
    }
}
