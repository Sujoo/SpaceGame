package sujoo.games.spacegame.datatypes.planet;

public class ClassTPlanet extends Planet {
	
	public ClassTPlanet() {
		super(PlanetType.CLASS_T);
	}

	public String toString() {
		return shortDesc();
	}
	
	public String shortDesc() {
		return "T";
	}
	
	public String longDesc() {
		return "Super Gas Giant";
	}
}
