package sujoo.games.spacegame.datatypes.planet;

public class ClassNPlanet extends Planet {
	
	public ClassNPlanet() {
		super(PlanetType.CLASS_N);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
