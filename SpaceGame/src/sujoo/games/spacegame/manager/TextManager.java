package sujoo.games.spacegame.manager;

import java.util.List;

import sujoo.games.spacegame.datatypes.CargoEnum;
import sujoo.games.spacegame.datatypes.CargoHold;
import sujoo.games.spacegame.datatypes.Star;
import sujoo.games.spacegame.datatypes.Station;
import sujoo.games.spacegame.datatypes.planet.Planet;
import sujoo.games.spacegame.datatypes.player.Player;

public class TextManager {
	private static final String nl = System.lineSeparator();
	private static final String starTag = "System : ";
	private static final String connectionsTag = "Exits  : ";
	private static final String planetsTag = "Planets: ";
	private static final String playersTag = "Players: ";
	private static final String creditsTag = "Credits: ";
	
	private static final String dust = "Dust and Echoes";
	
	private static final String helpString = "List of Commands:" + nl +
			"Jump #### - Jump to adjacent star system #" + nl +
			"Scan - Display star system information" + nl +
			"Map - Display adject system map";
	
	//Public Methods
	public static String getCurrentStarSystemString(Star star, String connectionString, List<Player> players) {
		return starTag + star.getId() + nl +
				connectionsTag + connectionString + nl +
				planetsTag + getStarPlanetString(star) + nl +
				playersTag + getPlayersString(players);
	}
	
	public static String getScoreString(Player humanPlayer, List<Player> players) {
		String result = "You: " + humanPlayer.getWallet().getCredits() + nl;
		for (Player player : players) {
			result += player.toString() + ": " + player.getWallet().getCredits() + nl;
		}
		return result;
	}
	
	public static String getPlayerStatusString(Player player) {
		return getPlayerCargoStatusString(player); 
	}
	
	public static String getDockString(Station station, Player player) {
		return getStationString(station) + nl + getPlayerCargoStatusString(player);
	}
	
	public static String getStationString(Station station) {
		CargoHold hold = station.getCargoHold();
		return creditsTag + station.getWallet().getCredits() + nl +
				hold.getCargoSpaceUsage() + " / " + hold.getSize() + nl +
				getStationCargoHoldString(station);
	}
	
	public static String getHelpString() {
		return helpString;
	}
	
	
	// Support Methods
	private static String getPlayerCargoStatusString(Player player) {
		CargoHold hold = player.getShip().getCargoHold();
		return creditsTag + player.getWallet().getCredits() + nl +
				hold.getCargoSpaceUsage() + " / " + hold.getSize() + nl +
				getPlayerCargoHoldString(hold); 
	}
	
	private static String getPlayersString(List<Player> players) {
		String result = "";
		for (Player player : players) {
			result += player.toString();
		}
		return result;
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
			result = result.substring(0, result.length()-2);
		}
		
		return result;
	}
	
	private static String getStationCargoHoldString(Station station) {
		int longestCargoString = getLongestCargoEnumSize();
		
		String titleBar = "| " + frontPad("type", longestCargoString) + " :  qty :    $ |";
		String bottomBar = getRepeatingCharacter("-", titleBar.length());
		String result = bottomBar + nl + titleBar + nl + bottomBar + nl;
		
		CargoHold hold = station.getCargoHold();
		for (int i = 0; i < CargoEnum.values().length; i++) {
			result += "| " + frontPad(CargoEnum.values()[i].toString(),longestCargoString) + " : " + 
					frontPad(String.valueOf(hold.getCargo()[i]),4) + " : " + 
					frontPad(String.valueOf(station.getPrices()[i]),4) + " : " + nl;
		}
		
		result += bottomBar;
		
		return result;
	}
	
	private static String getPlayerCargoHoldString(CargoHold hold) {
		int longestCargoString = getLongestCargoEnumSize();
		
		String titleBar = "| " + frontPad("type", longestCargoString) + " :  qty |";
		String bottomBar = getRepeatingCharacter("-", titleBar.length());
		String result = bottomBar + nl + titleBar + nl + bottomBar + nl;
		;
		for (int i = 0; i < CargoEnum.values().length; i++) {
			result += "| " + frontPad(CargoEnum.values()[i].toString(),longestCargoString) + " : " + 
					frontPad(String.valueOf(hold.getCargo()[i]),4) + " : " + nl;
		}
		
		result += bottomBar;
		
		return result;
	}
	
	private static String getRepeatingCharacter(String repeatMe, int num) {
		String result = "";
		for (int i = 0; i < num; i++) {
			result += repeatMe;
		}
		return result;
	}
	
	private static int getLongestCargoEnumSize() {
		int longest = 0;
		for (int i = 0; i < CargoEnum.values().length; i++) {
			if (CargoEnum.values()[i].toString().length() > longest) {
				longest = CargoEnum.values()[i].toString().length();
			}
		}
		return longest;
	}
	
	private static String frontPad(String s, int max) {
		for (int i = s.length(); i < max; i++) {
			s = " " + s;
 		}
		return s;
	}	
}
