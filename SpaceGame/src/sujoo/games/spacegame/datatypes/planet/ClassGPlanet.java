package sujoo.games.spacegame.datatypes.planet;

public class ClassGPlanet extends Planet {
	
	public ClassGPlanet() {
		super(PlanetType.CLASS_G);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
