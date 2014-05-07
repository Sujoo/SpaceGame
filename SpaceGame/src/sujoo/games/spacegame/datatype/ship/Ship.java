package sujoo.games.spacegame.datatype.ship;

import sujoo.games.spacegame.datatype.cargo.CargoHold;

public abstract class Ship {
	private ShipType type;
	private CargoHold hold;
	private int currentShieldPoints;
	private int currentHullPoints;
	
	private int shieldDamage;
	private int engineDamage;
	private int weaponDamage;
	
	private int restoreShieldPercent;
	private int repairPercent;
	private int shieldToughness;
	private int weaponToughness;
	private int engineToughness;
	
	public Ship(ShipType type) {
		this.type = type;
		hold = new CargoHold(type.getHoldSize());
		currentShieldPoints = type.getShieldPoints();
		currentHullPoints = type.getHullPoints();
		shieldDamage = 0;
		engineDamage = 0;
		weaponDamage = 0;
		restoreShieldPercent = 2;
		repairPercent = 3;
		shieldToughness = 2;
		weaponToughness = 4;
		engineToughness = 3;
	}
	
	public void fullRestore() {
		shieldDamage = 0;
		engineDamage = 0;
		weaponDamage = 0;
		currentShieldPoints = type.getShieldPoints();
		currentHullPoints = type.getHullPoints();
	}
	
	public int getEscapeTurns() {
		return 4;
	}

	public ShipType getType() {
		return type;
	}
	
	public CargoHold getCargoHold() {
		return hold;
	}
	
	public boolean isShieldUp() {
		if (currentShieldPoints > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getCurrentShieldPoints() {
		return currentShieldPoints;
	}
	
	public void removeCurrentShieldPoints(int damage) {
		currentShieldPoints -= damage;
	}
	
	public int getMaxShieldPoints() {
		return type.getShieldPoints() - shieldDamage;
	}
	
	public int getCurrentHullPoints() {
		return currentHullPoints;
	}
	
	public void removeCurrentHullPoints(int damage) {
		currentHullPoints -= damage;
	}
	
	public void repairCurrentHullPoints() {
		int repair = type.getHullPoints() / repairPercent;
		if (currentHullPoints + repair <= getMaxHullPoints()) {
			currentHullPoints += repair;
		} else {
			currentHullPoints = getMaxHullPoints();
		}
	}
	
	public void restoreShieldPoints() {
		int restoreAmount = getMaxShieldPoints() / restoreShieldPercent;
		if (currentShieldPoints + restoreAmount <= getMaxShieldPoints()) {
			currentShieldPoints += restoreAmount;
		} else {
			currentShieldPoints = getMaxShieldPoints();
		}
	}

	public int getMaxHullPoints() {
		return type.getHullPoints();
	}

	public int getWeaponAttack() {
		return type.getWeaponAttack() - weaponDamage;
	}

	public int getEnginePower() {
		return type.getEnginePower() - engineDamage;
	}
	
	public int getShieldRechargeTime() {
		return type.getShieldRechargeTime();
	}
	
	public void repairShieldDamage() {
		int repair = type.getShieldPoints() / repairPercent;
		if (shieldDamage - repair >= 0) {
			shieldDamage -= repair;
		} else {
			shieldDamage = 0;
		}
	}
	
	public void repairWeaponDamage() {
		int repair = type.getWeaponAttack() / repairPercent;
		if (weaponDamage - repair >= 0) {
			weaponDamage -= repair;
		} else {
			weaponDamage = 0;
		}
	}
	
	public void repairEngineDamage() {
		int repair = type.getEnginePower() / repairPercent;
		if (engineDamage - repair >= 0) {
			engineDamage -= repair;
		} else {
			engineDamage = 0;
		}
	}
	
	public void damageShields(int damage) {
		damage = damage / shieldToughness;
		if (shieldDamage + damage <= type.getShieldPoints()) {
			shieldDamage += damage;
		} else {
			shieldDamage = type.getShieldPoints();
		}
	}
	
	public void damageWeapon(int damage) {
		damage = damage / weaponToughness;
		if (weaponDamage + damage <= type.getWeaponAttack()) {
			weaponDamage += damage;
		} else {
			weaponDamage = type.getWeaponAttack();
		}
	}
	
	public void damageEngine(int damage) {
		damage = damage / engineToughness;
		if (engineDamage + damage <= type.getEnginePower()) {
			engineDamage += damage;
		} else {
			engineDamage = type.getEnginePower();
		}
	}
}
