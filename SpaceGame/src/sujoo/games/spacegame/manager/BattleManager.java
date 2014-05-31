package sujoo.games.spacegame.manager;

import com.google.common.base.Optional;

import sujoo.games.spacegame.datatype.player.Player;
import sujoo.games.spacegame.datatype.ship.Ship;
import sujoo.games.spacegame.datatype.ship.component.ShipComponent;
import sujoo.games.spacegame.datatype.ship.component.ShieldComponent;
import sujoo.games.spacegame.gui.BattleFeedbackEnum;

public class BattleManager {

    public static BattleFeedbackEnum escape(Player escapingPlayer, Player otherPlayer) {
        BattleFeedbackEnum feedback = BattleFeedbackEnum.ESCAPE;
        feedback.setCode(escapingPlayer.getName() + " escaped!");
        return feedback;
    }

    public static BattleFeedbackEnum damageComponent(Player player, ShipComponent component, int damage) {
        Ship ship = player.getShip();
        BattleFeedbackEnum feedback = null;
        // If ship has shields, damage those first
        Optional<ShieldComponent> shieldOptional = ship.getShield();
        if (ship.isShieldUp()) {
            if (shieldOptional.isPresent() && shieldOptional.get().getCurrentValue() >= damage) {
                int damageTaken = shieldOptional.get().takeDamage(damage);
                feedback = BattleFeedbackEnum.SHIELD_HIT;
                feedback.setCode(player.getName() + " Shield absorbed " + damageTaken + " damage");
            } else {
                int shieldDamage = shieldOptional.get().getCurrentValue();
                int remainingDamage = damage - shieldDamage;
                int shieldDamageTaken = shieldOptional.get().takeDamage(shieldDamage);
                int componentDamageTaken = component.takeDamage(remainingDamage);
                feedback = BattleFeedbackEnum.COMPONENT_DAMAGE;
                feedback.setCode(player.getName() + " Shield absorbed " + shieldDamageTaken + " damage"
                        + System.lineSeparator() + player.getName() + " " + component.getLocation().getCode() + " took "
                        + componentDamageTaken + " damage");
            }
        } else {
            int damageTaken = component.takeDamage(damage);
            feedback = BattleFeedbackEnum.COMPONENT_DAMAGE;
            feedback.setCode(player.getName() + " " + component.getLocation().getCode() + " took " + damageTaken + " damage");
        }

        if (ship.isDestoryed()) {
            feedback = BattleFeedbackEnum.SHIP_DESTROYED;
            feedback.setCode(player.getName() + " destroyed");
        }
        return feedback;
    }

    public static BattleFeedbackEnum repairComponent(Player player, ShipComponent component) {
        BattleFeedbackEnum feedback;
        int repaired = component.repair();
        feedback = BattleFeedbackEnum.COMPONENT_REPAIR;
        feedback.setCode(player.getName() + " repaired " + component.getLocation().getCode() + " by " + repaired);
        return feedback;
    }

    public static BattleFeedbackEnum performShieldRecharge(Player player, int battleCounter) {
        BattleFeedbackEnum feedback = null;
        Ship ship = player.getShip();
        Optional<ShieldComponent> shieldOptional = ship.getShield();
        if (shieldOptional.isPresent()) {
            ShieldComponent shield = shieldOptional.get();
            if (battleCounter % shield.getRechargeTime() == 0) {
                int restored = shield.restoreShield();
                feedback = BattleFeedbackEnum.SHIELD_RECHARGE;
                feedback.setCode(player.getName() + " recharged " + restored + " shields");
            }
        }
        return feedback;
    }

}
