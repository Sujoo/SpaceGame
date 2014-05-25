package sujoo.games.spacegame.datatype.planet;

public class PlanetFactory {
	public static Planet buildPlanet(PlanetType type) {
		Planet planet = null;
		switch(type) {
		case TERRESTRIAL:
			planet = new TerrestrialPlanet();
			break;
		case GAS:
			planet = new GasPlanet();
			break;
		case VOLCANIC:
			planet = new VolcanicPlanet();
			break;
		case JUNGLE:
			planet = new JunglePlanet();
			break;
		case DESERT:
			planet = new DesertPlanet();
			break;
		case ICE:
			planet = new IcePlanet();
			break;
		case SILICON:
			planet = new SiliconPlanet();
			break;
		}
		return planet;
	}

}
