package sujoo.games.spacegame.datatypes;

public enum CargoEnum {
	FUEL("fuel", 1, 50),
	//PARTS("parts", 1, 50),
    //ORGANICS("organics", 1, 50),
    //WEAPONS("weapons", 1, 75),
    UNKNOWN("unknown", 0, 0);

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
    	CargoEnum result = UNKNOWN;
        for (CargoEnum cargoEnum : CargoEnum.values()) {
            if (cargoEnum.getCode().equalsIgnoreCase(code)) {
                result = cargoEnum;
            }
        }
        return result;
    }

    /**
     * Does the entered string match a cargoEnum?
     * @param pos
     * @return
     */
    public static boolean isCargoEnum(String cargoEnum) {
        return isCargoEnum(toCargoEnum(cargoEnum));
    }

    private static boolean isCargoEnum(CargoEnum cargoEnum) {
        if (cargoEnum != UNKNOWN) {
            return true;
        } else {
            return false;
        }
    }
    
    public static int getCargoEnumIndex(CargoEnum cargoEnum) {
    	int result = CargoEnum.values().length-1;
    	for (int i = 0; i < CargoEnum.values().length - 1; i++) {
    		if (CargoEnum.values()[i] == cargoEnum) {
    			result =  i;
    			break;
    		}
    	}
    	return result;
    }
}
