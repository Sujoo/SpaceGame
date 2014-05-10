package sujoo.games.spacegame.manager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import sujoo.games.spacegame.ai.PlayerManagerAI;
import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.command.AttackCommand;
import sujoo.games.spacegame.datatype.command.AttackSubCommand;
import sujoo.games.spacegame.datatype.command.PrimaryCommand;
import sujoo.games.spacegame.datatype.command.TransactionSubCommand;
import sujoo.games.spacegame.datatype.general.Star;
import sujoo.games.spacegame.datatype.player.AIPlayer;
import sujoo.games.spacegame.datatype.player.HumanPlayer;
import sujoo.games.spacegame.datatype.player.Player;
import sujoo.games.spacegame.datatype.player.Station;
import sujoo.games.spacegame.datatype.ship.ShipFactory;
import sujoo.games.spacegame.datatype.ship.ShipType;
import sujoo.games.spacegame.gui.BattleFeedbackEnum;
import sujoo.games.spacegame.gui.ErrorEnum;
import sujoo.games.spacegame.gui.MainGui;

public class GameManager {
    private final int totalStarSystems = 10;
    private final int maximumConnections = 4;
    private final int minStarId = 1000;
    private final int numberOfAIPlayers = 5;
    private final int minPlanets = 1;
    private final int maxPlanets = 4;
    private final int initCredits = 1000;

    private final int turnsUntilStationRefreshBase = 50;
    private final int turnsUntilModifier = totalStarSystems / numberOfAIPlayers;
    private final int turnsUntilStationRefresh = turnsUntilStationRefreshBase * turnsUntilModifier + 1;

    private StarSystemManager starSystemManager;
    private MainGui gui;

    private List<Player> allPlayers;
    private List<AIPlayer> aiPlayers;
    private int turnCounter;

    private boolean youAreDead;

    private Player battlePlayer;
    private int battleCounter;
    private boolean inBattle;

    public GameManager() {
        initializeGame();
    }

    private void initializeGame() {
        starSystemManager = new StarSystemManager(minStarId, totalStarSystems, maximumConnections, minPlanets, maxPlanets);
        aiPlayers = PlayerManagerAI.createAIPlayers(numberOfAIPlayers, starSystemManager);

        Player humanPlayer = new HumanPlayer(ShipFactory.buildShip(ShipType.SMALL_TRANS), initCredits, "Sujoo");
        humanPlayer.setNewCurrentStar(starSystemManager.getRandomStarSystem());

        allPlayers = Lists.newArrayList();
        allPlayers.add(humanPlayer);
        allPlayers.addAll(aiPlayers);

        gui = MainGui.getInstance(this, humanPlayer);
        gui.setVisible(true);
        turnCounter = 0;
        youAreDead = true;
        endBattle();
    }

    private void rebootGame() {
        initializeGame();
        play();
    }

    // *************************
    // * Start the game, then wait for gui input
    // *************************
    public void play() {
        youAreDead = false;
        scanSystem(allPlayers.get(0));
    }

    public void playerKilled(Player player) {
        if (player instanceof AIPlayer) {
            aiPlayers.remove(player);
            allPlayers.remove(player);
        } else {
            youAreDead = true;
        }
    }

    // *************************
    // * Entry point for command input
    // *************************
    public void enterCommand(String command, Player player) {
        if (youAreDead) {
            if (command.equalsIgnoreCase("y") || command.equalsIgnoreCase("yes")) {
                rebootGame();
            }
        } else if (inBattle) {
            executeBattleCommand(command, player);
        } else {
            executePrimaryCommand(command, player);
        }
    }

    // *************************
    // * Battle Commands
    // *************************
    private void executeBattleCommand(String command, Player player) {
        String[] commandString = command.split(" ");
        if (AttackCommand.isAttackCommand(commandString[0])) {
            BattleFeedbackEnum feedback = null;
            AttackCommand firstCommand = AttackCommand.toCommand(commandString[0]);
            switch (firstCommand) {
            case TARGET:
                feedback = target(commandString, player);
                break;
            case REPAIR:
                feedback = repair(commandString, player);
                break;
            case ESCAPE:
                feedback = escape(player);
                break;
            }

            if (feedback != null) {
                handleBattleFeedback(feedback, player, battlePlayer, false);
                if (inBattle) {
                    if (battlePlayer instanceof AIPlayer) {
                        handleBattleFeedback(PlayerManagerAI.attackPlayer((AIPlayer) battlePlayer, player), battlePlayer, player, true);
                    } else {
                        // future code for hotseat human battles
                    }
                }
            }
        } else {
            // Get Attack Help
            gui.displayError(ErrorEnum.INVALID_PRIMARY_COMMAND);
        }
    }

