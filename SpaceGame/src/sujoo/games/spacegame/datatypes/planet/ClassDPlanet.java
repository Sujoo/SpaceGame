package sujoo.games.spacegame.datatypes.planet;

public class ClassDPlanet extends Planet {
	
	public ClassDPlanet() {
		super(PlanetType.CLASS_D);
	}

	public String toString() {
		return getType().getShortDesc();
	}
}
