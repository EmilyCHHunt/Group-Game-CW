package com.example.cs230gamewithjavafx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * The Main Class for the application, containing the main method and many of the operations required
 * to initialise JavaFX
 * and many other methods that would otherwise bridge between multiple classes
 * @author Emily Hunt
 * @version 0.5.0
 */
public class Main extends Application {
    private BorderPane root = new BorderPane();
    /**
     * the main menu pane
     */
    private SplitPane mainMenuPane;
    /**
     * the pause menu pane
     */
    private Pane pauseMenuPane;
    /**
     * the stack of all panes to switch between,
     * includes main menu, pause menu, highScoreTable, winPane and Gameover
     */
    private StackPane lstOfAllPanes;
    /**
     * pane to contain the others?
     */
    private Pane boardPane;
    /**
     * the menu pane
     */
    private Pane menuPane;
    /**
     * is the game level paused or not -- for tick control
     */
    private boolean isPaused = false;
    /**
     * the highscore table view
     */
    private BorderPane highScoreTable;
    /**
     * the game over pane
     */
    private BorderPane gameOverPane;
    /**
     * player profile management pane
     */
    private BorderPane playerProfilePane;

    /**
     * dimensions of the canvas,  width
     */
    private static final int WINDOW_WIDTH = 1000;
    /**
     * dimensions of the canvas,  height
     */
    private static final int WINDOW_HEIGHT = 550;


    /**
     * the width of the game board grid in number of cells
     */
    private int gridWidth;
    /**
     * the height of the game board grid in number of cells
     */
    private int gridHeight;

    /**
     * The width and height (in pixels) of each cell that makes up the game.
     */
    private static final int GRID_CELL_WIDTH = 50;


    /**
     * stores the player found on the game board
     */
    private Player thePlayer;
    /**
     * stores the profile of the user playing the fame
     */
    private PlayerProfile theProfile;
    /**
     * stores the board itself of a game
     */
    private Tile[][] theBoardUI;

    /**
     * stores the current game state, the container class that contains
     * absolutely everything needed to fully re-create a level
     */
    private GameState curGameState;

    /**
     * the current Board being used or displayed
     */
    private Board curBoard;

    /**
     * the JavaFX canvas being drawn too
     */
    private Canvas canvas;
    /**
     * the Javafx scene being used
     */
    private Scene scene;
    /**
     * the primary stage being displayed
     */
    private Stage primaryStage;

    /**
     * tracks the game ticks
     */
    private Timeline tickTimeline;
    /**
     * how often a tick occurs, in milliseconds
     */
    private final int tickTime = 1000;
    /**
     * monitors if the player is dead or not
     */
    private boolean playerDead;
    /**
     * Text to show how much time is left in the game
     */
    private Text timeLimitText;
    /**
     * Text to show the players current score
     */
    private Text currentScoreText;
    /**
     * Text to show the current level being played
     */
    private Text currentLevelText;
    /**
     * the win pane shown when a player wins the game
     */
    private BorderPane winPane;

    /**
     * On start display the MainMenu
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        displayMainMenu();
    }

    /**
     * launch the entire application from here
     * @param args the game code to run
     */
    public static void main(String[] args) {
        System.out.println("VAMOS!");
        launch(args);
    }

