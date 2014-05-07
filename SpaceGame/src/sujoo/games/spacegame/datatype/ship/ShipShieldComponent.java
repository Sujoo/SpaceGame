package sujoo.games.spacegame.datatype.ship;

public class ShipShieldComponent extends ShipComponent {
    private int rechargeTime;

    public ShipShieldComponent(ShipShieldComponentEnum shipComponentEnum) {
        super(shipComponentEnum);
        rechargeTime = shipComponentEnum.getRechargeTime();
    }
    
    public int getRechargeTime() {
        return rechargeTime;
    }
    
    public void restoreShield() {
        
    }
}
