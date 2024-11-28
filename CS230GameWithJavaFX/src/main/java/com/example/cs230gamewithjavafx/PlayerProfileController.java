package com.example.cs230gamewithjavafx;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controls the menu page for the Player Profile management
 * @author Joseph Dawson
 * @version 1.0.0
 *
 */
public class PlayerProfileController {

    @FXML
    private ChoiceBox deletePP;

    @FXML
    private Button backButton;

    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }


    public void initialize(){
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

            }
        });

        backButton.setOnAction(e -> handleBackButtonAction());
    }

    @FXML
    private void handleBackButtonAction(){
        System.out.println("back to menu");
        main.displayMainMenu();
    }
}
