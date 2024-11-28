package com.example.cs230gamewithjavafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;

    /**
     * Controls the Main menu of the game
     * @author Emily Hunt
     * @version 1.0.0
     *
     */
public class MenuController {

        /**
         * no longer used button to play the first level
         */
    @FXML
    private Button playButton;

        /**
         * delete a player profile from a selection list
         */
    @FXML
    private ChoiceBox deletePP;

        /**
         * no longer used, used to take to a seperate player profile page
         */
    @FXML
    private Button playerProfileButton;

    /**
     * Goes to the highscore table page
     */
    @FXML
    private Button highScoreButton;

        /**
         * closes the application
         */
    @FXML
    private Button quitButton;

        /**
         * the Pane everything is placed on
         */
    @FXML
    private Pane menuPane;

        /**
         * the imageview for the level1 thumbnail
         */
    @FXML
    private ImageView level1;

        /**
         * combo box to either choose an exisiting PP or type a new one
         */
    @FXML
    private ComboBox chooseOrAddPP;

        /**
         * error text for if you havent selected a PP or unlocked the level
         */
    @FXML
    private Text errorText;

        /**
         * the list of existing PPs
         */
    private ArrayList<PlayerProfile> thePlayerProfiles;

        /**
         * no longer used
         */
    private Label welcomeText;
        /**
         * the Main class for refrence and to access data
         */
    private Main main;

        /**
         * @param main set the main class the Controller needs to access for the data it displays
         */
    public void setMain(Main main) {
        this.main = main;
    }


        /**
         * creates the Menu Pane, implementing the utility for the GUI interactions
         */
    @FXML
    public void initialize() {
        // Set up the menu

        deletePP.getItems().addAll(GameIOManager.getPlayerProfileListFileAsArray());
        deletePP.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int arrayIndex = (int) observableValue.getValue();
                if (arrayIndex < 0) {
                    return;
                }
                PlayerProfile ppToDelete = GameIOManager.getPlayerProfileListFileAsArray().get(arrayIndex);
                GameIOManager.deletePlayerProfile(ppToDelete.getName());
                deletePP.getItems().clear();
                deletePP.getItems().addAll(GameIOManager.getPlayerProfileListFileAsArray());

                chooseOrAddPP.getItems().clear();
                chooseOrAddPP.getItems().addAll(GameIOManager.getPlayerProfileListFileAsArray());
            }
        });

        chooseOrAddPP.setEditable(true);
        thePlayerProfiles = GameIOManager.getPlayerProfileListFileAsArray();
        chooseOrAddPP.getItems().addAll(thePlayerProfiles);  //may cause an issue with deleting profiles then returning to menu as is data duplication

        EventHandler<ActionEvent> comboBoxEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                //System.out.println(chooseOrAddPP.getValue().getClass());
                if (chooseOrAddPP.getValue() instanceof PlayerProfile) {
                    main.setTheProfile((PlayerProfile) chooseOrAddPP.getValue());
                } else {

                    String ppName = (String) chooseOrAddPP.getValue();

                    if (!ppName.isBlank()) {
                        PlayerProfile pp;
                        if (ppName.contains(", level: ")) {
                            int ppNumber = Integer.parseInt(ppName.substring(ppName.indexOf(", level: ") + 9));
                            ppName = ppName.substring(0, ppName.indexOf(", level: "));
                            pp = new PlayerProfile(ppName, ppNumber);
                            final String ppNameFinal = ppName;
                            if (thePlayerProfiles.stream().anyMatch(a -> a.getName().equals(ppNameFinal))) {
                                return;
                            }
                        } else {
                            // System.out.println(ppName);
                            pp = new PlayerProfile(ppName, 1);
                        }
                        main.setTheProfile(pp);
                        GameIOManager.savePlayerProfile(pp);
                    }
                }
            }
        };


        chooseOrAddPP.setOnAction(comboBoxEvent);

        quitButton.setOnAction(e -> {
            handleQuitButtonAction();
        });

        /*
        playButton.setOnAction(e -> {
            handlePlayButtonAction();
        });

         */
/*
        playerProfileButton.setOnAction(e -> {
            handlePlayerProfileButtonAction();
        });


 */
        highScoreButton.setOnAction(e -> {
            handleHighScoreButtonAction();
        });

        level1.setOnMouseClicked(e -> {
            handleChooseLevel(e);
        });


    }

        /**
         * quit the application
         */
    @FXML
    private void handleQuitButtonAction() {
        Stage stage = (Stage) menuPane.getScene().getWindow();
        stage.close();
    }

        /**
         * play the first level, no longer used as didnt ensure PP selection
         */
    @FXML @Deprecated
    private void handlePlayButtonAction() {
        main.getMainMenuPane().setVisible(false);
        main.getMainMenuPane().setDisable(true);
        PlayerProfile pp = main.getTheProfile();
        main.loadNewBoard(pp.getLevelUnlocked(), pp.getName());
        System.out.print("");
    }

        /**
         * opens the chosen level if allowed to play otherwise writes error message
         * @param event the level button selected
         */
    @FXML
    void handleChooseLevel(MouseEvent event) {
        try {
            for (int i = 0; i < main.getTheProfile().getLevelUnlocked(); i++) {
                if (((ImageView) (event.getSource())).getId().equals("level" + (i + 1))) {
                    System.out.println("Level chosen : Level" + (i + 1));
                    main.getMainMenuPane().setVisible(false);
                    main.getMainMenuPane().setDisable(true);
                    main.loadNewBoard(i + 1, main.getTheProfile().getName());
                    errorText.setText("");
                } else {
                    errorText.setText("LEVEL NOT UNLOCKED YET");
                }
            }
        } catch (Exception e) {
            errorText.setText("PLEASE CHOOSE OR CREATE A PROFILE");
        }
    }

        /**
         * opens the Player Profile menu page
         */
    @FXML @Deprecated
    private void handlePlayerProfileButtonAction() {
        System.out.print("Player Profile");
        main.getMainMenuPane().setVisible(false);
        main.getPlayerProfilePane().setVisible(true);

    }

        /**
         * opens the highscore table page
         */
    @FXML
    private void handleHighScoreButtonAction() {
        System.out.print("High Score");
        main.getMainMenuPane().setVisible(false);
        main.getHighScoreTable().setVisible(true);

        // main.ScoreTable();
        // scorePage.ScoreTable(Stage primaryStage);
    }
}

