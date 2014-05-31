package sujoo.games.spacegame.datatype.ship.component;

public class ShieldComponent extends ShipComponent {

    private int rechargeTime;
    private int currentMaxValue;
    private double maxValueToughness;
    private double restoreFraction;

    public ShieldComponent(ShieldComponentEnum shipComponentEnum) {
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
        int restoreValue = (int) (currentMaxValue * restoreFraction);
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
        // Actually: this error is handled by the BattleManager isShieldUp logic block
        int currentValueDamage = (int) (damage * getToughness());
        if (getCurrentValue() - currentValueDamage >= 0) {
            setCurrentValue(getCurrentValue() - currentValueDamage);
            return currentValueDamage; 
        } else {
            int maxValueDamage = (int) (damage * maxValueToughness);
            setCurrentValue(0);
            if (currentMaxValue - maxValueDamage >= 0) {
                currentMaxValue -= maxValueDamage;
            } else {
                maxValueDamage = currentMaxValue;
                currentMaxValue = 0;
            }
            return maxValueDamage;
        }
    }
    
    @Override
    public int repair() {
        int repair = (int) (currentMaxValue * getRepairFraction());
        if (currentMaxValue + repair <= getAbsoluteMaxValue()) {
            currentMaxValue += repair;
        } else {
            repair = getAbsoluteMaxValue() - currentMaxValue;
            currentMaxValue = getAbsoluteMaxValue();
        }
        return repair;
    }
}
