package com.example.cs230gamewithjavafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

/**
 * controller for the fxml pane that displays the highscore table
 * @author Sonali and Joseph Dawson
 * @version 0.8.0
 */
public class HighScoreController {
    /**
     * the table that will display the highscores
     */
    @FXML
    private TableView<HighScore> table;

    /**
     * the column to show what level the score was earnt on
     */
    @FXML
    private TableColumn<HighScore, Integer> levelColumn;

    /**
     * the column to show the players name
     */
    @FXML
    private TableColumn<HighScore, String> nameColumn;

    /**
     * the column to show what the score achieved was
     */
    @FXML
    private TableColumn<HighScore, Integer> scoreColumn;

    /**
     * button to return to menu
     */
    @FXML
    private Button backButton;

    @FXML
    private ChoiceBox levelSelect;

    /**
     * the main class
     */
    private Main main;

    /**
     * the list of highscores to display on the table
     */
    ObservableList<HighScore> list;

    /**
     * @param main the main method to pull data from
     */
    public void setMain(Main main){
        this.main = main;
    }


    /**
     * create the page and populate the table with data to display the highscores achieved
     */
    @FXML
    public void initialize() {
        ArrayList<HighScore> highscores = GameIOManager.readHighScores();
        list = FXCollections.observableArrayList();
        backButton.setOnAction(e -> handleBackButtonAction());

        levelSelect.getItems().addAll(1,2,3,4,5,6,7,8,9,10);
        levelSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int levelSelected = (int) observableValue.getValue() + 1;
                if (levelSelected < 0) {
                    return;
                }
                System.out.println(levelSelected);
                list.clear();
                for (HighScore score: highscores) {
                    if (score.getLevelNum() == levelSelected) {
                        list.add(score);
                    }
                }
                table.setItems( list);
            }
        });



        //scores are already ordered in level so will only add an explicit position column if we have time remaining
        //list = FXCollections.observableArrayList(highscores);
        levelColumn.setCellValueFactory(new PropertyValueFactory<HighScore, Integer>("levelNum"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<HighScore, String>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<HighScore, Integer>("score"));
        table.setItems(list);

    }

    /**
     * action to go back to the main menu
     */
    @FXML
    private void handleBackButtonAction(){
        System.out.println("back to menu");
        main.displayMainMenu();
    }
}
