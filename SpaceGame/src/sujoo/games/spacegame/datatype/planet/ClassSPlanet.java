package sujoo.games.spacegame.datatype.planet;

public class ClassSPlanet extends Planet {
	
	public ClassSPlanet() {
		super(PlanetType.CLASS_S);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
