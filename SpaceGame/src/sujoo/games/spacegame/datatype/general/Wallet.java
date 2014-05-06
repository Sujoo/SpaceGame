package sujoo.games.spacegame.datatype.general;

public class Wallet {
	private int credits;
	
	public Wallet(int credits) {
		this.credits = credits;
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
