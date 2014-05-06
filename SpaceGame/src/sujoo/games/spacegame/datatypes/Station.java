package sujoo.games.spacegame.datatypes;

import java.util.Set;

import com.google.common.collect.Sets;

public class Station {
	
	private final int defaultHoldSize = 15000;
	private final int defaultCredits = 50000;
	private CargoHold hold;
	private Wallet wallet;
	private Set<CargoEnum> cargoGeneratedByPlanet;
	
	public Station() {
		hold = new CargoHold(defaultHoldSize);
		wallet = new Wallet(defaultCredits);
		cargoGeneratedByPlanet = Sets.newHashSet();
	}
	
	public void setPrice(int price, CargoEnum cargoEnum) {
		hold.setTransactionPrice(price, cargoEnum);
	}
	
	public int getPrice(CargoEnum cargoEnum) {
		return hold.getTransactionPrice(cargoEnum);
	}
	
	public CargoHold getCargoHold() {
		return hold;
	}
	
	public Wallet getWallet() {
		return wallet;
	}
	
	public void addCargoGeneratedByPlanet(CargoEnum cargoEnum) {
		cargoGeneratedByPlanet.add(cargoEnum);
	}
	
	public boolean isCargoGeneratedByPlanet(CargoEnum cargoEnum) {
		return cargoGeneratedByPlanet.contains(cargoEnum);
	}
}
