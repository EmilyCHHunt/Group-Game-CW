package com.example.cs230gamewithjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * the controller class of the pause menu
 * @author Upenyu and Joseph Dawson and Emily Hunt
 * @version 1.0.0
 *
 */
public class PauseMenuController {
    /**
     * resume playing the game
     */
    @FXML
    private Button resumeButton;
    /**
     * save the level to txt file
     */
    @FXML
    private Button saveButton;
    /**
     * reload the level back to the start
     */
    @FXML
    private Button reloadButton;
    /**
     * return to the main menu
     */
    @FXML
    private Button mainMenuButton;
    /**
     * exit the program
     */
    @FXML
    private Button exitButton;
    /**
     * the pane containing the menu
     */
    @FXML
    private Pane pausePane;

    /**
     * the programs main method
     */
    private Main main;

    /**
     * @param main the programs main method
     */
    public void setMain(Main main) {
        this.main = main;
    }

    /**
     * set up the button actions on creation of the pane
     */
    @FXML
    public void initialize() {
        // Set up the menu

        resumeButton.setOnAction(e -> {
            handleResumeButtonAction();
        });

        saveButton.setOnAction(e -> {
            handleSaveButtonAction();
        });

        mainMenuButton.setOnAction(e -> {
            handleMainMenuButtonAction();
        });

        exitButton.setOnAction(e -> {
                    handleExitButtonAction();
                }
        );
        reloadButton.setOnAction(e -> {
            handleReloadButton();
        });


    }

    /**
     * return back to playing the game
     */
    @FXML
    private void handleResumeButtonAction() {
        System.out.println("resume");
        main.handleUnPause();
    }

    /**
     * save the game in its current state to a txt file
     */
    @FXML
    private void handleSaveButtonAction() {
        System.out.println("save");
        GameIOManager.updatePlayerProfileLevelUnlock(main.getTheProfile());
        GameIOManager.saveGame(main.getCurGameState());
    }

    /**
     * return to the game main menu
     */
    @FXML
    private void handleMainMenuButtonAction() {
        System.out.println("main menu");
        GameIOManager.updatePlayerProfileLevelUnlock(main.getTheProfile());
        main.displayMainMenu();
        //main.setPaused(true);
    }

    /**
     * exit and close the program
     */
    @FXML
    private void handleExitButtonAction() {
        System.out.println("exit");
        Stage stage = (Stage) main.getRoot().getScene().getWindow();
        stage.close();
    }

    /**
     * reload the level from a save file
     */
    @FXML
    private void handleReloadButton() {
        main.reloadLevel();
    }
}

