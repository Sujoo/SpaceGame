package sujoo.games.spacegame.datatypes.planet;

public class ClassJPlanet extends Planet {
	
	public ClassJPlanet() {
		super(PlanetType.CLASS_J);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
