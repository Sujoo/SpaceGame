package sujoo.games.spacegame.datatype.ship;

import java.util.List;

import sujoo.games.spacegame.datatype.ship.component.ShipComponentEnum;
import sujoo.games.spacegame.datatype.ship.component.ShipComponentEnumIntf;
import sujoo.games.spacegame.datatype.ship.component.ShipShieldComponentEnum;

import com.google.common.collect.Lists;

public enum ShipType {
    SMALL_TRANS("Small Transport", 100, new ShipComponentEnumIntf[] { ShipShieldComponentEnum.BASE_SHIELD, ShipComponentEnum.BASE_HULL,
            ShipComponentEnum.BASE_WEAPON, ShipComponentEnum.BASE_ENGINE }),
    STATION("Station", 50000, new ShipComponentEnumIntf[] { ShipShieldComponentEnum.BASE_SHIELD, ShipComponentEnum.BASE_HULL,
            ShipComponentEnum.BASE_WEAPON, ShipComponentEnum.BASE_ENGINE });

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
