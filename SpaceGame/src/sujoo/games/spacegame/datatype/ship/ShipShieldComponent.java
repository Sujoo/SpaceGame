package sujoo.games.spacegame.datatype.ship;

public class ShipShieldComponent extends ShipComponent {

    private int rechargeTime;
    private int currentMaxValue;
    private int maxValueToughness;
    private int restoreFraction;

    public ShipShieldComponent(ShipShieldComponentEnum shipComponentEnum) {
        super(shipComponentEnum);
        rechargeTime = shipComponentEnum.getRechargeTime();
        currentMaxValue = shipComponentEnum.getAbsoluteMaxValue();
        maxValueToughness = shipComponentEnum.getMaxValueToughness();
        restoreFraction = shipComponentEnum.getRestoreFraction();
    }
    
    public int getRechargeTime() {
        return rechargeTime;
    }
    
    public int restoreShield() {
        int restoreValue = currentMaxValue / restoreFraction;
        if (getCurrentValue() + restoreValue <= currentMaxValue) {
            setCurrentValue(getCurrentValue() + restoreValue);
        } else {
            setCurrentValue(currentMaxValue);
        }
        return restoreValue;
    }
    
    @Override
    public void restoreComponent() {
        super.restoreComponent();
        currentMaxValue = getAbsoluteMaxValue();
    }
    
    @Override
    public int getCurrentMaxValue() {
        return currentMaxValue;
    }
    
    @Override
    public int takeDamage(int damage) {
        // Fix error:
        // when currentValueDamage > current value, the else block disregards toughness
        int currentValueDamage = damage / getToughness();
        if (getCurrentValue() - currentValueDamage >= 0) {
            setCurrentValue(getCurrentValue() - currentValueDamage);
            return currentValueDamage; 
        } else {
            setCurrentValue(0);
            int maxValueDamage = damage / maxValueToughness;
            if (currentMaxValue - maxValueDamage >= 0) {
                currentMaxValue -= maxValueDamage;
            } else {
                currentMaxValue = 0;
            }
            return maxValueDamage;
        }
    }
    
    @Override
    public int repair() {
        int repair = currentMaxValue / getRepairFraction();
        if (currentMaxValue + repair <= getAbsoluteMaxValue()) {
            currentMaxValue += repair;
        } else {
            currentMaxValue = getAbsoluteMaxValue();
        }
        return repair;
    }
}
