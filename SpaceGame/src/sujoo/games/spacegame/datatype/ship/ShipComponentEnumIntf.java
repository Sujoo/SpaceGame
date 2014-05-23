package sujoo.games.spacegame.datatype.ship;

import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

public interface ShipComponentEnumIntf {
    public int getAbsoluteMaxValue();

    public int getRepairFraction();

    public int getToughness();

    public ShipLocationCommand getLocation();
}
