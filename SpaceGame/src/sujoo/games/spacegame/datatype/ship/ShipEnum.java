package sujoo.games.spacegame.datatype.ship;

import java.util.List;

import sujoo.games.spacegame.datatype.ship.component.CargoHoldComponentEnum;
import sujoo.games.spacegame.datatype.ship.component.ShipComponentEnum;
import sujoo.games.spacegame.datatype.ship.component.ShipComponentEnumIntf;
import sujoo.games.spacegame.datatype.ship.component.ShieldComponentEnum;

import com.google.common.collect.Lists;

public enum ShipEnum {
    SMALL_TRANS("Small Transport", new ShipComponentEnumIntf[] { ShieldComponentEnum.BASE_SHIELD, ShipComponentEnum.BASE_HULL,
            ShipComponentEnum.BASE_WEAPON, ShipComponentEnum.BASE_ENGINE, CargoHoldComponentEnum.BASE_HOLD }),
    STATION("Station", new ShipComponentEnumIntf[] { ShieldComponentEnum.BASE_SHIELD, ShipComponentEnum.BASE_HULL,
            ShipComponentEnum.BASE_WEAPON, ShipComponentEnum.BASE_ENGINE, CargoHoldComponentEnum.STATION_BASE_HOLD });

    private String desc;
    private ShipComponentEnumIntf[] components;

    private ShipEnum(String desc, ShipComponentEnumIntf[] components) {
        this.desc = desc;
        this.components = components;
    }

    public String getDesc() {
        return desc;
    }

    public List<ShipComponentEnumIntf> getComponents() {
        return Lists.newArrayList(components);
    }
}
