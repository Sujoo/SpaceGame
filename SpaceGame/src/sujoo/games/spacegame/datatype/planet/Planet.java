package sujoo.games.spacegame.datatype.planet;

public abstract class Planet {
	private PlanetEnum type;
	
	public Planet(PlanetEnum type) {
		this.type = type;
	}
	
	public PlanetEnum getType() {
		return type;
	}
	
	@Override
	public String toString() {
	    return getType().getShortDesc();
	}
}
