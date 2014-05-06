package sujoo.games.spacegame.datatype.planet;

public class ClassVPlanet extends Planet {
	
	public ClassVPlanet() {
		super(PlanetType.CLASS_V);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
