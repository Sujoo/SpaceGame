package sujoo.games.spacegame.datatypes.player;

import sujoo.games.spacegame.datatypes.Wallet;
import sujoo.games.spacegame.datatypes.ship.Ship;

public abstract class Player {
	private Ship ship;
	private Wallet wallet;
	
	public Player(Ship ship, int credits) {
		this.ship = ship;
		wallet = new Wallet(credits);
	}
	
	public Ship getShip() {
		return ship;
	}
	
	public Wallet getWallet() {
		return wallet;
	}
}
