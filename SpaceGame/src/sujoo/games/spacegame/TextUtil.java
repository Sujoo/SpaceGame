package sujoo.games.spacegame;

import sujoo.games.spacegame.datatypes.CargoEnum;
import sujoo.games.spacegame.datatypes.CargoHold;
import sujoo.games.spacegame.datatypes.Star;
import sujoo.games.spacegame.datatypes.planet.Planet;

public class TextUtil {
	private static final String nl = System.lineSeparator();
	private static final String starTag = "System : ";
	private static final String connectionsTag = "Exits  : ";
	private static final String planetsTag = "Planets: ";
	private static final String stationTag = "Station: ";
	
	private static final String dust = "Dust and Echoes";
	
	private static final String helpString = "List of Commands:" + nl +
			"Jump #### - Jump to adjacent star system #" + nl +
			"Scan - Display star system information" + nl +
			"Map - Display adject system map";
	
	public static String getCurrentStarSystemString(Star star, String connectionString) {
		return starTag + star.getId() + nl +
				connectionsTag + connectionString + nl +
				planetsTag + getStarPlanetString(star) + nl +
				stationTag + getStarStationCargoString(star);
	}
	
	private static String getStarPlanetString(Star star) {
		String result;
		if (star.getPlanets().size() == 0) {
			result = dust;
		} else {
			result = "(" + star.getPlanets().size() + ") : ";
			for (Planet planet : star.getPlanets()) {
				result += planet + ", ";
			}
		}
		
		return result.substring(0, result.length()-2);
	}
	
	private static String getStarStationCargoString(Star star) {
		String result = "";
		
		CargoHold hold = star.getStation().getCargoHold();
		for (int i = 0; i < CargoEnum.values().length - 1; i++) {
			result += CargoEnum.values()[i].toString() + " : " + hold.getCargo()[i] + "  ";
		}
		
		return result;
	}
	
	public static String getHelpString() {
		return helpString;
	}
	
	
}
