package sujoo.games.spacegame.datatypes.player;

import sujoo.games.spacegame.datatypes.ship.Ship;

public abstract class Player {
	private Ship ship;
	private int credits;
	
	public Player(Ship ship, int credits) {
		this.ship = ship;
		this.credits = credits;
	}
	
	public Ship getShip() {
		return ship;
	}
	
	public int getCredits() {
		return credits;
	}
	
	public boolean removeCredits(int amount) {
		if (credits >= amount) {
			credits -= amount;
			return true;
		} else {
			return false;
		}
	}
	
	public void addCredits(int amount) {
		credits += amount;
	}
}
