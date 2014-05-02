package sujoo.games.spacegame.datatypes;

public class Station {
	
	private final int defaultHoldSize = 5000;
	private CargoHold hold;
	
	public Station() {
		hold = new CargoHold(defaultHoldSize);
	}
	
	public Station(int cargoHoldSize) {
		hold = new CargoHold(cargoHoldSize);
	}
	
	public CargoHold getCargoHold() {
		return hold;
	}
}
