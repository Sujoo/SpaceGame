package sujoo.games.spacegame.datatype.cargo;


public class CargoHold {
	private int size;
	private int[] cargo;
	private int[] transactionPrice;
	private int[] recentPurchasedPrice;
	
	public CargoHold(int size) {
		this.size = size;
		cargo = new int[CargoEnum.values().length];
		transactionPrice = new int[CargoEnum.values().length];
		recentPurchasedPrice = new int[CargoEnum.values().length];
	}
	
	public void removeCargo(CargoEnum cargoEnum, int amount) {
		if (getCargoAmount(cargoEnum) - amount >= 0) {
			cargo[CargoEnum.getCargoEnumIndex(cargoEnum)] -= amount;
		}
	}
	
	public boolean canRemoveCargo(CargoEnum cargoEnum, int amount) {
		if (cargo[CargoEnum.getCargoEnumIndex(cargoEnum)] - amount >= 0) {
			return true;
		}
		return false;
	}

	public void addCargo(CargoEnum cargoEnum, int amount) {
		if (getCargoSpaceUsage() + amount * cargoEnum.getSize() <= size) {
			cargo[CargoEnum.getCargoEnumIndex(cargoEnum)] += amount;
		}
	}
	
	public boolean canAddCargo(CargoEnum cargoEnum, int amount) {
		if (getCargoSpaceUsage() + amount * cargoEnum.getSize() <= size) {
			return true;
		}
		return false;
	}
	
	public int getRemainingCargoSpace() {
		return size - getCargoSpaceUsage();
	}
	
	public int getCargoSpaceUsage() {
		int result = 0;
		for (int i = 0; i < cargo.length; i++) {
			result += cargo[i] * CargoEnum.values()[i].getSize();
		}
		return result;
	}
	
	public int getTotalWorth() {
		int totalWorth = 0;
		for (int i = 0; i < cargo.length; i++) {
			totalWorth += cargo[i] * CargoEnum.values()[i].getBaseValue();
		}
		return totalWorth;
	}
	
	public void dumpAllCargo() {
		for (int i = 0; i < cargo.length; i++) {
			cargo[i] = 0;
		}
	}
	
	public void dumpCargo(CargoEnum cargoEnum) {
		cargo[CargoEnum.getCargoEnumIndex(cargoEnum)] = 0;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getCargoAmount(CargoEnum cargoEnum) {
		return cargo[CargoEnum.getCargoEnumIndex(cargoEnum)];
	}
	
	public int getTransactionPrice(CargoEnum cargoEnum) {
		return transactionPrice[CargoEnum.getCargoEnumIndex(cargoEnum)];
	}
	
	public void setTransactionPrice(int price, CargoEnum cargoEnum) {
		transactionPrice[CargoEnum.getCargoEnumIndex(cargoEnum)] = price;
	}
	
	public int getRecentPurchasePrice(CargoEnum cargoEnum) {
		return recentPurchasedPrice[CargoEnum.getCargoEnumIndex(cargoEnum)];
	}
	
	public void setRecentPurchasePrice(int price, CargoEnum cargoEnum) {
		recentPurchasedPrice[CargoEnum.getCargoEnumIndex(cargoEnum)] = price;
	}
}
