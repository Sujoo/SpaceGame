package sujoo.games.spacegame.ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.cargo.CargoHold;
import sujoo.games.spacegame.datatype.command.ShipLocationCommand;
import sujoo.games.spacegame.datatype.general.Star;
import sujoo.games.spacegame.datatype.player.AIPlayer;
import sujoo.games.spacegame.datatype.player.Player;
import sujoo.games.spacegame.datatype.player.Station;
import sujoo.games.spacegame.datatype.ship.Ship;
import sujoo.games.spacegame.datatype.ship.ShipFactory;
import sujoo.games.spacegame.datatype.ship.ShipType;
import sujoo.games.spacegame.gui.BattleFeedbackEnum;
import sujoo.games.spacegame.manager.BattleManager;
import sujoo.games.spacegame.manager.StarSystemManager;
import sujoo.games.spacegame.manager.TransactionManager;

public class PlayerManagerAI {
    private static final Random random = new Random();
    private static final int initCredits = 1000;

    public static void stationCargoHasBeenRefreshed(List<AIPlayer> aiPlayers) {
        for (AIPlayer player : aiPlayers) {
            player.resetStationLogic();
        }
    }

    public static List<AIPlayer> createAIPlayers(int numberOfAI, StarSystemManager starSystemManager) {
        List<AIPlayer> aiPlayers = Lists.newArrayList();
        List<String> traderNames = getNames("resources\\TraderNames.txt");
        for (int i = 0; i < numberOfAI; i++) {
            AIPlayer p = new AIPlayer(ShipFactory.buildShip(ShipType.SMALL_TRANS), initCredits, traderNames.get(i));
            p.setNewCurrentStar(starSystemManager.getRandomStarSystem());
            aiPlayers.add(p);
        }
        return aiPlayers;
    }

