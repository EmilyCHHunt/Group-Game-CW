package com.example.cs230gamewithjavafx;

import javafx.scene.image.Image;

/**
  Is a loot instance of item.
   * If collected by a player then lootValue is added to score.
 * @author Euan Phillips
 * @version 1.0
 * Subclass of CollectableItem.
 */
public class Loot extends CollectableItem {
    /**
     * Creates instance of loot
     * @param xCoord X-axis location of loot
     * @param yCoord Y-axis location of loot
     * @param lootValue value of loot
     */
    public Loot(int xCoord, int yCoord, int lootValue) {
        super("LootSapphire", xCoord, yCoord, lootValue);
        setImageItem(lootValue);
    }

    /**
     * Sets type of loot depending on loots value
     * @param lootValue value of loot
     * @return string
     */
        @Override
        protected String setLootType(int lootValue) {
            if (lootValue >= 1000) {
                return "LootDiamond";
            } else if (lootValue >= 500) {
                return "LootSapphire";
            } else if (lootValue >= 250) {
                return "LootRuby";
            } else if (lootValue >= 100) {
                return "LootEmerald";
            }
            return null;
        }

    /**
     * Removes default image of loot
     * Sets new image
     * Makes new image visible
     * @param lootValue value of loot
     */
    @Override
    protected void setImageItem(int lootValue){
        this.getImageView().setVisible(false);
        this.getImageView().setImage(new Image("file:CS230GameWithJavaFX/src/main/resources/Items/" + setLootType(lootValue) + ".png"));
        this.getImageView().setVisible(true);
    }

    /**
     * String information of loot
     * @return String
     */
    @Override
    public String getStringForFile() {
        return "Loot " + getxCoord() + "," + getyCoord() + " " + getItemValue();
    }

    @Override
    protected void setImageItem(String colour) {

    }

    /**
     * Collects loot item and adds loots value to player score
     * @param curBoard the current board in play
     * @param game the current game in play
     * @return value of loot
     */
    @Override
    public int collect(Board curBoard, GameState game) {
        game.removeItem(this);
        curBoard.getTile(getxCoord(),getyCoord()).setOccupyingItem(null);
        setCollected(true);
        this.getImageView().setVisible(false);
        return getItemValue();
    }

    /**
     * Gets item type
     * @return String
     */
    @Override
    public String getItemType() {
        return "Loot";
    }

}