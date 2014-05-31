package sujoo.games.spacegame.datatype.planet;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;

public enum PlanetEnum {
    TERRESTRIAL("T", "Terrestrial",
            new CargoEnum[] { CargoEnum.FUEL, CargoEnum.AMMO, CargoEnum.ALLOY, CargoEnum.PARTS, CargoEnum.ELECTRONICS,
                    CargoEnum.ORGANICS },
            new int[] { 500, 500, 250, 250, 125, 125 }),

    GAS("G", "Gas Giant",
            new CargoEnum[] { CargoEnum.FUEL, CargoEnum.AMMO, CargoEnum.ORGANICS },
            new int[] { 750, 750, 200 }),

    VOLCANIC("V", "Volcanic",
            new CargoEnum[] { CargoEnum.FUEL, CargoEnum.PARTS, CargoEnum.ALLOY },
            new int[] { 750, 750, 375 }),

    JUNGLE("J", "Jungle",
            new CargoEnum[] { CargoEnum.FUEL, CargoEnum.AMMO, CargoEnum.ORGANICS },
            new int[] { 750, 750, 375 }),

    DESERT("D", "Desert",
            new CargoEnum[] { CargoEnum.AMMO, CargoEnum.PARTS, CargoEnum.ORGANICS },
            new int[] { 750, 375, 200 }),

    ICE("I", "Ice",
            new CargoEnum[] { CargoEnum.FUEL, CargoEnum.AMMO, CargoEnum.ALLOY },
            new int[] { 750, 750, 375 }),

    SILICON("S", "Silicon",
            new CargoEnum[] { CargoEnum.ALLOY, CargoEnum.PARTS, CargoEnum.ELECTRONICS },
            new int[] { 375, 375, 200 });

    private String shortDesc;
    private String longDesc;
    private CargoEnum[] stationCargoEnums;
    private int[] baseStationCargoProduction;

    private PlanetEnum(String shortDesc, String longDesc, CargoEnum[] stationCargoEnums, int[] baseStationCargoProduction) {
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.stationCargoEnums = stationCargoEnums;
        this.baseStationCargoProduction = baseStationCargoProduction;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public CargoEnum[] getStationCargoEnums() {
        return stationCargoEnums;
    }

    public int[] getBaseStationCargoProduction() {
        return baseStationCargoProduction;
    }
}
