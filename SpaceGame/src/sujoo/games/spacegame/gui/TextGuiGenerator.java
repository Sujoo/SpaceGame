package sujoo.games.spacegame.gui;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.cargo.CargoHold;
import sujoo.games.spacegame.datatype.command.AttackSubCommand;
import sujoo.games.spacegame.datatype.command.PrimaryCommand;
import sujoo.games.spacegame.datatype.general.Star;
import sujoo.games.spacegame.datatype.general.Wallet;
import sujoo.games.spacegame.datatype.planet.Planet;
import sujoo.games.spacegame.datatype.player.Player;
import sujoo.games.spacegame.datatype.player.Station;
import sujoo.games.spacegame.datatype.ship.Ship;

public class TextGuiGenerator {
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
	
	//Public Methods
	public static STextArea getLoseUpperPanel() {
		STextArea textArea = new STextArea();
		textArea.appendLine("You...");
		textArea.appendLine("have...");
		textArea.appendLine("DIED!");
		return textArea;
	}
	
	public static STextArea getLoseLowerPanel() {
		STextArea textArea = new STextArea();
		textArea.appendLine("Play Again?");
		return textArea;
	}
	
	public static STextArea getScanPlayerUpperPanel(Player player) {
		STextArea textArea = new STextArea();
		includeShipText(textArea, player);
		return textArea;
	}
	
	public static STextArea getScanPlayerLowerPanel(Player player) {
		STextArea textArea = new STextArea();
		includeCargoText(textArea, player);
		return textArea;
	}
	
	public static STextArea getScanSystemLowerPanel(Star star, String connectionString, List<Player> players) {		
	    STextArea textArea = new STextArea();

	    textArea.append(starTag, Colors.gold);
	    textArea.appendLine(String.valueOf(star.getId()));
	    textArea.append(connectionsTag, Colors.gold);
	    textArea.appendLine(connectionString, Colors.paleGreen);
	    textArea.append(planetsTag);
	    textArea.appendLine(getPlanetInSystemString(star));
	    textArea.append(playersTag, Colors.deepSkyBlue);
	    textArea.append(getPlayerNamesString(players), Colors.deepSkyBlue);
	    
	    return textArea;
	}
	
	public static STextArea getScoreLowerPanel(List<Player> players) {
		STextArea textArea = new STextArea();
		
		int longestNameLength = getLongestTextLength(players.toArray());
		
		Collections.sort(players);
		Collections.reverse(players);
		for (Player player : players) {
			textArea.appendLine(frontPad(player.toString(), longestNameLength) + " : " + player.getScore());
		}
		
		return textArea;
	}
	
	public static STextArea getStatusUpperPanel(Player player) {
		STextArea textArea = new STextArea();
		includeShipText(textArea, player);
		return textArea;
	}
	
	public static STextArea getStatusLowerPanel(Player player) {
		STextArea textArea = new STextArea();
		includeCargoText(textArea, player);
		return textArea;
	}
	
	public static STextArea getDockUpperPanel(Station station) {
		STextArea textArea = new STextArea();
		includeCargoText(textArea, station);
		return textArea;
	}
	
	public static STextArea getDockLowerPanel(Player player) {
		STextArea textArea = new STextArea();
		includeCargoText(textArea, player);
		return textArea;
	}
	
	public static STextArea getHelpUpperPanel() {
		STextArea textArea = new STextArea();
		includeTitleText(textArea, "List of Commands");
		for (PrimaryCommand primaryCommand : PrimaryCommand.getList()) {
			textArea.appendLine(primaryCommand.getCode());
		}
		return textArea;
	}	
	
	public static STextArea getHelpLowerPanel(PrimaryCommand primaryCommand) {
		STextArea textArea = new STextArea();
		includeTitleText(textArea, primaryCommand.getCode() + " Command");
		for (String text : primaryCommand.getExplanation()) {
			textArea.appendLine(text);
		}
		return textArea;
	}

	// Support Methods
	private static void includeCargoText(STextArea textArea, Station station) {
		includeCargoText(textArea, station.getName(), station.getWallet(), station.getCargoHold(), true);
	}
	
	private static void includeCargoText(STextArea textArea, Player player) {
		includeCargoText(textArea, player.getName(), player.getWallet(), player.getCargoHold(), false);
	}
	
	private static void includeCargoText(STextArea textArea, String name, Wallet wallet, CargoHold hold, boolean isStation) {
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
		
		for (CargoEnum cargoEnum : CargoEnum.getList()) {
			textArea.appendLine("| " + frontPad(cargoEnum.toString(), longestCargoString) + " : " + 
					frontPad(String.valueOf(hold.getCargoAmount(cargoEnum)),4) + " : " + 
					frontPad(String.valueOf(isStation?hold.getTransactionPrice(cargoEnum):hold.getCargoAmount(cargoEnum)*cargoEnum.getBaseValue()),5) + " : ");			
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
		includeShieldCapacityText(textArea, ship.getCurrentComponentValue(AttackSubCommand.SHIELD), ship.getCurrentMaxComponentValue(AttackSubCommand.SHIELD));
		includeHullCapacityText(textArea, ship.getCurrentComponentValue(AttackSubCommand.HULL), ship.getCurrentMaxComponentValue(AttackSubCommand.HULL));
		textArea.appendLine("Engine Power: " + ship.getCurrentComponentValue(AttackSubCommand.ENGINE));
		textArea.appendLine("Weapon Power: " + ship.getCurrentComponentValue(AttackSubCommand.WEAPON));
	}
	
	private static void includeCreditsText(STextArea textArea, Wallet wallet) {
		textArea.appendLine(creditsTag + wallet.getCredits(), Colors.gold);
	}
	
	private static void includeCargoCapacityText(STextArea textArea, int spaceUsage, int totalSize) {
		textArea.append(cargoSpaceTag);
		includeCapacityText(textArea, spaceUsage, totalSize, new Color[]{Colors.forestGreen,Colors.orange,Colors.crimson});
	}
	
	private static void includeShieldCapacityText(STextArea textArea, int currentShields, int maxShields) {
		textArea.append(shieldTag);
		includeCapacityText(textArea, currentShields, maxShields, new Color[]{Colors.crimson,Colors.orange,Colors.forestGreen});
	}
	
	private static void includeHullCapacityText(STextArea textArea, int currentHull, int maxHull) {
		textArea.append(hullTag);
		includeCapacityText(textArea, currentHull, maxHull, new Color[]{Colors.crimson,Colors.orange,Colors.forestGreen});
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
