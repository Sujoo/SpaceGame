package sujoo.games.spacegame.datatypes.planet;

public class ClassTPlanet extends Planet {
	
	public ClassTPlanet() {
		super(PlanetType.CLASS_T);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
