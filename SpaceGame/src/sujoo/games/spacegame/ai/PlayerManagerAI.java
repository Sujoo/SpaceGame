package sujoo.games.spacegame.ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.cargo.CargoHold;
import sujoo.games.spacegame.datatype.command.AttackSubCommand;
import sujoo.games.spacegame.datatype.general.Star;
import sujoo.games.spacegame.datatype.player.AIPlayer;
import sujoo.games.spacegame.datatype.player.Player;
import sujoo.games.spacegame.datatype.player.Station;
import sujoo.games.spacegame.datatype.ship.ShipFactory;
import sujoo.games.spacegame.datatype.ship.ShipType;
import sujoo.games.spacegame.gui.BattleFeedbackEnum;
import sujoo.games.spacegame.manager.StarSystemManager;
import sujoo.games.spacegame.manager.TransactionManager;

public class PlayerManagerAI {
    private static final Random random = new Random();
    private static final int initCredits = 1000;

    // private List<AIPlayer> aiPlayers;
    // private StarSystemManager starSystemManager;

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
            trade(player);
            jumpToNewSystem(player, starSystemManager);
            player.clearRecentlyPurchased();
        }
    }

    private static void trade(AIPlayer player) {
        sellToStation(player);
        buyFromStation(player);
    }

    private static void buyFromStation(AIPlayer player) {
        Station station = player.getCurrentStar().getStation();
        int credits = player.getWallet().getCredits();
        CargoHold hold = player.getCargoHold();

        // while ai has money and cargo space
        if (credits > 0 && hold.getRemainingCargoSpace() > 0) {
            int greatestDifference = 0;
            CargoEnum bestCargoEnum = null;
            // find best value item to buy that station has in stock
            for (CargoEnum cargoEnum : CargoEnum.getList()) {
                int difference = cargoEnum.getBaseValue() - station.getTransactionPrice(cargoEnum);
                if (difference > greatestDifference && station.getCargoHold().getCargoAmount(cargoEnum) > 0
                        && !player.isRecentlyPurchased(bestCargoEnum)) {
                    greatestDifference = difference;
                    bestCargoEnum = cargoEnum;
                }
            }

            // buy max of that item
            if (bestCargoEnum != null) {
                int maxPurchaseAmount = TransactionManager.getMaximumAmount(credits, station.getTransactionPrice(bestCargoEnum),
                        hold.getRemainingCargoSpace(), bestCargoEnum.getSize(), station.getCargoHold().getCargoAmount(bestCargoEnum));
                if (TransactionManager.validateBuyFromStationTransaction(player, station, bestCargoEnum, maxPurchaseAmount) == 0) {
                    TransactionManager.performBuyFromStationTransaction(player, station, bestCargoEnum, maxPurchaseAmount);
                    // remember what items have been bought
                    player.addRecentlyPurchased(bestCargoEnum);
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
                    int difference = station.getTransactionPrice(cargoEnum) - player.getPurchasePrice(cargoEnum);
                    if (difference > greatestDifference && !player.isRecentlyPurchased(bestCargoEnum)) {
                        greatestDifference = difference;
                        bestCargoEnum = cargoEnum;
                    }
                }

            }

            // sell max of that item
            if (bestCargoEnum != null) {
                int maxSaleAmount = TransactionManager.getMaximumAmount(station.getWallet().getCredits(), station.getTransactionPrice(bestCargoEnum),
                        station.getCargoHold().getRemainingCargoSpace(), bestCargoEnum.getSize(), hold.getCargoAmount(bestCargoEnum));
                if (TransactionManager.validateSellToStationTransaction(player, station, bestCargoEnum, maxSaleAmount) == 0) {
                    TransactionManager.performSellToStationTransaction(player, station, bestCargoEnum, maxSaleAmount);
                    player.addRecentlyPurchased(bestCargoEnum);
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

    public static BattleFeedbackEnum attackPlayer(AIPlayer aiPlayer, Player player) {
        // THIS WILL CAUSE AN ISSUE AS SOON AS A SHIP DOESN'T HAVE EVERY COMPONENT
        AttackSubCommand location = AttackSubCommand.getList().get(random.nextInt(AttackSubCommand.getList().size()));

        return player.getShip().damageComponent(location, aiPlayer.getShip().getCurrentComponentValue(AttackSubCommand.WEAPON));
    }
}
