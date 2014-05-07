package sujoo.games.spacegame.datatype.ship;

public class ShipComponent {

    private int currentValue;
    private int absoluteMaxValue;
    private int repairFraction;
    private int toughness;

    public ShipComponent(ShipComponentEnumIntf shipComponentEnum) {
        absoluteMaxValue = shipComponentEnum.getAbsoluteMaxValue();
        repairFraction = shipComponentEnum.getRepairFraction();
        toughness = shipComponentEnum.getToughness();
        restoreComponent();
    }

    public void takeDamage(int damage) {
        damage = damage / toughness;
        if (currentValue - damage >= 0) {
            currentValue -= damage;
        } else {
            currentValue = 0;
        }
    }

    public void repair() {
        int repair = absoluteMaxValue / repairFraction;
        if (currentValue + repair <= absoluteMaxValue) {
            currentValue += repair;
        } else {
            currentValue = absoluteMaxValue;
        }
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

    public int getRepairFraction() {
        return repairFraction;
    }

    public int getToughness() {
        return toughness;
    }
}