    private static List<String> getNames(String fileName) {
        List<String> names = Lists.newArrayList();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            String name;
            while ((name = reader.readLine()) != null) {
                names.add(name);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return names;
    }

    public static void performAIPlayerTurns(List<AIPlayer> aiPlayers, StarSystemManager starSystemManager) {
        for (AIPlayer player : aiPlayers) {
            // System.out.println(player.getName());
            // System.out.println(player.getCurrentStar().getStation());
            trade(player);
            jumpToNewSystem(player, starSystemManager);
            player.clearRecentlyPurchased();
            // System.out.println("Jump to: " +
            // player.getCurrentStar().getStation());
            // for (CargoEnum cargoEnum : CargoEnum.getList()) {
            // System.out.println(cargoEnum + ": " +
            // player.getCargoHold().getCargoAmount(cargoEnum) + ",b: "
            // + player.getBestBuyPrice(cargoEnum) + ",s: " +
            // player.getBestSellPrice(cargoEnum));
            // }
            // System.out.println("-------------------------------");
        }
    }

    private static void trade(AIPlayer player) {
        checkPrices(player);
        sellToStation(player);
        buyFromStation(player);
    }

    private static void checkPrices(AIPlayer player) {
        for (CargoEnum cargoEnum : CargoEnum.getList()) {
            player.updateCargoPrice(cargoEnum, player.getCurrentStar());
        }
    }

    private static void buyFromStation(AIPlayer player) {
        Station station = player.getCurrentStar().getStation();
        int credits = player.getWallet().getCredits();
        CargoHold hold = player.getCargoHold();

        // while ai has money and cargo space
        if (credits > 0 && hold.getRemainingCargoSpace() > 0) {
            int greatestDifference = 500;
            CargoEnum bestCargoEnum = null;
            // find best value item to buy that station has in stock
            for (CargoEnum cargoEnum : CargoEnum.getList()) {
                // This needs to buy not the cargo with the greatest difference
                // from base, but with the best known difference!
                if (player.isKnownBuyPriceLessThanSellPrice(cargoEnum)) {
                    int difference = station.getTransactionPrice(cargoEnum) - player.getBestBuyPrice(cargoEnum);
                    if (difference < greatestDifference && station.getCargoHold().getCargoAmount(cargoEnum) > 0
                            && !player.isRecentlyPurchased(cargoEnum)) {
                        greatestDifference = difference;
                        bestCargoEnum = cargoEnum;
                    }
                }
            }

            // buy max of that item
            if (bestCargoEnum != null && player.isBestBuyPrice(bestCargoEnum, station.getTransactionPrice(bestCargoEnum))) {
                int maxPurchaseAmount = TransactionManager.getMaximumAmount(credits,
                        station.getTransactionPrice(bestCargoEnum),
                        hold.getRemainingCargoSpace(), bestCargoEnum.getSize(),
                        station.getCargoHold().getCargoAmount(bestCargoEnum));
                if (TransactionManager.validateBuyFromStationTransaction(player, station, bestCargoEnum, maxPurchaseAmount) == 0) {
                    TransactionManager.performBuyFromStationTransaction(player, station, bestCargoEnum, maxPurchaseAmount);
                    // remember what items have been bought
                    player.addRecentlyPurchased(bestCargoEnum);
                    // System.out.println("bought " + maxPurchaseAmount + " " +
                    // bestCargoEnum);
                }
            }
        }
    }

    private static void sellToStation(AIPlayer player) {
        Station station = player.getCurrentStar().getStation();
        CargoHold hold = player.getCargoHold();

        // find best value item to sell that player has in cargo and wasn't just
        // bought
        if (hold.getCargoSpaceUsage() > 0) {
            int greatestDifference = 0;
            CargoEnum bestCargoEnum = null;
            for (CargoEnum cargoEnum : CargoEnum.getList()) {
                if (hold.getCargoAmount(cargoEnum) > 0) {
                    int s = station.getTransactionPrice(cargoEnum);
                    int p = player.getTransactionPrice(cargoEnum);
                    int difference = s - p;
                    if (difference > greatestDifference && !player.isRecentlyPurchased(cargoEnum)) {
                        greatestDifference = difference;
                        bestCargoEnum = cargoEnum;
                    }
                }

            }

            // sell max of that item
            if (bestCargoEnum != null && player.isBestSellPrice(bestCargoEnum, station.getTransactionPrice(bestCargoEnum))) {
                int maxSaleAmount = TransactionManager.getMaximumAmount(station.getWallet().getCredits(),
                        station.getTransactionPrice(bestCargoEnum),
                        station.getCargoHold().getRemainingCargoSpace(), bestCargoEnum.getSize(),
                        hold.getCargoAmount(bestCargoEnum));
                if (TransactionManager.validateSellToStationTransaction(player, station, bestCargoEnum, maxSaleAmount) == 0) {
                    TransactionManager.performSellToStationTransaction(player, station, bestCargoEnum, maxSaleAmount);
                    player.addRecentlyPurchased(bestCargoEnum);
                    // //System.out.println("sold " + maxSaleAmount + " " +
                    // bestCargoEnum);
                }
            }
        }
    }

    private static void jumpToNewSystem(AIPlayer player, StarSystemManager starSystemManager) {
        List<Star> neighbors = starSystemManager.getNeighbors(player.getCurrentStar());
        Star nextJump;
        nextJump = neighbors.get(random.nextInt(neighbors.size()));
        player.setNewCurrentStar(nextJump);
    }

    public static BattleFeedbackEnum attackPlayer(AIPlayer aiPlayer, Player player, int battleCounter) {
        Ship enemyShip = player.getShip();
        Ship aiShip = aiPlayer.getShip();
        ShipLocationCommand attackHere = ShipLocationCommand.HULL;
        boolean attack = true;
        int damage = aiShip.getCurrentComponentValue(ShipLocationCommand.WEAPON);

        if (aiShip.getCurrentComponentValue(ShipLocationCommand.HULL) <= enemyShip
                .getCurrentComponentValue(ShipLocationCommand.WEAPON)) {
            if (aiShip.isEscapePossible(battleCounter) && random.nextBoolean()) {
                return BattleManager.escape(aiPlayer, player);
            } else {
                attack = false;
                attackHere = ShipLocationCommand.HULL;
            }
        } else if (aiShip.getCurrentComponentValue(ShipLocationCommand.WEAPON) < aiShip
                .getCurrentMaxComponentValue(ShipLocationCommand.WEAPON) && random.nextBoolean()) {
            attack = false;
            attackHere = ShipLocationCommand.WEAPON;
        } else if (enemyShip.isShieldUp()
                && enemyShip.getComponent(ShipLocationCommand.SHIELD).getCurrentValue() >= damage) {
            attackHere = ShipLocationCommand.SHIELD;
        } else if (enemyShip.getCurrentComponentValue(ShipLocationCommand.WEAPON) >= damage && random.nextBoolean()) {
            attackHere = ShipLocationCommand.WEAPON;
        } else {
            attackHere = ShipLocationCommand.HULL;
        }

        if (attack) {
            return BattleManager.damageComponent(player, attackHere, damage);
        } else {
            return BattleManager.repairComponent(aiPlayer, attackHere);
        }
    }
}
