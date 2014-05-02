package sujoo.games.spacegame.datatypes.planet;

public class ClassKPlanet extends Planet {
	
	public ClassKPlanet() {
		super(PlanetType.CLASS_K);
	}

	public String toString() {
		return shortDesc();
	}
	
	public String shortDesc() {
		return "K";
	}
	
	public String longDesc() {
		return "Adaptable with pressure domes";
	}
}
