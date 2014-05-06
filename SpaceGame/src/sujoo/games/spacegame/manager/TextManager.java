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
import sujoo.games.spacegame.datatypes.ship.Ship;
import sujoo.games.spacegame.gui.STextArea;

public class TextManager {
	private static final String nl = System.lineSeparator();
	private static final String starTag =        "System : ";
	private static final String connectionsTag = "Exits  : ";
	private static final String planetsTag =     "Planets: ";
	private static final String playersTag =     "Players: ";
	
	private static final String creditsTag =    "Credits    : ";
	private static final String cargoSpaceTag = "Cargo Space: ";
	
	private static final String hullTag =   "Hull  : ";
	private static final String shieldTag = "Shield: ";
	
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
	public static Component getScanPlayerUpperPanel(Player player) {
		STextArea textArea = new STextArea();
		includeShipText(textArea, player);
		return wrapText(textArea);
	}
	
	public static Component getScanPlayerLowerPanel(Player player) {
		STextArea textArea = new STextArea();
		includeCargoText(textArea, player);
		return wrapText(textArea);
	}
	
	public static Component getScanLowerPanel(Star star, String connectionString, List<Player> players) {		
	    STextArea textArea = new STextArea();

	    textArea.append(starTag, Color.YELLOW);
	    textArea.appendLine(String.valueOf(star.getId()), Color.WHITE);
	    textArea.append(connectionsTag, Color.YELLOW);
	    textArea.appendLine(connectionString, Color.GREEN);
	    textArea.append(planetsTag, Color.WHITE);
	    textArea.appendLine(getPlanetInSystemString(star), Color.WHITE);
	    textArea.append(playersTag, Color.BLUE);
	    textArea.append(getPlayerNamesString(players), Color.BLUE);
	    
	    return wrapText(textArea);
	}
	
	public static Component getScoreLowerPanel(Player humanPlayer, List<Player> players) {
		STextArea textArea = new STextArea();
		textArea.appendLine("You: " + humanPlayer.getWallet().getCredits());
		
		for (Player player : players) {
			textArea.appendLine(player + ": " + player.getWallet().getCredits());
		}
		return wrapText(textArea);
	}
	
	public static Component getStatusUpperPanel(Player player) {
		STextArea textArea = new STextArea();
		includeShipText(textArea, player);
		return wrapText(textArea);
	}
	
	public static Component getStatusLowerPanel(Player player) {
		STextArea textArea = new STextArea();
		includeCargoText(textArea, player);
		return wrapText(textArea);
	}
	
	public static Component getDockUpperPanel(Station station) {
		STextArea textArea = new STextArea();
		includeCargoText(textArea, station);
		return wrapText(textArea);
	}
	
	public static Component getDockLowerPanel(Player player) {
		STextArea textArea = new STextArea();
		includeCargoText(textArea, player);
		return wrapText(textArea);
	}
	
	public static Component getHelpLowerPanel() {
		STextArea textArea = new STextArea();
		textArea.appendLine(helpString); 
		return wrapText(textArea);
	}

	// Support Methods
	private static void includeCargoText(STextArea textArea, Station station) {
		includeCargoText(textArea, "Station", station.getWallet(), station.getCargoHold(), station.getPrices(), true);
	}
	
	private static void includeCargoText(STextArea textArea, Player player) {
		includeCargoText(textArea, player.getName(), player.getWallet(), player.getShip().getCargoHold(), player.getShip().getCargoHold().getTotalValues(), false);
	}
	
	private static void includeCargoText(STextArea textArea, String name, Wallet wallet, CargoHold hold, int[] values, boolean isStation) {
		includeTitleText(textArea, name + " Cargo");
		includeCreditsText(textArea, wallet);
		includeCargoCapacityText(textArea, hold.getCargoSpaceUsage(), hold.getSize());
		
		int longestCargoString = getLongestTextLength(CargoEnum.values());
		String titleBar = "| " + frontPad("Type", longestCargoString) + " :  Qty :";
		if (isStation) {
			titleBar += " Price |";
		} else {
			titleBar += " Value |";
		}
		String bottomBar = getRepeatingCharacter("-", titleBar.length());
		textArea.appendLine(bottomBar);
		textArea.appendLine(titleBar);
		textArea.appendLine(bottomBar);
		
		for (int i = 0; i < CargoEnum.values().length; i++) {
			textArea.appendLine("| " + frontPad(CargoEnum.values()[i].toString(),longestCargoString) + " : " + 
					frontPad(String.valueOf(hold.getCargo()[i]),4) + " : " + 
					frontPad(String.valueOf(values[i]),5) + " : ");
		}
		
		textArea.appendLine(bottomBar);
	}
	
	private static void includeTitleText(STextArea textArea, String title) {
		String nameBottomBar = getRepeatingCharacter("-", title.length());
		textArea.appendLine(title);
		textArea.appendLine(nameBottomBar);
	}
	
	private static void includeShipText(STextArea textArea, Player player) {
		includeTitleText(textArea, player.getName() + " Ship");
		Ship ship = player.getShip();
		includeShieldCapacityText(textArea, ship.getCurrentShieldPoints(), ship.getMaxShieldPoints());
		includeHullCapacityText(textArea, ship.getCurrentHullPoints(), ship.getMaxHullPoints());
		textArea.appendLine("Engine Power: " + ship.getEnginePower());
		textArea.appendLine("Weapon Power: " + ship.getWeaponAttack());
	}
	
	private static void includeCreditsText(STextArea textArea, Wallet wallet) {
		textArea.appendLine(creditsTag + wallet.getCredits(), Color.YELLOW);
	}
	
	private static void includeCargoCapacityText(STextArea textArea, int spaceUsage, int totalSize) {
		textArea.append(cargoSpaceTag);
		includeCapacityText(textArea, spaceUsage, totalSize, new Color[]{Color.GREEN,Color.ORANGE,Color.RED});
	}
	
	private static void includeShieldCapacityText(STextArea textArea, int currentShields, int maxShields) {
		textArea.append(shieldTag);
		includeCapacityText(textArea, currentShields, maxShields, new Color[]{Color.RED,Color.ORANGE,Color.GREEN});
	}
	
	private static void includeHullCapacityText(STextArea textArea, int currentHull, int maxHull) {
		textArea.append(hullTag);
		includeCapacityText(textArea, currentHull, maxHull, new Color[]{Color.RED,Color.ORANGE,Color.GREEN});
	}
	
	private static void includeCapacityText(STextArea textArea, int usage, int max, Color[] colors) {
		Color spaceUsageColor = colors[1];
		if (usage <= max / 3) {
			spaceUsageColor = colors[0];
		} else if (usage >= (max / 3) * 2 ) {
			spaceUsageColor = colors[2];
		}
		textArea.append(usage + " ", spaceUsageColor);
		textArea.append("/");
		textArea.appendLine(" " + max);
	}
	
	private static String getPlayerNamesString(List<Player> players) {
		String result = "(" + players.size() + ") :";
		for (Player player : players) {
			result += nl + player.toString();
		}
		return result;
	}
	
	private static String getPlanetInSystemString(Star star) {
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
