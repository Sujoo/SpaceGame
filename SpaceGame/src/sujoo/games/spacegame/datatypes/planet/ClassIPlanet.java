package sujoo.games.spacegame.datatypes.planet;


public class ClassIPlanet extends Planet {
	
	public ClassIPlanet() {
		super(PlanetType.CLASS_I);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
