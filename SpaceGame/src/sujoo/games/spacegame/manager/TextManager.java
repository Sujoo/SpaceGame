package sujoo.games.spacegame.manager;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JTextPane;

import sujoo.games.spacegame.datatypes.CargoEnum;
import sujoo.games.spacegame.datatypes.CargoHold;
import sujoo.games.spacegame.datatypes.Star;
import sujoo.games.spacegame.datatypes.Station;
import sujoo.games.spacegame.datatypes.Wallet;
import sujoo.games.spacegame.datatypes.planet.Planet;
import sujoo.games.spacegame.datatypes.player.Player;
import sujoo.games.spacegame.gui.STextArea;

public class TextManager {
	private static final String nl = System.lineSeparator();
	private static final String starTag = "System : ";
	private static final String connectionsTag = "Exits  : ";
	private static final String planetsTag = "Planets ";
	private static final String playersTag = "Players ";
	private static final String creditsTag = "Credits: ";
	
	private static final String dust = "Dust and Echoes";
	
	private static final String helpString = "List of Commands:" + nl +
			"Jump #### - Jump to adjacent star system #" + nl +
			"Scan - Display star system information" + nl +
			"Map - Display adject system map";
	
	private static Component wrapText(STextArea textArea) {
		JTextPane textPane = new JTextPane(textArea);
		textPane.setEditable(false);
		textPane.setBackground(Color.BLACK);
		return textPane;
	}
	
	//Public Methods
	public static Component getCurrentStarSystemPanel(Star star, String connectionString, List<Player> players) {		
	    STextArea textArea = new STextArea();

	    textArea.append(starTag, Color.YELLOW);
	    textArea.appendLine(String.valueOf(star.getId()), Color.WHITE);
	    textArea.append(connectionsTag, Color.YELLOW);
	    textArea.appendLine(connectionString, Color.GREEN);
	    textArea.append(planetsTag, Color.WHITE);
	    textArea.appendLine(getStarPlanetString(star), Color.WHITE);
	    textArea.append(playersTag, Color.BLUE);
	    textArea.append(getPlayersString(players), Color.BLUE);
	    
	    return wrapText(textArea);
	}
	
	public static Component getScoreString(Player humanPlayer, List<Player> players) {
		STextArea textArea = new STextArea();
		textArea.appendLine("You: " + humanPlayer.getWallet().getCredits());
		
		for (Player player : players) {
			textArea.appendLine(player + ": " + player.getWallet().getCredits());
		}
		return wrapText(textArea);
	}
	
	public static Component getPlayerStatusPanel(Player player) {
		STextArea textArea = new STextArea();
		includeCreditsText(textArea, player.getWallet());
		includePlayerCargoText(textArea, player); 
		return wrapText(textArea);
	}
	
	public static Component getDockUpperPanel(Station station) {
		STextArea textArea = new STextArea();
		includeCreditsText(textArea, station.getWallet());
		includeStationCargoText(textArea, station);
		return wrapText(textArea);
	}
	
	public static Component getDockLowerPanel(Player player) {
		STextArea textArea = new STextArea();
		includeCreditsText(textArea, player.getWallet());
		includePlayerCargoText(textArea, player); 
		return wrapText(textArea);
	}
	
	public static Component getHelpPanel() {
		STextArea textArea = new STextArea();
		textArea.appendLine(helpString); 
		return wrapText(textArea);
	}
	
	
	// Support Methods
	private static void includeCreditsText(STextArea textArea, Wallet wallet) {
		textArea.appendLine(creditsTag + wallet.getCredits(), Color.YELLOW);
	}
	private static void includeStationCargoText(STextArea textArea, Station station) {
		CargoHold hold = station.getCargoHold();
		textArea.appendLine(hold.getCargoSpaceUsage() + " / " + hold.getSize());
		
		int longestCargoString = getLongestTextLength(CargoEnum.values());
		String titleBar = "| " + frontPad("type", longestCargoString) + " :  qty :    $ |";
		String bottomBar = getRepeatingCharacter("-", titleBar.length());
		textArea.appendLine(bottomBar);
		textArea.appendLine(titleBar);
		textArea.appendLine(bottomBar);
		
		for (int i = 0; i < CargoEnum.values().length; i++) {
			textArea.appendLine("| " + frontPad(CargoEnum.values()[i].toString(),longestCargoString) + " : " + 
					frontPad(String.valueOf(hold.getCargo()[i]),4) + " : " + 
					frontPad(String.valueOf(station.getPrices()[i]),4) + " : ");
		}
		
		textArea.appendLine(bottomBar);
	}
	
	private static void includePlayerCargoText(STextArea textArea, Player player) {
		CargoHold hold = player.getShip().getCargoHold();
		textArea.appendLine(hold.getCargoSpaceUsage() + " / " + hold.getSize());
		
		int longestCargoString = getLongestTextLength(CargoEnum.values());
		String titleBar = "| " + frontPad("type", longestCargoString) + " :  qty |";
		String bottomBar = getRepeatingCharacter("-", titleBar.length());
		textArea.appendLine(bottomBar);
		textArea.appendLine(titleBar);
		textArea.appendLine(bottomBar);
		
		for (int i = 0; i < CargoEnum.values().length; i++) {
			textArea.appendLine("| " + frontPad(CargoEnum.values()[i].toString(),longestCargoString) + " : " + 
					frontPad(String.valueOf(hold.getCargo()[i]),4) + " : ");
		}
		
		textArea.appendLine(bottomBar);
	}
	
	private static String getPlayersString(List<Player> players) {
		String result = "(" + players.size() + ") :";
		for (Player player : players) {
			result += nl + player.toString();
		}
		return result;
	}
	
	private static String getStarPlanetString(Star star) {
		String result;
		if (star.getPlanets().size() == 0) {
			result = dust;
		} else {
			result = "(" + star.getPlanets().size() + ") :";
			for (Planet planet : star.getPlanets()) {
				result += nl + planet.getType().getLongDesc();
			}
		}
		
		return result;
	}
	
	private static String getRepeatingCharacter(String repeatMe, int num) {
		String result = "";
		for (int i = 0; i < num; i++) {
			result += repeatMe;
		}
		return result;
	}
	
	private static int getLongestTextLength(Object[] objects) {
		int longest = 0;
		for (int i = 0; i < objects.length; i++) {
			if (objects[i].toString().length() > longest) {
				longest = objects[i].toString().length();
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
