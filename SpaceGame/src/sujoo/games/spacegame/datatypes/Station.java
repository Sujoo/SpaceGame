package sujoo.games.spacegame.datatypes;

public class Station {
	
	private final int defaultHoldSize = 15000;
	private final int defaultCredits = 10000;
	private CargoHold hold;
	private int[] prices;
	private Wallet wallet;
	
	public Station() {
		hold = new CargoHold(defaultHoldSize);
		prices = new int[hold.getCargo().length];
		wallet = new Wallet(defaultCredits);
	}
	
	public int[] getPrices() {
		return prices;
	}
	
	public CargoHold getCargoHold() {
		return hold;
	}
	
	public Wallet getWallet() {
		return wallet;
	}
}
