package sujoo.games.spacegame.datatype.ship.component;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

public interface ShipComponentEnumIntf {
    public int getAbsoluteMaxValue();

    public double getRepairFraction();

    public double getToughness();

    public ShipLocationCommand getLocation();
    
    public int getPrice();
    
    public CargoEnum[] getMaterials();
    
    public int[] getMaterialPrices();
}
