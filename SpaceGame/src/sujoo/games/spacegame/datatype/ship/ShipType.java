package sujoo.games.spacegame.datatype.ship;

public enum ShipType {
	SMALL_TRANS("Small Transport", 100, 50, 100, 25, 20),
	STATION("Station", 50000, 1000, 5000, 50, 0);
	
	private String desc;
	private int holdSize;
	private int shieldPoints;
	private int hullPoints;
	private int weaponAttack;
	private int enginePower;
	private ShipType(String desc, int holdSize, int shieldPoints, int hullPoints, int weaponAttack, int enginePower) {
		this.desc = desc;
		this.holdSize = holdSize;
		this.shieldPoints = shieldPoints;
		this.hullPoints = hullPoints;
		this.weaponAttack = weaponAttack;
		this.enginePower = enginePower;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public int getHoldSize() {
		return holdSize;
	}

	public int getShieldPoints() {
		return shieldPoints;
	}

	public int getHullPoints() {
		return hullPoints;
	}

	public int getWeaponAttack() {
		return weaponAttack;
	}

	public int getEnginePower() {
		return enginePower;
	}
}
