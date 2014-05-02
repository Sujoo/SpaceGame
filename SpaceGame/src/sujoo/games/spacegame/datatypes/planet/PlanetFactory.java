package sujoo.games.spacegame.datatypes.planet;

public class PlanetFactory {
	public static Planet buildPlanet(PlanetType type) {
		Planet planet = null;
		switch(type) {
		case CLASS_D:
			planet = new ClassDPlanet();
			break;
		case CLASS_H:
			planet = new ClassHPlanet();
			break;
		case CLASS_J:
			planet = new ClassJPlanet();
			break;
		case CLASS_K:
			planet = new ClassKPlanet();
			break;
		case CLASS_L:
			planet = new ClassLPlanet();
			break;
		case CLASS_M:
			planet = new ClassMPlanet();
			break;
		case CLASS_N:
			planet = new ClassNPlanet();
			break;
		case CLASS_T:
			planet = new ClassTPlanet();
			break;
		}
		return planet;
	}

}