    private void continueBattle(Player player) {
        battleCounter++;
        player.getShip().performShieldRecharge(battleCounter);
        battlePlayer.getShip().performShieldRecharge(battleCounter);
        displayBattle(player);
    }

    // *************************
    // * Target Command
    // *************************
    private BattleFeedbackEnum target(String[] commandString, Player player) {
        BattleFeedbackEnum feedback = null;
        if (commandString.length > 1) {
            AttackSubCommand secondCommand = AttackSubCommand.toCommand(commandString[1]);
            if (secondCommand != null) {
                feedback = battlePlayer.getShip().damageComponent(secondCommand, player.getShip().getCurrentComponentValue(AttackSubCommand.WEAPON));
            } else {
                gui.displayError(ErrorEnum.INVALID_PLAYER_NAME);
            }
        } else {
            gui.displayError(ErrorEnum.NOT_ENOUGH_INPUT);
        }
        return feedback;
    }

    // *************************
    // * Repair Command
    // *************************
    private BattleFeedbackEnum repair(String[] commandString, Player player) {
        BattleFeedbackEnum feedback = null;
        if (commandString.length > 1) {
            AttackSubCommand secondCommand = AttackSubCommand.toCommand(commandString[1]);
            if (secondCommand != null) {
                feedback = player.getShip().repairComponent(secondCommand);
            } else {
                gui.displayError(ErrorEnum.INVALID_PLAYER_NAME);
            }
        } else {
            gui.displayError(ErrorEnum.NOT_ENOUGH_INPUT);
        }
        return feedback;
    }

    // *************************
    // * Escape Command
    // *************************
    private BattleFeedbackEnum escape(Player player) {
        BattleFeedbackEnum feedback = null;
        if (player.getShip().isEscapePossible(battleCounter)) {
            feedback = BattleFeedbackEnum.ESCAPE;
        } else {
            // add to battle log "you cannot escape at this time"
        }
        return feedback;
    }

    private void handleBattleFeedback(BattleFeedbackEnum feedback, Player attacker, Player defender, boolean aiFeedback) {
        switch (feedback) {
        case SHIP_DESTROYED:
            attacker.getShip().restoreAllComponents();
            playerKilled(defender);
            endBattle();
            if (aiFeedback) {
                gui.displayLoss();
            } else {
                gui.displayStatus(attacker);
                gui.displayBattleFeedback(feedback);
            }
            break;
        case COMPONENT_DAMAGE:
            gui.displayBattleFeedback(feedback);
            break;
        case COMPONENT_REPAIR:
            gui.displayBattleFeedback(feedback);
            break;
        case SHIELD_HIT:
            gui.displayBattleFeedback(feedback);
            break;
        case SHIELD_RECHARGE:
            gui.displayBattleFeedback(feedback);
            break;
        case ESCAPE:
            attacker.getShip().restoreAllComponents();
            battlePlayer.getShip().restoreAllComponents();
            endBattle();
            gui.displayStatus(attacker);
            gui.displayBattleFeedback(feedback);
            break;
        }

        if (inBattle && aiFeedback) {
            continueBattle(defender);
        }
    }

    // *************************
    // * Primary Commands
    // *************************
    private void executePrimaryCommand(String command, Player player) {
        String[] commandString = command.split(" ");
        if (PrimaryCommand.isPrimaryCommand(commandString[0])) {
            PrimaryCommand firstCommand = PrimaryCommand.toCommand(commandString[0]);
            switch (firstCommand) {
            case JUMP:
                jump(commandString, player);
                break;
            case ATTACK:
                attack(commandString, player);
                break;
            case SCAN:
                scan(commandString, player);
                break;
            case STATUS:
                status(player);
                break;
            case DOCK:
                dock(player);
                break;
            case BUY:
                buy(commandString, player);
                break;
            case SELL:
                sell(commandString, player);
                break;
            case WAIT:
                waitTurn();
                break;
            case SCORE:
                score();
                break;
            case MAP:
                displayLocalSystemMap(player.getCurrentStar(), player.getPreviousStar());
                break;
            case FULL_MAP:
                displayGlobalSystemMap(player.getCurrentStar(), player.getPreviousStar());
                break;
            case HELP:
                help(commandString);
                break;
            }
        } else {
            help(commandString);
        }
    }

