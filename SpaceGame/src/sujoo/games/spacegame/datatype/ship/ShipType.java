package sujoo.games.spacegame.datatype.ship;

import java.util.List;

import com.google.common.collect.Lists;

public enum ShipType {
    SMALL_TRANS("Small Transport", 100, new ShipComponentEnumIntf[] { ShipComponentEnum.BASE_HULL, ShipComponentEnum.BASE_WEAPON,
            ShipComponentEnum.BASE_ENGINE, ShipShieldComponentEnum.BASE_SHIELD }),
    STATION("Station", 50000, new ShipComponentEnumIntf[] { ShipComponentEnum.BASE_HULL, ShipComponentEnum.BASE_WEAPON,
            ShipComponentEnum.BASE_ENGINE, ShipShieldComponentEnum.BASE_SHIELD });

    private String desc;
    private int holdSize;
    private ShipComponentEnumIntf[] components;

    private ShipType(String desc, int holdSize, ShipComponentEnumIntf[] components) {
        this.desc = desc;
        this.holdSize = holdSize;
        this.components = components;
    }

    public String getDesc() {
        return desc;
    }

    public int getHoldSize() {
        return holdSize;
    }
    
    public List<ShipComponentEnumIntf> getComponents() {
        return Lists.newArrayList(components);
    }
}
