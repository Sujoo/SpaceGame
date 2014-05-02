package sujoo.games.spacegame;

import sujoo.games.spacegame.datatypes.Star;
import sujoo.games.spacegame.datatypes.planet.Planet;

public class TextUtil {
	private static final String nl = System.lineSeparator();
	private static final String starTag = "System : ";
	private static final String connectionsTag = "Exits  : ";
	private static final String planetsTag = "Planets: ";
	
	private static final String helpString = "List of Commands:" + nl +
			"Jump #### - Jump to adjacent star system #" + nl +
			"Scan - Display star system information" + nl +
			"Map - Display adject system map";
	
	public static String getCurrentStarSystemString(Star star, String connectionString) {
		return starTag + star.getId() + nl + connectionsTag + connectionString + nl + planetsTag + getStarPlanetString(star);
	}
	
	private static String getStarPlanetString(Star star) {
		String result = "(" + star.getPlanets().size() + ") : ";
		
		for (Planet planet : star.getPlanets()) {
			result += planet + ", ";
		}
		
		return result.substring(0, result.length()-2);
	}
	
	public static String getHelpString() {
		return helpString;
	}
	
	
}
