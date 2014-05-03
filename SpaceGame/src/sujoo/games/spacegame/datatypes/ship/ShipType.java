package sujoo.games.spacegame.datatypes.ship;

public enum ShipType {
	SMALL_TRANS("Small Transport", 500);
	
	private String desc;
	private int holdSize;
	private ShipType(String desc, int holdSize) {
		this.desc = desc;
		this.holdSize = holdSize;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public int getHoldSize() {
		return holdSize;
	}
}
