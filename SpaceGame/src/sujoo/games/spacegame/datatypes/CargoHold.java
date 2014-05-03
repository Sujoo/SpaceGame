package sujoo.games.spacegame.datatypes;

public class CargoHold {
	private int size;
	private int[] cargo;
	
	public CargoHold(int size) {
		this.size = size;
		cargo = new int[CargoEnum.values().length - 1];
	}
	
	public boolean removeCargo(CargoEnum cargoEnum, int amount) {
		boolean result = false;
		for (int i = 0; i < CargoEnum.values().length; i++) {
			if (CargoEnum.values()[i] == cargoEnum) {
				if (cargo[i] - amount >= 0) {
					cargo[i] -= amount;
					result = true;
				}
			}
		}
		return result;
	}

	public boolean addCargo(CargoEnum cargoEnum, int amount) {
		boolean result = false;
		for (int i = 0; i < CargoEnum.values().length; i++) {
			if (CargoEnum.values()[i] == cargoEnum) {
				if (getCargoSpaceUsage() + amount * CargoEnum.values()[i].getSize() <= size) {
					cargo[i] += amount;
					result = true;
				}
			}
		}
		return result;
	}
	
	public int remainingCargoSpace() {
		return size - getCargoSpaceUsage();
	}
	
	public int getCargoSpaceUsage() {
		int result = 0;
		for (int i = 0; i < CargoEnum.values().length - 1; i++) {
			result += cargo[i] * CargoEnum.values()[i].getSize();
		}
		return result;
	}
	
	public int getSize() {
		return size;
	}
	
	public int[] getCargo() {
		return cargo;
	}
}
