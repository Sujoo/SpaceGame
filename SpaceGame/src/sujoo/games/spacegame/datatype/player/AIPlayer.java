package sujoo.games.spacegame.datatype.player;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.general.Star;
import sujoo.games.spacegame.datatype.ship.Ship;

public class AIPlayer extends Player {
    private Set<CargoEnum> recentlyPurchased;
    private int numberOfJumps;
    private Map<CargoEnum, Star> buyCargoStationMap;
    private Map<CargoEnum, Star> sellCargoStationMap;

    public AIPlayer(Ship ship, int credits, String name) {
        super(ship, credits, name);
        recentlyPurchased = Sets.newHashSet();
        numberOfJumps = 0;
        buyCargoStationMap = Maps.newHashMap();
        sellCargoStationMap = Maps.newHashMap();
    }

    public int getBestBuyPrice(CargoEnum cargoEnum) {
        return buyCargoStationMap.get(cargoEnum).getStation().getTransactionPrice(cargoEnum);
    }

    public int getBestSellPrice(CargoEnum cargoEnum) {
        return sellCargoStationMap.get(cargoEnum).getStation().getTransactionPrice(cargoEnum);
    }

    public int getNumberOfJumps() {
        return numberOfJumps;
    }

    public boolean isRecentlyPurchased(CargoEnum cargoEnum) {
        return recentlyPurchased.contains(cargoEnum);
    }

    public void addRecentlyPurchased(CargoEnum cargoEnum) {
        recentlyPurchased.add(cargoEnum);
    }

    public void clearRecentlyPurchased() {
        recentlyPurchased.clear();
    }

    public void resetStationLogic() {

    }

    public void updateCargoPrice(CargoEnum cargoEnum, Star star) {
        if (buyCargoStationMap.containsKey(cargoEnum)) {
            if (getBestBuyPrice(cargoEnum) >= star.getStation().getTransactionPrice(cargoEnum)) {
                buyCargoStationMap.put(cargoEnum, star);
            }
        } else {
            buyCargoStationMap.put(cargoEnum, star);
        }

        if (sellCargoStationMap.containsKey(cargoEnum)) {
            if (sellCargoStationMap.get(cargoEnum).getStation().getTransactionPrice(cargoEnum) <= star.getStation()
                    .getTransactionPrice(cargoEnum)) {
                sellCargoStationMap.put(cargoEnum, star);
            }
        } else {
            sellCargoStationMap.put(cargoEnum, star);
        }
    }

    public boolean isKnownBuyPriceLessThanSellPrice(CargoEnum cargoEnum) {
        int bestBuyPrice = buyCargoStationMap.get(cargoEnum).getStation().getTransactionPrice(cargoEnum);
        int bestSellPrice = sellCargoStationMap.get(cargoEnum).getStation().getTransactionPrice(cargoEnum);

        if (bestBuyPrice < bestSellPrice) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBestSellPrice(CargoEnum cargoEnum, int price) {
        if (sellCargoStationMap.get(cargoEnum).getStation().getTransactionPrice(cargoEnum) >= price) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBestBuyPrice(CargoEnum cargoEnum, int price) {
        if (buyCargoStationMap.get(cargoEnum).getStation().getTransactionPrice(cargoEnum) <= price) {
            return true;
        } else {
            return false;
        }
    }
}
