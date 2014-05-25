package sujoo.games.spacegame.manager;

import sujoo.games.spacegame.datatype.command.ShipLocationCommand;
import sujoo.games.spacegame.datatype.player.Player;
import sujoo.games.spacegame.datatype.ship.Ship;
import sujoo.games.spacegame.datatype.ship.component.ShipComponent;
import sujoo.games.spacegame.datatype.ship.component.ShipShieldComponent;
import sujoo.games.spacegame.gui.BattleFeedbackEnum;

public class BattleManager {

    public static BattleFeedbackEnum damageComponent(Player player, ShipLocationCommand location, int damage) {
        Ship ship = player.getShip();
        BattleFeedbackEnum feedback = null;
        // if the component isn't on the ship, than the player input the wrong
        // command so leave feedback null
        if (ship.hasComponent(location)) {
            // If ship has shields, damage those first
            if (ship.isShieldUp()) {
                ShipComponent shield = ship.getComponent(ShipLocationCommand.SHIELD);
                if (shield.getCurrentValue() >= damage) {
                    int damageTaken = ship.getComponent(ShipLocationCommand.SHIELD).takeDamage(damage);
                    feedback = BattleFeedbackEnum.SHIELD_HIT;
                    feedback.setCode(player.getName() + " Shield absorbed " + damageTaken + " damage");
                } else {
                    int shieldDamage = shield.getCurrentValue();
                    int remainingDamage = damage - shieldDamage;
                    int shieldDamageTaken = shield.takeDamage(shieldDamage);
                    int componentDamageTaken = ship.getComponent(location).takeDamage(remainingDamage);
                    feedback = BattleFeedbackEnum.COMPONENT_DAMAGE;
                    feedback.setCode(player.getName() + " Shield absorbed " + shieldDamageTaken + " damage"
                            + System.lineSeparator() + player.getName() + " " + location.getCode() + " took "
                            + componentDamageTaken + " damage");
                }
            } else {
                int damageTaken = ship.getComponent(location).takeDamage(damage);
                feedback = BattleFeedbackEnum.COMPONENT_DAMAGE;
                feedback.setCode(player.getName() + " " + location.getCode() + " took " + damageTaken + " damage");
            }

            if (ship.isDestoryed()) {
                feedback = BattleFeedbackEnum.SHIP_DESTROYED;
                feedback.setCode(player.getName() + " destroyed");
            }
        }
        return feedback;
    }

    public static BattleFeedbackEnum repairComponent(Player player, ShipLocationCommand location) {
        BattleFeedbackEnum feedback = null;
        Ship ship = player.getShip();
        if (ship.hasComponent(location)) {
            int repaired = ship.getComponent(location).repair();
            feedback = BattleFeedbackEnum.COMPONENT_REPAIR;
            feedback.setCode(player.getName() + " repaired " + location.getCode() + " by " + repaired);
        }
        return feedback;
    }

    public static BattleFeedbackEnum performShieldRecharge(Player player, int battleCounter) {
        BattleFeedbackEnum feedback = null;
        Ship ship = player.getShip();
        if (ship.hasComponent(ShipLocationCommand.SHIELD)) {
            ShipShieldComponent shield = (ShipShieldComponent) ship.getComponent(ShipLocationCommand.SHIELD);
            if (battleCounter % shield.getRechargeTime() == 0) {
                int restored = shield.restoreShield();
                feedback = BattleFeedbackEnum.SHIELD_RECHARGE;
                feedback.setCode(player.getName() + " recharged " + restored + " shields");
            }
        }
        return feedback;
    }

}