    // *************************
    // * Jump Command Logic
    // *************************
    private void jump(String[] commandString, Player player) {
        if (commandString.length > 1 && StringUtils.isNumeric(commandString[1])) {
            Star jumpToStar = starSystemManager.getStarSystem(Integer.parseInt(commandString[1]));
            if (jumpToStar != null && starSystemManager.isNeighbor(player.getCurrentStar(), jumpToStar)) {
                player.setNewCurrentStar(jumpToStar);
                advanceTurn();
                scanSystem(player);
            }
        } else {
            gui.displayError(ErrorEnum.NOT_ENOUGH_INPUT);
        }
    }

    // *************************
    // * Attack Command Logic
    // *************************
    private void attack(String[] commandString, Player player) {
        if (commandString.length > 1) {
            Player otherPlayer = getAIPlayer(commandString[1]);
            if (otherPlayer != null) {
                if (player.getCurrentStar().equals(otherPlayer.getCurrentStar())) {
                    initializeBattle(otherPlayer);
                    displayBattle(player);
                } else {
                    gui.displayError(ErrorEnum.PLAYER_NOT_IN_SYSTEM);
                }
            } else {
                gui.displayError(ErrorEnum.INVALID_PLAYER_NAME);
            }
        } else {
            gui.displayError(ErrorEnum.NOT_ENOUGH_INPUT);
        }
    }

    public void initializeBattle(Player opponent) {
        battlePlayer = opponent;
        battleCounter = 1;
        inBattle = true;
    }

    private void endBattle() {
        battlePlayer = null;
        battleCounter = 0;
        inBattle = false;
    }

    private Player getAIPlayer(String name) {
        Player aiPlayer = null;
        for (Player player : aiPlayers) {
            if (player.getName().equalsIgnoreCase(name)) {
                aiPlayer = player;
                break;
            }
        }
        return aiPlayer;
    }

    private void displayBattle(Player player) {
        gui.displayBattle(player, battlePlayer);
    }

    // *************************
    // * Scan Command Logic
    // *************************
    private void scan(String[] commandString, Player player) {
        if (commandString.length == 1) {
            scanSystem(player);
        } else if (commandString.length > 1) {
            Player otherPlayer = getAIPlayer(commandString[1]);
            if (otherPlayer != null) {
                if (player.getCurrentStar().equals(otherPlayer.getCurrentStar())) {
                    scanPlayer(otherPlayer);
                } else {
                    gui.displayError(ErrorEnum.PLAYER_NOT_IN_SYSTEM);
                }
            } else {
                gui.displayError(ErrorEnum.INVALID_PLAYER_NAME);
            }
        }
    }

    private void scanSystem(Player player) {
        displayScanSystem(player);
        displayLocalSystemMap(player.getCurrentStar(), player.getPreviousStar());
    }

    private void displayScanSystem(Player player) {
        gui.displayScanSystem(player, starSystemManager.getNeighborsString(player.getCurrentStar()),
                getAIPlayersInStarSystem(player.getCurrentStar()));
    }

    private List<Player> getAIPlayersInStarSystem(Star star) {
        List<Player> resultList = Lists.newArrayList();
        for (Player player : aiPlayers) {
            if (player.getCurrentStar().equals(star)) {
                resultList.add(player);
            }
        }
        return resultList;
    }

    private void scanPlayer(Player player) {
        gui.displayScanPlayer(player);
    }

    // *************************
    // * Status Command Logic
    // *************************
    private void status(Player player) {
        gui.displayStatus(player);
    }

    // *************************
    // * Dock Command Logic
    // *************************
    private void dock(Player player) {
        displayDockCargo(player);
    }

    private void displayDockCargo(Player player) {
        gui.displayDockCargo(player);
    }

