package sujoo.games.spacegame.datatype.cargo;

import java.util.List;

import com.google.common.collect.Lists;

import sujoo.games.spacegame.datatype.ship.component.ShipComponent;


public class CargoHold {
	private int size;
	private int[] cargo;
	private int[] transactionPrice;
	private int[] recentPurchasedPrice;
	private ShipComponent[] componentHold;
	
	public CargoHold(int size) {
		this.size = size;
		cargo = new int[CargoEnum.values().length];
		transactionPrice = new int[CargoEnum.values().length];
		recentPurchasedPrice = new int[CargoEnum.values().length];
		componentHold = new ShipComponent[4];
	}
	
	public boolean doesComponentExist(String componentName) {
	    boolean result = false;
	    for (int i = 0; i < componentHold.length; i++) {
	        if (componentHold[i] != null && componentHold[i].getName().equalsIgnoreCase(componentName)) {
	            result = true;
	            break;
	        }
	    }
	    return result;
	}
	
	public ShipComponent getComponentExist(String componentName) {
	    ShipComponent result = null;
        for (int i = 0; i < componentHold.length; i++) {
            if (componentHold[i].getName().equalsIgnoreCase(componentName)) {
                result = componentHold[i];
                break;
            }
        }
        return result;
    }
	
	public boolean canAddComponent() {
	    boolean result = false;
	    for (int i = 0; i < componentHold.length; i++) {
	        if (componentHold[i] != null) {
	            result = true;
	            break;
	        }
	    }
	    return result;
	}
	
	public int getNextOpenComponentIndex() {
	    int result = -1;
	    for (int i = 0; i < componentHold.length; i++) {
            if (componentHold[i] == null) {
                result = i;
                break;
            }
        }
	    return result;
	}
	
	public void addComponent(ShipComponent component) {
	    int index = getNextOpenComponentIndex();
	    if (index != -1) {
	        componentHold[index] = component;
	    }
	}
	
	public void removeComponent(ShipComponent component) {
	    for (int i = 0; i < componentHold.length; i++) {
            if (componentHold[i] != null && componentHold[i].equals(component)) {
                componentHold[i] = null;
                break;
            }
        }
	}
	
	public List<ShipComponent> getComponentHoldList() {
	    List<ShipComponent> result = Lists.newArrayList();
	    for (int i = 0; i < componentHold.length; i++) {
            if (componentHold[i] != null) {
                result.add(componentHold[i]);
            }
        }
	    return result;
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
		if (canAddCargo(cargoEnum, amount)) {
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
