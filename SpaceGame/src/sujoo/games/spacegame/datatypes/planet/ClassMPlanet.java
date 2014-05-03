package sujoo.games.spacegame.datatypes.planet;


public class ClassMPlanet extends Planet {
	
	public ClassMPlanet() {
		super(PlanetType.CLASS_M);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
