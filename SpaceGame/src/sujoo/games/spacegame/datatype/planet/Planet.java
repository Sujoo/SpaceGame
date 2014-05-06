package sujoo.games.spacegame.datatype.planet;

public abstract class Planet {
	private PlanetType type;
	
	public Planet(PlanetType type) {
		this.type = type;
	}
	
	public PlanetType getType() {
		return type;
	}
}
