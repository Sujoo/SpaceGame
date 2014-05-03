package sujoo.games.spacegame.datatypes.planet;

public class ClassHPlanet extends Planet {
	
	public ClassHPlanet() {
		super(PlanetType.CLASS_H);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
