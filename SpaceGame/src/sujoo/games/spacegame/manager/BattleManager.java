package sujoo.games.spacegame.manager;

import sujoo.games.spacegame.datatype.command.AttackSubCommand;
import sujoo.games.spacegame.datatype.ship.Ship;
import sujoo.games.spacegame.gui.BattleFeedbackEnum;

public class BattleManager {
    public static BattleFeedbackEnum repairDamage(AttackSubCommand location, Ship ship) {
        BattleFeedbackEnum result = null;
        switch (location) {
        case SHIELD:
            ship.repairShieldDamage();
            result = BattleFeedbackEnum.SHIELD_REPAIR;
            break;
        case HULL:
            ship.repairCurrentHullPoints();
            result = BattleFeedbackEnum.HULL_REPAIR;
            break;
        case WEAPON:
            ship.repairWeaponDamage();
            result = BattleFeedbackEnum.WEAPON_REPAIR;
            break;
        case ENGINE:
            ship.repairEngineDamage();
            result = BattleFeedbackEnum.ENGINE_REPAIR;
            break;
        }
        return result;
    }

    public static BattleFeedbackEnum takeDamage(AttackSubCommand location, Ship ship, int damage) {
        BattleFeedbackEnum result = null;
        if (ship.getCurrentShieldPoints() >= damage) {
            ship.removeCurrentShieldPoints(damage);
            result = BattleFeedbackEnum.SHIELD_HIT;
        } else {
            int remainingDamage = damage - ship.getCurrentShieldPoints();
            ship.removeCurrentShieldPoints(ship.getCurrentShieldPoints());
            switch (location) {
            case SHIELD:
                ship.damageShields(remainingDamage);
                result = BattleFeedbackEnum.SHIELD_DAMAGE;
                break;
            case HULL:
                if (hasEnoughHitPoints(ship, remainingDamage)) {
                    ship.removeCurrentHullPoints(remainingDamage);
                    result = BattleFeedbackEnum.HULL_HIT;
                } else {
                    ship.removeCurrentHullPoints(ship.getCurrentHullPoints());
                    result = BattleFeedbackEnum.SHIP_DESTROYED;
                }
                break;
            case WEAPON:
                ship.damageWeapon(remainingDamage);
                result = BattleFeedbackEnum.WEAPON_DAMAGE;
                break;
            case ENGINE:
                ship.damageEngine(remainingDamage);
                result = BattleFeedbackEnum.ENGINE_DAMAGE;
                break;
            }
        }

        return result;
    }

    public static boolean hasEnoughHitPoints(Ship ship, int damage) {
        if (ship.getCurrentShieldPoints() + ship.getCurrentHullPoints() > damage) {
            return true;
        } else {
            return false;
        }
    }

    public static BattleFeedbackEnum performShieldRecharge(Ship ship, int battleCounter) {
        BattleFeedbackEnum feedback = null;
        if (battleCounter % ship.getShieldRechargeTime() == 0) {
            ship.restoreShieldPoints();
            feedback = BattleFeedbackEnum.SHIELD_RECHARGE;
        }
        return feedback;
    }

    public static boolean isEscapePossible(Ship ship, int battleCounter) {
        if (battleCounter >= ship.getEscapeTurns()) {
            return true;
        } else {
            return false;
        }
    }
}
