package sujoo.games.spacegame.manager;

import sujoo.games.spacegame.ai.PlayerManagerAI;
import sujoo.games.spacegame.datatype.command.AttackCommand;
import sujoo.games.spacegame.datatype.command.AttackSubCommand;
import sujoo.games.spacegame.datatype.player.AIPlayer;
import sujoo.games.spacegame.datatype.player.Player;
import sujoo.games.spacegame.datatype.ship.Ship;
import sujoo.games.spacegame.gui.ErrorEnum;
import sujoo.games.spacegame.gui.MainGui;

public class BattleManager {
	
	private Player user;
	private Player other;
	private MainGui gui;
	private GameManager gameManager;
	private boolean inBattle;
	private int battleCounter;

	public BattleManager(MainGui gui, GameManager gameManager) {
		this.gui = gui;
		this.gameManager = gameManager;
		user = null;
		other = null;
		inBattle = false;
		battleCounter = 0;
	}
	
	public void startBattle(Player user, Player other) {
		this.user = user;
		this.other = other;
		inBattle = true;
		battleCounter = 1;
		displayBattle();
	}
	
	private void displayBattle() {
		gui.displayBattle(user, other);
	}
	
	public void enterCommand(String[] commandString) {
		if (AttackCommand.isAttackCommand(commandString[0])) {
			if (AttackCommand.isEscape(commandString[0]) || (commandString.length > 1 && AttackSubCommand.isAttackSubCommand(commandString[1]))) {
				AttackCommand firstCommand = AttackCommand.toCommand(commandString[0]);
				switch (firstCommand) {
				case TARGET:
					inflictDamage(AttackSubCommand.toCommand(commandString[1]), user, other);
					break;
				case REPAIR:
					repairDamage(AttackSubCommand.toCommand(commandString[1]), user);
					break;
				case ESCAPE:
					escape(user);
					break;
				}
				
				if (other instanceof AIPlayer && inBattle) {
					PlayerManagerAI.attackPlayer((AIPlayer) other, user, this);
					if (inBattle) {
						displayBattle();
					}
				} else {
					// opponent human player goes
				}
				performShieldRecharge();
				battleCounter++;
				if (isEscapePossible(user)) {
					System.out.println("Escape possible!");
				}
			} else {
				gui.displayError(ErrorEnum.INVALID_SECONDARY_COMMAND);
			}
		} else {
			// Get Attack Help
			gui.displayError(ErrorEnum.INVALID_PRIMARY_COMMAND);
		}
	}
	
	public void escape(Player player) {
		if (isEscapePossible(player)) {
			System.out.println(player.getName() + " escaped!");
			user.getShip().fullRestore();
			other.getShip().fullRestore();
			inBattle = false;
			gui.displayStatus(player);
		} else {
			System.out.println("Derp, failed escape!");
		}
	}
	
	public boolean isEscapePossible(Player player) {
		if (battleCounter >= player.getShip().getEscapeTurns()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void inflictDamage(AttackSubCommand location, Player attacker, Player defender) {
		int damage = attacker.getShip().getWeaponAttack();
		// Will defender survive this attack?
		if (hasEnoughHitPoints(defender.getShip(), damage)) {
			takeDamage(location, defender.getShip(), damage);
		} else { // Nope!
			System.out.println(attacker.getName() + " wins the battle!");
			attacker.getShip().fullRestore();
			inBattle = false;
			gui.displayStatus(attacker);
			gameManager.playerKilled(defender);
		}
	}
	
	public void repairDamage(AttackSubCommand location, Player player) {
		Ship ship = player.getShip();
		switch(location) {
		case SHIELD:
			ship.repairShieldDamage();
			break;
		case HULL:
			ship.repairCurrentHullPoints();
			break;
		case WEAPON:
			ship.repairWeaponDamage();
			break;
		case ENGINE:
			ship.repairEngineDamage();
			break;
		}
	}
	
	private void takeDamage(AttackSubCommand location, Ship ship, int damage) {
		if (ship.getCurrentShieldPoints() >= damage) {
			ship.removeCurrentShieldPoints(damage);
		} else {
			int remainingDamage = damage - ship.getCurrentShieldPoints();
			ship.removeCurrentShieldPoints(ship.getCurrentShieldPoints());
			switch(location) {
			case SHIELD:
				ship.damageShields(remainingDamage);
				break;
			case HULL:
				ship.removeCurrentHullPoints(remainingDamage);
				break;
			case WEAPON:
				ship.damageWeapon(remainingDamage);
				break;
			case ENGINE:
				ship.damageEngine(remainingDamage);
				break;
			}
		}
	}
	
	private boolean hasEnoughHitPoints(Ship ship, int damage) {
		if (ship.getCurrentShieldPoints() + ship.getCurrentHullPoints() > damage) {
			return true;
		} else {
			return false;
		}
	}
	
	private void performShieldRecharge() {
		performShieldRecharge(user.getShip());
		performShieldRecharge(other.getShip());
	}
	
	private void performShieldRecharge(Ship ship) {
		if (battleCounter % ship.getShieldRechargeTime() == 0) {
			ship.restoreShieldPoints();
		}
	}
	
	public boolean isInBattle() {
		return inBattle;
	}
}
