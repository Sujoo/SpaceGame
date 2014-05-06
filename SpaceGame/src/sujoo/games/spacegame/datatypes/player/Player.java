package sujoo.games.spacegame.datatypes.player;

import sujoo.games.spacegame.datatypes.CargoEnum;
import sujoo.games.spacegame.datatypes.Star;
import sujoo.games.spacegame.datatypes.Wallet;
import sujoo.games.spacegame.datatypes.ship.Ship;

public abstract class Player implements Comparable<Player> {
	private final String name;
	private Ship ship;
	private Wallet wallet;
	private Star currentStar;
	private Star previousStar;
	
	public Player(Ship ship, int credits, String name) {
		this.name = name;
		this.ship = ship;
		wallet = new Wallet(credits);
		currentStar = null;
		previousStar = null;
	}
	
	public int getScore() {
		return wallet.getCredits() + ship.getCargoHold().getTotalWorth();
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
	
	public int getPurchasePrice(CargoEnum cargoEnum) {
		return ship.getCargoHold().getRecentPurchasePrice(cargoEnum);
	}
	
	public void setPurchasePrice(int price, CargoEnum cargoEnum) {
		ship.getCargoHold().setRecentPurchasePrice(price, cargoEnum);
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Player other) {
		return Integer.compare(this.getScore(), other.getScore());
	}
}
