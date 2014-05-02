package sujoo.games.spacegame.datatypes.planet;


public abstract class Planet {
	private PlanetType type;
	
	public Planet(PlanetType type) {
		this.type = type;
	}
	
	public PlanetType getType() {
		return type;
	}
}
