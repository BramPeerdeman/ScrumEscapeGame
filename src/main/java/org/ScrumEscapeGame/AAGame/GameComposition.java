package org.ScrumEscapeGame.AAGame;

import org.ScrumEscapeGame.AAEvents.*;
import org.ScrumEscapeGame.AAUserInterface.ConsoleWindow;
import org.ScrumEscapeGame.AAUserInterface.DisplayService;
import org.ScrumEscapeGame.AAUserInterface.GameUIService;
import org.ScrumEscapeGame.Commands.CommandManager;
import org.ScrumEscapeGame.GameObjects.Player;
import org.ScrumEscapeGame.GameObjects.Room;
import org.ScrumEscapeGame.Handlers.BossDoorUnlockHandler;
import org.ScrumEscapeGame.Handlers.MapBuilder;
import org.ScrumEscapeGame.Items.InventoryManager;
import org.ScrumEscapeGame.Rooms.RoomDefinition;
import org.ScrumEscapeGame.Rooms.RoomFactory;
import org.ScrumEscapeGame.Rooms.ZoneConfig;

import java.util.Map;

/**
 * GameComposition manages the construction of all core components in the game.
 * It initializes the game context, room management, command handling, UI setup,
 * and event observers.
 */
public class GameComposition {
    private final EventPublisher<GameEvent> publisher;
    private final Player player;
    private final RoomManager roomManager;
    private final CommandManager commandManager;
    private final InventoryManager inventoryManager;
    private final GameContext gameContext;
    private final ConsoleWindow consoleWindow;
    private final GameUIService uiService;
    private final GameCycleManager cycleManager;

    /**
     * Constructs the GameComposition and initializes all dependencies.
     * This class ensures that event observers are registered properly
     * and that the UI is set up before launching the game.
     */
    public GameComposition() {
        // Core game state management
        this.player = new Player();
        this.roomManager = new RoomManager();
        this.commandManager = new CommandManager();
        this.inventoryManager = new InventoryManager();
        this.publisher = new EventPublisher<>();
        this.gameContext = new GameContext(player, roomManager, publisher, commandManager, inventoryManager);

        // UI components setup
        this.consoleWindow = new ConsoleWindow(gameContext);
        this.uiService = consoleWindow.getUiService();  // Access DisplayService via UIService.

        // Game cycle and event observers
        this.cycleManager = new GameCycleManager(gameContext, consoleWindow, commandManager, publisher);
        registerObservers();
    }

    /**
     * Registers event observers to listen for game events.
     * Ensures that UI, reset, door unlock, and game start events are handled properly.
     */
    private void registerObservers() {
        publisher.addObserver(new GameUIObserver(uiService));
        publisher.addObserver(new ResetObserver(cycleManager));
        publisher.addObserver(new GlobalDoorUnlockObserver(uiService));
        publisher.addObserver(new GameBeginObserver(cycleManager));
        publisher.addObserver(new ItemObserver(uiService));
        publisher.addObserver(new InventoryObserver(uiService));
        publisher.addObserver(new InventoryItemActionObserver(gameContext, uiService));
        publisher.addObserver(new BossDoorUnlockHandler(gameContext));
    }


    /**
     * Retrieves the game cycle manager responsible for starting and resetting the game.
     *
     * @return The GameCycleManager instance.
     */
    public GameCycleManager getCycleManager() {
        return cycleManager;
    }

    /**
     * Retrieves the main console window for UI interactions.
     *
     * @return The ConsoleWindow instance.
     */
    public ConsoleWindow getConsoleWindow() {
        return consoleWindow;
    }

    /**
     * Retrieves the GameContext which manages core game data.
     *
     * @return The GameContext instance.
     */
    public GameContext getGameContext() {
        return gameContext;
    }

}


