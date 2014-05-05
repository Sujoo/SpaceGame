package sujoo.games.spacegame.datatypes.player;

import sujoo.games.spacegame.datatypes.Star;
import sujoo.games.spacegame.datatypes.Wallet;
import sujoo.games.spacegame.datatypes.ship.Ship;

public class Player {
	private Ship ship;
	private Wallet wallet;
	private Star currentStar;
	private Star previousStar;
	
	public Player(Ship ship, int credits) {
		this.ship = ship;
		wallet = new Wallet(credits);
		currentStar = null;
		previousStar = null;
	}
	
	public Ship getShip() {
		return ship;
	}
	
	public Wallet getWallet() {
		return wallet;
	}
	
	public Star getCurrentStar() {
		return currentStar;
	}
	
	public Star getPreviousStar() {
		return previousStar;
	}
	
	public void setNewCurrentStar(Star newCurrentStar) {
		previousStar = currentStar;
		currentStar = newCurrentStar;
	}
}
