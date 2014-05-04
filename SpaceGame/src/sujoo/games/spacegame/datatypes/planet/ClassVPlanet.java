package sujoo.games.spacegame.datatypes.planet;

public class ClassVPlanet extends Planet {
	
	public ClassVPlanet() {
		super(PlanetType.CLASS_V);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
