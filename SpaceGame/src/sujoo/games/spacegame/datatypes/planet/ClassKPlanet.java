package sujoo.games.spacegame.datatypes.planet;

public class ClassKPlanet extends Planet {
	
	public ClassKPlanet() {
		super(PlanetType.CLASS_K);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
