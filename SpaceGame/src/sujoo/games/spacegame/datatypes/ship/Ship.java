package sujoo.games.spacegame.datatypes.ship;

import sujoo.games.spacegame.datatypes.CargoHold;

public abstract class Ship {
	private ShipType type;
	private CargoHold hold;
	private int currentShieldPoints;
	private int currentHullPoints;
	
	public Ship(ShipType type) {
		this.type = type;
		hold = new CargoHold(type.getHoldSize());
		currentShieldPoints = type.getShieldPoints();
		currentHullPoints = type.getHullPoints();
	}

	public ShipType getType() {
		return type;
	}
	
	/**
	 * 
	 * @param damage
	 * @return true if the damage is taken and does not destroy the ship, false if the damage will destroy the ship
	 */
	public boolean takeDamage(int damage) {
		if (hasEnoughHitPoints(damage)) {
			if (currentShieldPoints >= damage) {
				currentShieldPoints -= damage;
			} else {
				int damageToHull = damage - currentShieldPoints;
				currentShieldPoints = 0;
				currentHullPoints -= damageToHull;
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean hasEnoughHitPoints(int damage) {
		if (currentShieldPoints + currentHullPoints > damage) {
			return true;
		} else {
			return false;
		}
	}
	
	public CargoHold getCargoHold() {
		return hold;
	}
	
	public int getCurrentShieldPoints() {
		return currentShieldPoints;
	}
	
	public int getMaxShieldPoints() {
		return type.getShieldPoints();
	}
	
	public int getCurrentHullPoints() {
		return currentHullPoints;
	}

	public int getMaxHullPoints() {
		return type.getHullPoints();
	}

	public int getWeaponAttack() {
		return type.getWeaponAttack();
	}

	public int getEnginePower() {
		return type.getEnginePower();
	}
}
