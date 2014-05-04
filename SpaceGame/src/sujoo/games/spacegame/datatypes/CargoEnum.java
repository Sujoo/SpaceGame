package sujoo.games.spacegame.datatypes;

public enum CargoEnum {
	ELECTRONICS("Electronics", 1, 150),
	ORGANICS("Organics", 1, 125),
	PARTS("Parts", 1, 100),
	ALLOY("Alloy", 1, 100),
	AMMO("Ammo", 1, 25),
	FUEL("Fuel", 1, 25);

	private String code;
	private int size;
	private int baseValue;
    private CargoEnum(String code, int size, int modifierValue) {
        this.code = code;
        this.size = size;
        this.baseValue = modifierValue;
    }
    
    private String getCode() {
        return code;
    }
    
    public int getSize() {
    	return size;
    }
    
    public int getBaseValue() {
    	return baseValue;
    }
    
    public String toString() {
    	return code;
    }

    /**
     * Attempts to match an input String with a cargoEnum
     * If no match is found, the Unknown type is returned
     * 
     * @param code
     * @return
     */
    public static CargoEnum toCargoEnum(String code) {
    	CargoEnum result = null;
        for (CargoEnum cargoEnum : CargoEnum.values()) {
            if (cargoEnum.getCode().equalsIgnoreCase(code)) {
                result = cargoEnum;
            }
        }
        return result;
    }
    
    public static int getCargoEnumIndex(CargoEnum cargoEnum) {
    	int result = -1;
    	for (int i = 0; i < CargoEnum.values().length; i++) {
    		if (CargoEnum.values()[i] == cargoEnum) {
    			result =  i;
    			break;
    		}
    	}
    	return result;
    }
}
