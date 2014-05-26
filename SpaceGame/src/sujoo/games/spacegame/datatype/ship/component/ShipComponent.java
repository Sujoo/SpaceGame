package sujoo.games.spacegame.datatype.ship.component;

import java.util.List;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

import com.google.common.collect.Lists;

public class ShipComponent {

    private String name;
    private int currentValue;
    private int absoluteMaxValue;
    private double repairFraction;
    private double toughness;
    private ShipLocationCommand location;
    private CargoEnum[] materials;
    private int[] materialPrices;
    private int price;

    public ShipComponent(ShipComponentEnumIntf shipComponentEnum) {
        name = shipComponentEnum.getName();
        absoluteMaxValue = shipComponentEnum.getAbsoluteMaxValue();
        repairFraction = shipComponentEnum.getRepairFraction();
        toughness = shipComponentEnum.getToughness();
        location = shipComponentEnum.getLocation();
        materials = shipComponentEnum.getMaterials();
        materialPrices = shipComponentEnum.getMaterialPrices();
        price = shipComponentEnum.getPrice();
        restoreComponent();
    }
    
    public ShipLocationCommand getLocation() {
        return location;
    }

    public String[] toStringLabels() {
        return new String[] { "Name", "Max Value", "Repair", "Toughness", "Price", "Material Costs" };
    }

    public String[] toStringValues() {
        return new String[] { name, String.valueOf(absoluteMaxValue), String.valueOf(repairFraction),
                String.valueOf(toughness), String.valueOf(price), getMaterialCostString() };
    }

    public String getMaterialCostString() {
        String result = "";
        for (int i = 0; i < materials.length; i++) {
            result += materials[i].toString() + " : " + materialPrices[i] + " | ";
        }
        return result;
    }

    public String getName() {
        return name;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShipComponent other = (ShipComponent) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
