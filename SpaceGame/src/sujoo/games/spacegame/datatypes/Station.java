package sujoo.games.spacegame.datatypes;

public class Station {
	
	private final int defaultHoldSize = 5000;
	private CargoHold hold;
	private int[] buyPrices;
	private int[] sellPrices;
	
	public Station() {
		hold = new CargoHold(defaultHoldSize);
		buyPrices = new int[CargoEnum.values().length - 1];
		sellPrices = new int[CargoEnum.values().length - 1];
		initializePrices();
	}
	
	private void initializePrices() {
		for (int i = 0; i < CargoEnum.values().length - 1; i++) {
			buyPrices[i] = CargoEnum.values()[i].getBaseValue();
			sellPrices[i] = CargoEnum.values()[i].getBaseValue();
		}
	}
	
	public Station(int cargoHoldSize) {
		hold = new CargoHold(cargoHoldSize);
	}
	
	public CargoHold getCargoHold() {
		return hold;
	}
	
	public int[] getBuyPrices() {
		return buyPrices;
	}
	
	public int[] getSellPrices() {
		return sellPrices;
	}
}
