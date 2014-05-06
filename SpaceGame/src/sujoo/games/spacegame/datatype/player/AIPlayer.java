package sujoo.games.spacegame.datatype.player;

import java.util.Set;

import com.google.common.collect.Sets;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.ship.Ship;

public class AIPlayer extends Player {
	private Set<CargoEnum> recentlyPurchased;
	
	public AIPlayer(Ship ship, int credits, String name) {
		super(ship, credits, name);
		recentlyPurchased = Sets.newHashSet();
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
}
