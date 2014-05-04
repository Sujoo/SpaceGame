package sujoo.games.spacegame.datatypes;

public class Station {
	
	private final int defaultHoldSize = 5000;
	private CargoHold hold;
	private int[] prices;
	private boolean[] isSelling;
	
	public Station() {
		hold = new CargoHold(defaultHoldSize);
		prices = new int[CargoEnum.values().length - 1];
		isSelling = new boolean[prices.length];
	}
	
	public int[] getPrices() {
		return prices;
	}
	
	public boolean[] getIsSelling() {
		return isSelling;
	}
	
	public Station(int cargoHoldSize) {
		hold = new CargoHold(cargoHoldSize);
	}
	
	public CargoHold getCargoHold() {
		return hold;
	}
}