    // *************************
    // * Buy Command Logic
    // *************************
    private void buy(String[] commandString, Player player) {
        if (commandString.length >= 3) {
            CargoEnum cargoEnum = CargoEnum.toCargoEnum(commandString[2]);
            if (cargoEnum != null) {
                Station station = player.getCurrentStar().getStation();
                int amountToBuy = getAmount(commandString[1], player.getWallet().getCredits(), station.getTransactionPrice(cargoEnum), player
                        .getCargoHold().getRemainingCargoSpace(), cargoEnum.getSize(), station.getCargoHold().getCargoAmount(cargoEnum));
                int validationCode = TransactionManager.validateBuyFromStationTransaction(player, station, cargoEnum, amountToBuy);
                switch (validationCode) {
                case 0:
                    TransactionManager.performBuyFromStationTransaction(player, station, cargoEnum, amountToBuy);
                    break;
                case 1:
                    gui.displayError(ErrorEnum.PLAYER_NO_CARGO_SPACE);
                    break;
                case 2:
                    gui.displayError(ErrorEnum.STATION_NO_CARGO_SPACE);
                    break;
                case 3:
                    gui.displayError(ErrorEnum.PLAYER_NO_MONEY);
                    break;
                }
                displayDockCargo(player);
            }
        } else {
            gui.displayError(ErrorEnum.NOT_ENOUGH_INPUT);
        }
    }

    // *************************
    // * Sell Command Logic
    // *************************
    private void sell(String[] commandString, Player player) {
        if (commandString.length >= 3) {
            CargoEnum cargoEnum = CargoEnum.toCargoEnum(commandString[2]);
            if (cargoEnum != null) {
                Station station = player.getCurrentStar().getStation();
                int amountToSell = getAmount(commandString[1], station.getWallet().getCredits(), station.getTransactionPrice(cargoEnum), station
                        .getCargoHold().getRemainingCargoSpace(), cargoEnum.getSize(), player.getCargoHold().getCargoAmount(cargoEnum));
                int validationCode = TransactionManager.validateSellToStationTransaction(player, station, cargoEnum, amountToSell);
                switch (validationCode) {
                case 0:
                    TransactionManager.performSellToStationTransaction(player, station, cargoEnum, amountToSell);
                    break;
                case 1:
                    gui.displayError(ErrorEnum.STATION_NO_CARGO_SPACE);
                    break;
                case 2:
                    gui.displayError(ErrorEnum.PLAYER_NO_CARGO_SPACE);
                    break;
                case 3:
                    gui.displayError(ErrorEnum.STATION_NO_MONEY);
                    break;
                }
                displayDockCargo(player);
            }
        } else {
            gui.displayError(ErrorEnum.NOT_ENOUGH_INPUT);
        }
    }

    private int getAmount(String command, int buyerCredits, int cargoPrice, int remainingCargoSpace, int cargoSize, int maxStock) {
        int amount = 0;
        if (StringUtils.isNumeric(command)) {
            amount = Integer.parseInt(command);
        } else if (TransactionSubCommand.isMaxAllCommand(command)) {
            amount = TransactionManager.getMaximumAmount(buyerCredits, cargoPrice, remainingCargoSpace, cargoSize, maxStock);
        } else {
            gui.displayError(ErrorEnum.INVALID_TRANSACTION_AMOUNT);
        }
        return amount;
    }

    // *************************
    // * Wait Command Logic
    // *************************
    private void waitTurn() {
        advanceTurn();
    }

    // *************************
    // * Score Command Logic
    // *************************
    private void score() {
        gui.displayScore(allPlayers);
    }

    // *************************
    // * Help Command Logic
    // *************************
    private void help(String[] commandString) {
        if (PrimaryCommand.toCommand(commandString[0]) == null) {
            gui.displayError(ErrorEnum.INVALID_PRIMARY_COMMAND);
        } else {
            PrimaryCommand secondCommand = PrimaryCommand.HELP;
            if (commandString.length >= 2) {
                if (PrimaryCommand.toCommand(commandString[1]) != null) {
                    secondCommand = PrimaryCommand.toCommand(commandString[1]);
                } else {
                    gui.displayError(ErrorEnum.INVALID_SECONDARY_COMMAND);
                }
            } else {
                gui.displayError(ErrorEnum.NOT_ENOUGH_INPUT);
            }
            gui.displayHelp(secondCommand);
        }
    }

    // *************************
    // * Display JUNG Graphs
    // *************************
    private void displayLocalSystemMap(Star currStar, Star prevStar) {
        gui.loadSystemMap(starSystemManager.createSubGraph(currStar), currStar, prevStar);
    }

    private void displayGlobalSystemMap(Star currStar, Star prevStar) {
        gui.loadSystemMap(starSystemManager.getStarGraph(), currStar, prevStar);
    }

    // *************************
    // * End turn timing and logic
    // *************************
    private void advanceTurn() {
        PlayerManagerAI.performAIPlayerTurns(aiPlayers, starSystemManager);
        turnCounter++;
        if (turnCounter % turnsUntilStationRefresh == 0) {
            starSystemManager.refreshStationCargo();
        }
    }
}
