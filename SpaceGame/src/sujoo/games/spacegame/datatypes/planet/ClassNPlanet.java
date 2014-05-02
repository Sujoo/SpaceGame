package sujoo.games.spacegame.datatypes.planet;


public class ClassNPlanet extends Planet {
	
	public ClassNPlanet() {
		super(PlanetType.CLASS_N);
	}

	public String toString() {
		return shortDesc();
	}
	
	public String shortDesc() {
		return "N";
	}
	
	public String longDesc() {
		return "Sulfuric";
	}
}
