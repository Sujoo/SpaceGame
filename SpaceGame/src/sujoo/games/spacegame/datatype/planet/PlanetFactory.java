package sujoo.games.spacegame.datatype.planet;

public class PlanetFactory {
	public static Planet buildPlanet(PlanetType type) {
		Planet planet = null;
		switch(type) {
		case CLASS_T:
			planet = new ClassTPlanet();
			break;
		case CLASS_G:
			planet = new ClassGPlanet();
			break;
		case CLASS_V:
			planet = new ClassVPlanet();
			break;
		case CLASS_J:
			planet = new ClassJPlanet();
			break;
		case CLASS_D:
			planet = new ClassDPlanet();
			break;
		case CLASS_I:
			planet = new ClassIPlanet();
			break;
		case CLASS_S:
			planet = new ClassSPlanet();
			break;
		}
		return planet;
	}

}
