package sujoo.games.spacegame.datatypes;

public class CargoHold {
	private int size;
	private int[] cargo;
	
	public CargoHold(int size) {
		this.size = size;
		cargo = new int[CargoEnum.values().length];
	}
	
	public void removeCargo(CargoEnum cargoEnum, int amount) {
		int index = CargoEnum.getCargoEnumIndex(cargoEnum);
		if (cargo[index] - amount >= 0) {
			cargo[index] -= amount;
		}
	}
	
	public boolean canRemoveCargo(CargoEnum cargoEnum, int amount) {
		if (cargo[CargoEnum.getCargoEnumIndex(cargoEnum)] - amount >= 0) {
			return true;
		}
		return false;
	}

	public void addCargo(CargoEnum cargoEnum, int amount) {
		int index = CargoEnum.getCargoEnumIndex(cargoEnum);
		if (getCargoSpaceUsage() + amount * CargoEnum.values()[index].getSize() <= size) {
			cargo[index] += amount;
		}
	}
	
	public boolean canAddCargo(CargoEnum cargoEnum, int amount) {
		if (getCargoSpaceUsage() + amount * CargoEnum.values()[CargoEnum.getCargoEnumIndex(cargoEnum)].getSize() <= size) {
			return true;
		}
		return false;
	}
	
	public int remainingCargoSpace() {
		return size - getCargoSpaceUsage();
	}
	
	public int getCargoSpaceUsage() {
		int result = 0;
		for (int i = 0; i < CargoEnum.values().length; i++) {
			result += cargo[i] * CargoEnum.values()[i].getSize();
		}
		return result;
	}
	
	public int[] getTotalValues() {
		int[] totalValues = new int[cargo.length];
		for (int i = 0; i < cargo.length; i++) {
			totalValues[i] = cargo[i] * CargoEnum.values()[i].getBaseValue();
		}
		return totalValues;
	}
	
	public int getSize() {
		return size;
	}
	
	public int[] getCargo() {
		return cargo;
	}
}
