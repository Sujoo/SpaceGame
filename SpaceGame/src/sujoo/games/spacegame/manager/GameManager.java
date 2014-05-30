package sujoo.games.spacegame.manager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import sujoo.games.spacegame.ai.PlayerManagerAI;
import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.cargo.CargoHold;
import sujoo.games.spacegame.datatype.command.AttackCommand;
import sujoo.games.spacegame.datatype.command.DockCommand;
import sujoo.games.spacegame.datatype.command.ShipLocationCommand;
import sujoo.games.spacegame.datatype.command.PrimaryCommand;
import sujoo.games.spacegame.datatype.command.SubCommand;
import sujoo.games.spacegame.datatype.general.Star;
import sujoo.games.spacegame.datatype.player.AIPlayer;
import sujoo.games.spacegame.datatype.player.HumanPlayer;
import sujoo.games.spacegame.datatype.player.Player;
import sujoo.games.spacegame.datatype.player.Station;
import sujoo.games.spacegame.datatype.ship.ShipFactory;
import sujoo.games.spacegame.datatype.ship.ShipType;
import sujoo.games.spacegame.gui.BattleFeedbackEnum;
import sujoo.games.spacegame.gui.MainGui;
import sujoo.games.spacegame.message.CommandException;

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

    private GameState state;

    private Player battlePlayer;
    private int battleCounter;

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
        state = GameState.NEWGAME;
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
        state = GameState.DEFAULT;
        scanSystem(allPlayers.get(0));
    }

    private void playerKilled(Player player) {
        if (player instanceof AIPlayer) {
            aiPlayers.remove(player);
            allPlayers.remove(player);
        } else {
            state = GameState.NEWGAME;
        }
    }

    // *************************
    // * Entry point for command input
    // *************************
    public void enterCommand(String command, Player player) {
        try {
            String[] commandString = command.split(" ");
            if (SubCommand.isHelpCommand(commandString[0]) || commandString[0].equals("")) {
                help(commandString);
            } else if (SubCommand.isBackCommand(commandString[0])) {
                displayDefault(player);
            }
            else {
                switch (state) {
                case NEWGAME:
                    if (SubCommand.isYesCommand(commandString[0])) {
                        rebootGame();
                    }
                    break;
                case BATTLE:
                    executeBattleCommand(commandString, player);
                    break;
                case DOCKED:
                    executeDockCommand(commandString, player);
                    break;
                case DEFAULT:
                    executePrimaryCommand(commandString, player);
                    break;
                }
            }
        } catch (CommandException e) {
            gui.displayError(e.getMessage());
        }
    }

    // *************************
    // * Battle Commands
    // *************************
    private void executeBattleCommand(String[] commandString, Player player) throws CommandException {
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
                if (state == GameState.BATTLE) {
                    if (battlePlayer instanceof AIPlayer) {
                        handleBattleFeedback(PlayerManagerAI.attackPlayer((AIPlayer) battlePlayer, player, battleCounter),
                                battlePlayer, player, true);
                    } else {
                        // future code for hotseat human battles
                    }
                }
            }
        } else {
            throw new CommandException("Invalid battle command: " + commandString[0]);
        }
    }

    // *************************
    // * Target Command
    // *************************
    private BattleFeedbackEnum target(String[] commandString, Player player) throws CommandException {
        BattleFeedbackEnum feedback = null;
        if (commandString.length > 1) {
            ShipLocationCommand secondCommand = ShipLocationCommand.toCommand(commandString[1]);
            if (secondCommand != null) {
                feedback = BattleManager.damageComponent(battlePlayer, secondCommand,
                        player.getShip().getCurrentComponentValue(ShipLocationCommand.WEAPON));
            } else {
                throw new CommandException("Invalid component name");
            }
        } else {
            throw new CommandException("Not enough input");
        }
        return feedback;
    }

    // *************************
    // * Repair Command
    // *************************
    private BattleFeedbackEnum repair(String[] commandString, Player player) throws CommandException {
        BattleFeedbackEnum feedback = null;
        if (commandString.length > 1) {
            ShipLocationCommand secondCommand = ShipLocationCommand.toCommand(commandString[1]);
            if (secondCommand != null) {
                feedback = BattleManager.repairComponent(player, secondCommand);
            } else {
                throw new CommandException("Invalid component name");
            }
        } else {
            throw new CommandException("Not enough input");
        }
        return feedback;
    }

    // *************************
    // * Escape Command
    // *************************
    private BattleFeedbackEnum escape(Player player) throws CommandException {
        BattleFeedbackEnum feedback = null;
        if (player.getShip().isEscapePossible(battleCounter)) {
            BattleManager.escape(player, battlePlayer);
        } else {
            throw new CommandException("Escape is not yet available");
        }
        return feedback;
    }

    private void handleBattleFeedback(BattleFeedbackEnum feedback, Player attacker, Player defender, boolean aiFeedback) {
        switch (feedback) {
        case SHIP_DESTROYED:
            attacker.getShip().restoreAllComponents();
            endBattle();
            playerKilled(defender);
            attacker.getWallet().addCredits(2000);
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

        if (state == GameState.BATTLE && aiFeedback) {
            continueBattle(defender);
        }
    }

    private void continueBattle(Player player) {
        gui.displayBattleFeedback(BattleManager.performShieldRecharge(player, battleCounter));
        gui.displayBattleFeedback(BattleManager.performShieldRecharge(battlePlayer, battleCounter));
        displayBattle(player);
        battleCounter++;
    }

    // *************************
    // * Primary Commands
    // *************************
    private void executePrimaryCommand(String[] commandString, Player player) throws CommandException {
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
            case WAIT:
                waitTurn(player);
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
            }
        } else {
            throw new CommandException("Invalid command: " + commandString[0]);
        }
    }

    // *************************
    // * Jump Command Logic
    // *************************
    private void jump(String[] commandString, Player player) throws CommandException {
        if (commandString.length > 1 && StringUtils.isNumeric(commandString[1])) {
            Star jumpToStar = starSystemManager.getStarSystem(Integer.parseInt(commandString[1]));
            if (jumpToStar != null && starSystemManager.isNeighbor(player.getCurrentStar(), jumpToStar)) {
                player.setNewCurrentStar(jumpToStar);
                advanceTurn();
                scanSystem(player);
            } else {
                throw new CommandException("Invalid secondary command: " + commandString[1]);
            }
        } else {
            throw new CommandException("Not enough input");
        }
    }

    // *************************
    // * Attack Command Logic
    // *************************
    private void attack(String[] commandString, Player player) throws CommandException {
        if (commandString.length > 1) {
            Player otherPlayer = getAIPlayer(commandString[1]);
            if (otherPlayer != null) {
                if (player.getCurrentStar().equals(otherPlayer.getCurrentStar())) {
                    initializeBattle(otherPlayer);
                    displayBattle(player);
                } else {
                    throw new CommandException("Player <" + commandString[1] + "> not in system");
                }
            } else {
                throw new CommandException("Invalid player name: " + commandString[1]);
            }
        } else {
            throw new CommandException("Command <attack> required additional input");
        }
    }

    private void initializeBattle(Player opponent) {
        battlePlayer = opponent;
        battleCounter = 1;
        state = GameState.BATTLE;
    }

    private void endBattle() {
        battlePlayer = null;
        battleCounter = 0;
        state = GameState.DEFAULT;
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
        gui.displayBattle(player, battlePlayer, battleCounter);
    }

    // *************************
    // * Scan Command Logic
    // *************************
    private void scan(String[] commandString, Player player) throws CommandException {
        if (commandString.length == 1) {
            scanSystem(player);
        } else if (commandString.length > 1) {
            Player otherPlayer = getAIPlayer(commandString[1]);
            if (otherPlayer != null) {
                if (player.getCurrentStar().equals(otherPlayer.getCurrentStar())) {
                    scanPlayer(otherPlayer);
                } else {
                    throw new CommandException("Player <" + commandString[1] + "> not in system");
                }
            } else {
                throw new CommandException("Invalid player name: " + commandString[1]);
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
        state = GameState.DOCKED;
        CargoHold stationHold = player.getCurrentStar().getStation().getCargoHold();
        CargoHold playerHold = player.getCargoHold();
        for (CargoEnum cargoEnum : CargoEnum.getList()) {
            playerHold.setTransactionPrice(stationHold.getTransactionPrice(cargoEnum), cargoEnum);
        }
        displayDockCargo(player);
    }

    private void undock(Player player) {
        state = GameState.DEFAULT;
        scanSystem(player);
    }

    private void displayDockCargo(Player player) {
        gui.displayDockCargo(player);
    }

    // *************************
    // * Buy Command Logic
    // *************************
    private void buy(String[] commandString, Player player) throws CommandException {
        if (commandString.length >= 3) {
            CargoEnum cargoEnum = CargoEnum.toCargoEnum(commandString[2]);
            if (cargoEnum != null) {
                Station station = player.getCurrentStar().getStation();
                int amountToBuy = getAmount(commandString[1], player.getWallet().getCredits(),
                        station.getTransactionPrice(cargoEnum), player
                                .getCargoHold().getRemainingCargoSpace(), cargoEnum.getSize(), station.getCargoHold()
                                .getCargoAmount(cargoEnum));
                int validationCode = TransactionManager.validateBuyFromStationTransaction(player, station, cargoEnum,
                        amountToBuy);
                switch (validationCode) {
                case 0:
                    TransactionManager.performBuyFromStationTransaction(player, station, cargoEnum, amountToBuy);
                    break;
                case 1:
                    // gui.displayError(ErrorEnum.PLAYER_NO_CARGO_SPACE);
                    throw new CommandException("<" + player.getName() + "> has insufficient cargo space");
                case 2:
                    throw new CommandException("<" + station.getName() + "> has insufficient cargo");
                case 3:
                    throw new CommandException("<" + player.getName() + "> has insufficient funds");
                }
                displayDockCargo(player);
            }
        } else {
            throw new CommandException("Not enough input");
        }
    }

    private void install(String[] commandString, Player player) throws CommandException {
        if (commandString.length > 1) {
            String componentName = "";
            for (int i = 1; i < commandString.length; i++) {
                componentName += " " + commandString[i];
            }
            componentName = componentName.trim();
            if (!componentName.equals("")) {
                Station station = player.getCurrentStar().getStation();
                int validationCode = TransactionManager
                        .validateInstallFromStationTransaction(player, station, componentName);
                switch (validationCode) {
                case 0:
                    TransactionManager.performInstallFromStationTransaction(player, station, componentName);
                    break;
                case 1:
                    throw new CommandException("<" + player.getName() + "> has insufficient materials");
                case 2:
                    throw new CommandException("<" + player.getName() + "> has insufficient funds");
                case 3:
                    throw new CommandException("<" + station.getName() + "> does not have component <" + componentName + ">");
                case 4:
                    throw new CommandException("<" + station.getName() + "> has insufficient cargo space");
                }
                displayDockStore(player);
            }
        } else {
            throw new CommandException("Not enough input");
        }
    }

    // *************************
    // * Sell Command Logic
    // *************************
    private void sell(String[] commandString, Player player) throws CommandException {
        if (commandString.length >= 3) {
            CargoEnum cargoEnum = CargoEnum.toCargoEnum(commandString[2]);
            if (cargoEnum != null) {
                Station station = player.getCurrentStar().getStation();
                int amountToSell = getAmount(commandString[1], station.getWallet().getCredits(),
                        station.getTransactionPrice(cargoEnum), station
                                .getCargoHold().getRemainingCargoSpace(), cargoEnum.getSize(), player.getCargoHold()
                                .getCargoAmount(cargoEnum));
                int validationCode = TransactionManager.validateSellToStationTransaction(player, station, cargoEnum,
                        amountToSell);
                switch (validationCode) {
                case 0:
                    TransactionManager.performSellToStationTransaction(player, station, cargoEnum, amountToSell);
                    break;
                case 1:
                    throw new CommandException("<" + station.getName() + "> has insufficient cargo space");
                case 2:
                    throw new CommandException("<" + player.getName() + "> has insufficient cargo amount");
                case 3:
                    throw new CommandException("<" + station.getName() + "> has insufficient funds");
                }
                displayDockCargo(player);
            }
        } else {
            throw new CommandException("Not enough input");
        }
    }

    private int getAmount(String command, int buyerCredits, int cargoPrice, int remainingCargoSpace, int cargoSize,
            int maxStock)
            throws CommandException {
        int amount = 0;
        if (StringUtils.isNumeric(command)) {
            amount = Integer.parseInt(command);
        } else if (SubCommand.isMaxAllCommand(command)) {
            amount = TransactionManager.getMaximumAmount(buyerCredits, cargoPrice, remainingCargoSpace, cargoSize, maxStock);
        } else {
            throw new CommandException("Invalid transcation amount: " + command);
        }
        return amount;
    }

    // *************************
    // * Wait Command Logic
    // *************************
    private void waitTurn(Player player) {
        advanceTurn();
        displayDefault(player);
    }

    private void displayDefault(Player player) {
        if (state == GameState.DEFAULT) {
            score();
        } else if (state == GameState.BATTLE) {
            displayBattle(player);
        } else if (state == GameState.DOCKED) {
            displayDockCargo(player);
        }
    }

    // *************************
    // * Score Command Logic
    // *************************
    private void score() {
        gui.displayScore(allPlayers);
    }

    private void executeDockCommand(String[] commandString, Player player) throws CommandException {
        if (DockCommand.isDockCommand(commandString[0])) {
            DockCommand firstCommand = DockCommand.toCommand(commandString[0]);
            switch (firstCommand) {
            case UNDOCK:
                undock(player);
                break;
            case BUY:
                buy(commandString, player);
                break;
            case SELL:
                sell(commandString, player);
                break;
            case CARGO:
                displayDockCargo(player);
                break;
            case STORE:
                displayDockStore(player);
                break;
            case INSTALL:
                install(commandString, player);
                break;
            }
        } else {
            throw new CommandException("Invalid dock command: " + commandString[0]);
        }
    }

    private void displayDockStore(Player player) {
        gui.displayDockStore(player);
    }

    // *************************
    // * Help Command Logic
    // *************************
    private void help(String[] commandString) throws CommandException {
        String code = "";
        List<String> listOfCommands = Lists.newArrayList();
        List<String> commandExplanation = Lists.newArrayList();
        if (state == GameState.DEFAULT) {
            listOfCommands = PrimaryCommand.getCodeList();
        } else if (state == GameState.BATTLE) {
            listOfCommands = AttackCommand.getCodeList();
        } else if (state == GameState.DOCKED) {
            listOfCommands = DockCommand.getCodeList();
        }

        if (commandString.length > 1) {
            if (state == GameState.DEFAULT) {
                PrimaryCommand subCommand = PrimaryCommand.toCommand(commandString[1]);
                if (subCommand != null) {
                    code = subCommand.getCode();
                    commandExplanation = subCommand.getExplanation();
                } else {
                    code = SubCommand.HELP.getCode();
                    commandExplanation = SubCommand.HELP.getExplanation();
                }
            } else if (state == GameState.BATTLE) {
                AttackCommand subCommand = AttackCommand.toCommand(commandString[1]);
                if (subCommand != null) {
                    code = subCommand.getCode();
                    commandExplanation = subCommand.getExplanation();
                } else {
                    code = SubCommand.HELP.getCode();
                    commandExplanation = SubCommand.HELP.getExplanation();
                }
            } else if (state == GameState.DOCKED) {
                DockCommand subCommand = DockCommand.toCommand(commandString[1]);
                if (subCommand != null) {
                    code = subCommand.getCode();
                    commandExplanation = subCommand.getExplanation();
                } else {
                    code = SubCommand.HELP.getCode();
                    commandExplanation = SubCommand.HELP.getExplanation();
                }
            }
        } else if (commandString[0].equals("") || SubCommand.isHelpCommand(commandString[0])) {
            code = SubCommand.HELP.getCode();
            commandExplanation = SubCommand.HELP.getExplanation();
        } else {
            throw new CommandException("Invalid help command");
        }

        gui.displayHelp(code, listOfCommands, commandExplanation);
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
            PlayerManagerAI.stationCargoHasBeenRefreshed(aiPlayers);
        }
    }
}
