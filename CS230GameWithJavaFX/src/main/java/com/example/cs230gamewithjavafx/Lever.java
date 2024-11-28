package com.example.cs230gamewithjavafx;

import javafx.scene.image.Image;

/**
 * When a lever is collected, corresponding gate/s are removed from board.
 * @author Euan Phillips
 * @version 1.0
 * Sublcass of CollectableItem.
 */
public class Lever extends CollectableItem {
    private String colour; //The colour of the lever, same colour as gate it opens.

    /**
     * Creates a lever
     * @param xCoord postion on gameBoard(X-axis)
     * @param yCoord postion on gameBoard(Y-axis)
     * @param colour colour of lever
     */
    public Lever(int xCoord, int yCoord, String colour) {
        super("Lever", xCoord, yCoord,0);
        this.colour = colour;
        setImageItem(colour);
    }

    /**
     * Sets the image of the lever to the correct colour
     * @param colour colour to set as
     */
    @Override
    protected void setImageItem(String colour){
        this.getImageView().setVisible(false);
        this.getImageView().setImage(new Image("file:CS230GameWithJavaFX/src/main/resources/Items/Lever"  + colour + ".png"));
        this.getImageView().setVisible(true);
    }

    /**
     * Collects Lever and sets gates of that levers colour
     * @param curBoard  board in play
     * @param game      game in play
     */
    public int collect(Board curBoard, GameState game) {
        //iterates over items list to find gates with same colour
        for (int i = 0; i < game.getItems().size(); i++) {
            if (game.getItems().get(i).getEntityName().equals("Gate")) {
                Gate gate = (Gate) game.getItems().get(i);
                if (gate.colour.equals(this.colour)) {
                    System.out.println("GATE FOUND!");
                    gate.getImageView().setVisible(false);
                    curBoard.getTile(gate.getxCoord(), gate.getyCoord()).setOccupyingItem(null);
                }
            }
        }
        game.removeItem(this);
        this.getImageView().setVisible(false);
        return 0;
    }

    /**
     * String information of lever to save in file
     * @return string that can be written to file
     */
    @Override
    public String getStringForFile() {
        return "Lever " + getxCoord() + "," + getyCoord() + " " + getColour();
    }

    /**
     *
     * @return the type of the item used for comparisons
     */
    @Override
    public String getItemType() {
        return "Lever";
    }

    /**
     * Gets colour of lever
     * @return colour
     */
    public String getColour() {
        return colour;
    }

    /**
     * Sets colour
     * @param colour of lever
     */
    public void setColour(String colour) {
        this.colour = colour;
    }


    @Override
    protected String setLootType(int lootValue) {
        return null;
    }

    @Override
    protected void setImageItem(int lootValue) {

    }
}