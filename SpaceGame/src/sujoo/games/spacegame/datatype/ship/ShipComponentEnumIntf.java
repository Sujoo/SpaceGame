package sujoo.games.spacegame.datatype.ship;

import sujoo.games.spacegame.datatype.command.AttackSubCommand;

public interface ShipComponentEnumIntf {
    public int getAbsoluteMaxValue();

    public int getRepairFraction();

    public int getToughness();

    public AttackSubCommand getLocation();
}
