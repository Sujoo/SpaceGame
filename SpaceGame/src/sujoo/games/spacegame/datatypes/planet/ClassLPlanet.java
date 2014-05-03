package sujoo.games.spacegame.datatypes.planet;

public class ClassLPlanet extends Planet {
	
	public ClassLPlanet() {
		super(PlanetType.CLASS_L);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
