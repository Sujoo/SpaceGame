package sujoo.games.spacegame.datatypes.planet;

public class ClassLPlanet extends Planet {
	
	public ClassLPlanet() {
		super(PlanetType.CLASS_L);
	}

	public String toString() {
		return shortDesc();
	}
	
	public String shortDesc() {
		return "L";
	}
	
	public String longDesc() {
		return "Marginally habitable";
	}
}