    /**
     * Tick moves the NPCs, checks NPC collision, countdowns the bomb and checks its collision and updates game info
     */
    private void tick() {

        if (!playerDead) {
            System.out.println(curGameState.getTimeLimit());

            //can we use the curGameState.incrementTimeLimit(false) here?
            curGameState.setTimeLimit(curGameState.getTimeLimit() - tickTime / 1000);
            timeLimitText.setText("Time left: " + curGameState.getTimeLimit());
            currentScoreText.setText("Score: " + curGameState.getThePlayer().getScore());
            if (curGameState.getTimeLimit() <= 0) {
                gameOver();
            }
            //tie this into the countdown clock when that exists i.e. when countdown hits zero stop ticking and end game
            ArrayList<NPC> lstOfNPCs = new ArrayList<>(curGameState.getNPCs());
            for (NPC npc : lstOfNPCs) {
                try {
                    if (npc.getEntityName().equals("SmartThief")) {
                        npc.move(npc.getDirection(), curBoard, theBoardUI, curGameState.getItems());
                    } else if (npc.getEntityName().equals("FlyingAssassin")) {
                        npc.move(npc.getDirection(), curBoard, theBoardUI, curGameState);
                        for (NPC npcCheck : lstOfNPCs) {
                            if (npc.getxCoord() == npcCheck.getxCoord() && npc.getyCoord() == npcCheck.getyCoord()
                                    && npc != npcCheck) {
                                npcCheck.removeFromBoard(curBoard, curGameState);
                            }
                        }
                    } else {
                        npc.move(npc.getDirection(), curBoard, theBoardUI, curGameState);
                    }
                    int npcX = npc.getxCoord();
                    int npcY = npc.getyCoord();
                    Tile targetTile = curBoard.getTile(npcX, npcY);
                    if (npc.getEntityName().equals("FloorFollowingThief") || npc.getEntityName().equals("SmartThief")) {
                        if (curBoard.getTile(npcX, npcY).getOccupyingItem() != null) {
                            if (targetTile.getOccupyingItem().getItemType().equals("Loot")) {
                                ((CollectableItem) targetTile.getOccupyingItem()).collect(curBoard, curGameState);
                            } else if (targetTile.getOccupyingItem().getItemType().equals("Clock")) {
                                curGameState.increaseTimeLimit(-((CollectableItem) targetTile.getOccupyingItem()).collect(curBoard, curGameState));
                            } else if (targetTile.getOccupyingItem().getItemType().equals("Door")) {
                                if (((Door) targetTile.getOccupyingItem()).isOpen()) {
                                    gameOver();
                                }
                            } else if (targetTile.getOccupyingItem().getItemType().equals("Lever")) {
                                ((Lever) targetTile.getOccupyingItem()).collect(curBoard, curGameState);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Cant move NPC dead");
                }
            }
            checkCollision(thePlayer.getxCoord(), thePlayer.getyCoord());

            boolean openDoor = true;
            Door tempDoor = null;
            for (Item item : curGameState.getItems()) {
                if (item.getItemType().equals("Loot") || item.getItemType().equals("Lever")) {
                    openDoor = false;
                } else if (item.getItemType().equals("Door")) {
                    tempDoor = (Door) item;
                }
            }
            if (openDoor && tempDoor != null) {
                tempDoor.setOpen(true);
                System.out.println("open door");
            }
            bombSweep(thePlayer.getxCoord(), thePlayer.getyCoord());
            for (int i = 0; i < curGameState.getItems().size(); i++) {
                Item item = curGameState.getItems().get(i);
                if (item.getEntityName().equals("Bomb")) {
                    Bomb bombOnFuse = (Bomb) item;
                    if (bombOnFuse.isArmed()) {
                        bombOnFuse.incrementDetonateTick();
                        System.out.println(bombOnFuse.getTimeToDetonate() + " : " + bombOnFuse.getDetonateTick());
                        if (bombOnFuse.getTimeToDetonate() == bombOnFuse.getDetonateTick()) {
                            bombOnFuse.incrementDetonateTick();
                            System.out.println("CONTACT!");
                            bombOnFuse.detonate(curGameState, curBoard, bombOnFuse);
                        } else if (bombOnFuse.getTimeToDetonate() < bombOnFuse.getDetonateTick()) {
                            bombOnFuse.getImageView().setVisible(false);
                        }
                    }
                }
            }
        }
    }

    /**
     * Loads new board into the scene and hides other menus
     *
     * @param levelNum   Number of the level to load
     * @param playerName Name of the Player playing
     */
    public void loadNewBoard(int levelNum, String playerName) {
        playerDead = false;

        canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);


        curGameState = GameIOManager.loadGame(levelNum, playerName);
        curBoard = curGameState.getTheBoard();
        thePlayer = curGameState.getThePlayer();

        root.setCenter(canvas);

        gridWidth = curBoard.getBoardMaxX();
        gridHeight = curBoard.getBoardMaxY();

        generateNewBoardUi();

        displayLevel();
        if (tickTimeline == null) {
            tickTimeline = new Timeline(new KeyFrame(Duration.millis(tickTime), event -> tick()));
            // Loop the timeline forever can be changed to the time limit variable
            tickTimeline.setCycleCount(Animation.INDEFINITE);
        }
        tickTimeline.play();
    }


    /**
     * Generates the scenes UI components and displays them.
     */
    private void displayLevel() {
        menuPane = new Pane();
        menuPane.setDisable(true);

        boardPane = new AnchorPane();
        boardPane.setTranslateX((WINDOW_WIDTH - gridWidth * GRID_CELL_WIDTH) / 2.0);
        boardPane.setTranslateY((WINDOW_HEIGHT - gridHeight * GRID_CELL_WIDTH) / 2.0);

        AnchorPane textPane = new AnchorPane();
        timeLimitText = new Text("Time left: " + curGameState.getTimeLimit());
        textPane.getChildren().add(timeLimitText);
        timeLimitText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        timeLimitText.setTranslateX((gridWidth - 3) / 2.0 * GRID_CELL_WIDTH);

        currentLevelText = new Text("Level: " + curGameState.getLevelNum());
        textPane.getChildren().add(currentLevelText);
        currentLevelText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        currentScoreText = new Text("Score: " + curGameState.getThePlayer().getScore());
        currentLevelText.setTextAlignment(TextAlignment.RIGHT);
        textPane.getChildren().add(currentScoreText);
        currentScoreText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        currentScoreText.setTranslateX((gridWidth - 2) * GRID_CELL_WIDTH);
        //textPane.setTranslateY(-GRID_CELL_WIDTH/2);

        root = new BorderPane();


        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                boardPane.getChildren().add(theBoardUI[y][x]);
            }
        }

        //Setting the start position of the player image
        thePlayer.getImageView().setX(theBoardUI[thePlayer.getyCoord()][thePlayer.getxCoord()].getTranslateX());
        thePlayer.getImageView().setY(theBoardUI[thePlayer.getyCoord()][thePlayer.getxCoord()].getTranslateY());

        //Setting the start position of the enemies

        for (int i = 0; i < curGameState.getNPCs().size(); i++) {
            curGameState.getNPCs().get(i).getImageView().setX(theBoardUI[curGameState.getNPCs().get(i).getyCoord()]
                    [curGameState.getNPCs().get(i).getxCoord()].getTranslateX());
            curGameState.getNPCs().get(i).getImageView().setY(theBoardUI[curGameState.getNPCs().get(i).getyCoord()]
                    [curGameState.getNPCs().get(i).getxCoord()].getTranslateY());

        }

        //Setting the position of the items
        for (int i = 0; i < curGameState.getItems().size(); i++) {
            curGameState.getItems().get(i).getImageView().setX(theBoardUI[curGameState.getItems().get(i).getyCoord()]
                    [curGameState.getItems().get(i).getxCoord()].getTranslateX());
            curGameState.getItems().get(i).getImageView().setY(theBoardUI[curGameState.getItems().get(i).getyCoord()]
                    [curGameState.getItems().get(i).getxCoord()].getTranslateY());
        }

        //setting the fit height and width of the player image view
        thePlayer.setFitHeightandWidth(GRID_CELL_WIDTH);

        //setting the fit height and width of the enemy image view
        for (NPC npc : curGameState.getNPCs()) {
            npc.setFitHeightandWidth(GRID_CELL_WIDTH);
        }

        //setting the fit height and width of the item imageview's
        for (Item item : curGameState.getItems()) {
            item.setFitHeightandWidth(GRID_CELL_WIDTH);
        }

        root.getChildren().addAll(boardPane, pauseMenuPane, gameOverPane, winPane);
        boardPane.getChildren().add(thePlayer.getImageView());

        for (Item item : curGameState.getItems()) {
            boardPane.getChildren().add(item.getImageView());
        }

        for (NPC npc : curGameState.getNPCs()) {
            boardPane.getChildren().add(npc.getImageView());
        }


        boardPane.getChildren().add(textPane);
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> processKeyEvent(event));
        displayScene();
    }

    /**
     * Creates the UI for the board with each Tile represented by a Node
     */
    public void generateNewBoardUi() {
        theBoardUI = new Tile[gridHeight][gridWidth];
        // initialize
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                try {
                    // create node
                    Tile node = new Tile(curBoard.getTile(x, y).getColour(),
                            x * GRID_CELL_WIDTH, y * GRID_CELL_WIDTH, GRID_CELL_WIDTH);
                    theBoardUI[y][x] = node;
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("^At (" + x + "," + y + ")");
                }
            }
        }
    }

    /**
     * Handles player inputs
     *
     * @param event
     */
    public void processKeyEvent(KeyEvent event) {
        // We change the behaviour depending on the actual key that was pressed.
        if (!isPaused && !playerDead) {
            curBoard.getTile(thePlayer.getxCoord(), thePlayer.getyCoord()).holdsPlayer(false);
            switch (event.getCode()) {
                case RIGHT:
                    // Right key was pressed. So move the player right by one cell.
                    if (thePlayer.checkPlayerMoveColour("E", curBoard)) {
                        thePlayer.getImageView().setX(theBoardUI[0][0].getTranslateX() + thePlayer.getxCoord() * GRID_CELL_WIDTH);
                    }
                    break;
                case LEFT:
                    if (thePlayer.checkPlayerMoveColour("W", curBoard)) {
                        thePlayer.getImageView().setX(theBoardUI[0][0].getTranslateX() + thePlayer.getxCoord() * GRID_CELL_WIDTH);
                    }
                    break;
                case UP:
                    if (thePlayer.checkPlayerMoveColour("N", curBoard)) {
                        thePlayer.getImageView().setY(theBoardUI[0][0].getTranslateY() + thePlayer.getyCoord() * GRID_CELL_WIDTH);
                    }
                    break;
                case DOWN:
                    if (thePlayer.checkPlayerMoveColour("S", curBoard)) {
                        thePlayer.getImageView().setY(theBoardUI[0][0].getTranslateY() + thePlayer.getyCoord() * GRID_CELL_WIDTH);
                    }
                    break;
                case ESCAPE:
                    handlePause();//Opens pause menu
                    isPaused = true;
                default:
                    // Do nothing for all other keys.
                    break;
            }
            event.consume();
            checkCollision(thePlayer.getxCoord(), thePlayer.getyCoord());
            curBoard.getTile(thePlayer.getxCoord(), thePlayer.getyCoord()).holdsPlayer(true);
        } else if (event.getCode() == KeyCode.ESCAPE && !playerDead) {
            //Closes pause menu
            handleUnPause();
            isPaused = false;
        }
        event.consume();
    }

    /**
     * Checks player collision with NPCs and Items
     *
     * @param xCoord
     * @param yCoord
     */
    private void checkCollision(int xCoord, int yCoord) {
        if (curBoard.getTile(xCoord, yCoord).getOccupyingCharacter() != null) {
            if (curBoard.getTile(xCoord, yCoord).getOccupyingCharacter().getEntityName().equals("FlyingAssassin")) {
                gameOver();
                System.out.println("Player is dead collision with: " +
                        curBoard.getTile(xCoord, yCoord).getOccupyingCharacter());
                isPaused = true;
                tickTimeline.stop();
            }
        } else if (curBoard.getTile(xCoord, yCoord).getOccupyingItem() != null) {
            Item item = curBoard.getTile(xCoord, yCoord).getOccupyingItem();
            //System.out.println("Player collision with: " + item.getItemType() + " at " + xCoord + yCoord);
            if (item.isIS_COLLECTABLE()) {
                if (item.getItemType().equals("Loot")) {
                    Loot loot = (Loot) item;
                    curGameState.getThePlayer().increaseScore(loot.collect(curBoard, curGameState));
                } else if (item.getItemType().equals("Clock")) {
                    Clock clock = (Clock) item;
                    curGameState.increaseTimeLimit(clock.collect(curBoard, curGameState));
                } else if (item.getItemType().equals("Lever")) {
                    System.out.println("Lever Found");
                    Lever lever = (Lever) item;
                    lever.collect(curBoard, curGameState);
                    curBoard.getTile(xCoord, yCoord).setOccupyingItem(null);
                    curGameState.getItems().remove(lever);
                }
            } else if (item.getItemType().equals("Door")) {
                Door door = (Door) item;
                if (door.isOpen()) {
                    thePlayer.increaseScore(curGameState.getTimeLimit());
                    GameIOManager.addHighScore(curGameState);
                    thePlayer.getPlayerProfile().setLevelUnlocked(thePlayer.getPlayerProfile().getLevelUnlocked() + 1);
                    theProfile = thePlayer.getPlayerProfile();
                    GameIOManager.updatePlayerProfileLevelUnlock(theProfile);
                    System.out.println("Player won with score: " + thePlayer.getScore());
                    if (thePlayer.getPlayerProfile().getLevelUnlocked() < 10) {
                        loadNewBoard(curGameState.getLevelNum() + 1, curGameState.getThePlayer().getPlayerProfile().getName());
                    } else {
                        winPane.setVisible(true);
                        tickTimeline.pause();
                        System.out.println("You win");
                        tickTimeline.pause();
                        boardPane.setVisible(false);
                        isPaused = true;

                    }
                }
            }
        }
    }


    /**
     * Checks if player is near a bomb
     *
     * @param xCoord X-axis location of player
     * @param yCoord Y-axis location of player
     */
    private void bombSweep(int xCoord, int yCoord) {
        Bomb bomb = null;
        try {
            if (curBoard.getTile(xCoord, yCoord - 1)
                    .getOccupyingItem().getEntityName().equals("Bomb")) {
                System.out.println("BOMB FOUND");
                bomb = (Bomb) curBoard.getTile(xCoord, yCoord - 1)
                        .getOccupyingItem();
            }
        } catch (Exception e) {
            System.out.print("");
        }
        try {
            if (curBoard.getTile(xCoord + 1, yCoord - 1)
                    .getOccupyingItem().getEntityName().equals("Bomb")) {
                System.out.println("BOMB FOUND");
                bomb = (Bomb) curBoard.getTile(xCoord + 1, yCoord - 1)
                        .getOccupyingItem();
            }
        } catch (Exception e) {
            System.out.print("");
        }
        try {
            if (curBoard.getTile(xCoord + 1, yCoord)
                    .getOccupyingItem().getEntityName().equals("Bomb")) {
                System.out.println("BOMB FOUND");
                bomb = (Bomb) curBoard.getTile(xCoord + 1, yCoord)
                        .getOccupyingItem();
            }
        } catch (Exception e) {
            System.out.print("");
        }
        try {
            if (curBoard.getTile(xCoord + 1, yCoord + 1)
                    .getOccupyingItem().getEntityName().equals("Bomb")) {
                System.out.println("BOMB FOUND");
                bomb = (Bomb) curBoard.getTile(xCoord + 1, yCoord + 1)
                        .getOccupyingItem();
            }
        } catch (Exception e) {
            System.out.print("");
        }
        try {
            if (curBoard.getTile(xCoord, yCoord + 1)
                    .getOccupyingItem().getEntityName().equals("Bomb")) {
                System.out.println("BOMB FOUND");
                bomb = (Bomb) curBoard.getTile(xCoord, yCoord + 1).getOccupyingItem();
            }
        } catch (Exception e) {
            System.out.print("");
        }
        try {
            if (curBoard.getTile(xCoord - 1, yCoord + 1)
                    .getOccupyingItem().getEntityName().equals("Bomb")) {
                System.out.println("BOMB FOUND");
                bomb = (Bomb) curBoard.getTile(xCoord - 1, yCoord + 1)
                        .getOccupyingItem();
            }
        } catch (Exception e) {
            System.out.print("");
        }
        try {
            if (curBoard.getTile(xCoord - 1, yCoord)
                    .getOccupyingItem().getEntityName().equals("Bomb")) {
                System.out.println("BOMB FOUND");
                bomb = (Bomb) curBoard.getTile(xCoord - 1, yCoord)
                        .getOccupyingItem();
            }
        } catch (Exception e) {
            System.out.print("");
        }
        try {
            if (curBoard.getTile(xCoord - 1, yCoord - 1)
                    .getOccupyingItem().getEntityName().equals("Bomb")) {
                System.out.println("BOMB FOUND");
                bomb = (Bomb) curBoard.getTile(xCoord - 1, yCoord - 1)
                        .getOccupyingItem();
            }
        } catch (Exception e) {
            System.out.print("");
        }
        if (bomb != null && !bomb.isArmed()) {
            bomb.bombHit(curGameState);
        }
    }

    /**
     * Pauses the game and displays either the pause menu or game over menu
     */
    private void handlePause() {
        tickTimeline.pause();
        System.out.println("pause");
        tickTimeline.pause();
        boardPane.setVisible(false);
        if (!playerDead) {
            pauseMenuPane.setVisible(true);
            pauseMenuPane.setDisable(false);
        } else {
            gameOverPane.setVisible(true);
            gameOverPane.setDisable(false);
        }
        isPaused = true;
    }

    /**
     * Unpauses the game
     */
    public void handleUnPause() {
        tickTimeline.pause();
        System.out.println("play");
        tickTimeline.play();
        boardPane.setVisible(true);
        pauseMenuPane.setVisible(false);
        pauseMenuPane.setDisable(true);
        isPaused = false;
    }

    /**
     * Ends tick timeline and calls pause
     */
    private void gameOver() {
        System.out.println("The end!(Add 'Game over' screen)");
        playerDead = true;
        tickTimeline.stop();
        handlePause();
    }

    /**
     * Displays the current scene
     */
    private void displayScene() {
        primaryStage.setScene(scene);
        primaryStage.setTitle(MessageOfTheDay.getMessageOfTheDay());
        primaryStage.show();
    }

    /**
     * Initialises the menus. Setting main menu as the only visible/active
     */
    public void displayMainMenu() {
        try {
            isPaused = false;
            playerDead = false;
            mainMenuPane = new SplitPane();
            // Load the main scene.

            FXMLLoader fxmlloadMainMenu = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
            fxmlloadMainMenu.setRoot(mainMenuPane);
            mainMenuPane = (SplitPane) fxmlloadMainMenu.load();
            MenuController editControllerMainMenu = fxmlloadMainMenu.<MenuController>getController();
            editControllerMainMenu.setMain(this);

            pauseMenuPane = new Pane();
            // Load the Pause scene.
            FXMLLoader fxmlPauseLoad = new FXMLLoader(getClass().getResource("/pauseMenu.fxml"));
            //fxmlPauseLoad.setRoot(subMenuPane);
            pauseMenuPane = (Pane) fxmlPauseLoad.load();
            pauseMenuPane.setVisible(false);
            PauseMenuController editControllerPauseMenu = fxmlPauseLoad.<PauseMenuController>getController();
            editControllerPauseMenu.setMain(this);

            highScoreTable = new BorderPane();
            FXMLLoader fxmlHighScoreLoader = new FXMLLoader(getClass().getResource("/highScoreTable.fxml"));
            highScoreTable = (BorderPane) fxmlHighScoreLoader.load();
            HighScoreController editControllerHighScore = fxmlHighScoreLoader.<HighScoreController>getController();
            editControllerHighScore.setMain(this);
            highScoreTable.setVisible(false);


            // Load the PlayerProfile scene.
            playerProfilePane = new BorderPane();
            FXMLLoader fxmlPlayerProfileLoad = new FXMLLoader(getClass().getResource("/playerProfile.fxml"));
            playerProfilePane = (BorderPane) fxmlPlayerProfileLoad.load();
            playerProfilePane.setVisible(false);
            PlayerProfileController editPlayerProfileMenu = fxmlPlayerProfileLoad.<PlayerProfileController>getController();
            editPlayerProfileMenu.setMain(this);

            gameOverPane = new BorderPane();
            // Load the Pause scene.
            FXMLLoader fxmlGameOverLoad = new FXMLLoader(getClass().getResource("/gameOverMenu.fxml"));
            gameOverPane = (BorderPane) fxmlGameOverLoad.load();
            gameOverPane.setVisible(false);
            PauseMenuController editGameOverPauseMenu = fxmlGameOverLoad.<PauseMenuController>getController();
            editGameOverPauseMenu.setMain(this);

            winPane = new BorderPane();
            // Load the Pause scene.
            FXMLLoader fxmlGWinLoad = new FXMLLoader(getClass().getResource("/winMenu.fxml"));
            winPane = (BorderPane) fxmlGWinLoad.load();
            winPane.setVisible(false);
            PauseMenuController editWineMenu = fxmlGWinLoad.<PauseMenuController>getController();
            editWineMenu.setMain(this);


            lstOfAllPanes = new StackPane(mainMenuPane, pauseMenuPane, highScoreTable, playerProfilePane, gameOverPane, winPane);

            scene = new Scene(lstOfAllPanes, WINDOW_WIDTH, WINDOW_HEIGHT);

            // Place the main scene on stage and show it.
            displayScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Restarts the level
     */
    public void reloadLevel() {
        handleUnPause();
        gameOverPane.setVisible(false);
        gameOverPane.setDisable(true);
        loadNewBoard(curGameState.getLevelNum(), thePlayer.getPlayerProfile().getName());
    }

    /**
     * @return root of all UI elements
     */
    public BorderPane getRoot() {
        return root;
    }

    /**
     * @return the pane for the main menu
     */
    public SplitPane getMainMenuPane() {
        return mainMenuPane;
    }

    /**
     * @return Levels current GameState
     */
    public GameState getCurGameState() {
        return curGameState;
    }

    /**
     * @return High score menu
     */
    public BorderPane getHighScoreTable() {
        return highScoreTable;
    }

    /**
     * @return Player profile manager pane
     */
    public BorderPane getPlayerProfilePane() {
        return playerProfilePane;
    }

    /**
     * @return Current scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * @param scene New Scene
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * @return Current players profile
     */
    public PlayerProfile getTheProfile() {
        return theProfile;
    }

    /**
     * @param theProfile Players profile
     */
    public void setTheProfile(PlayerProfile theProfile) {
        this.theProfile = theProfile;
    }
}