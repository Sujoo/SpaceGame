package sujoo.games.spacegame.datatype.ship;

public class ShipComponent {

    private int currentValue;
    private int currentMaxValue;
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
        int repair = currentMaxValue / repairFraction;
        if (currentValue + repair <= currentMaxValue) {
            currentValue += repair;
        } else {
            currentValue = currentMaxValue;
        }
    }
    
    public void restoreComponent() {
        currentValue = absoluteMaxValue;
        currentMaxValue = absoluteMaxValue;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public int getCurrentMaxValue() {
        return currentMaxValue;
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
}
