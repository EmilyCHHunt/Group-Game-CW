package com.example.cs230gamewithjavafx;

/**
 * Supper class of items that are collecteable
 * @author Euan Phillips
 * @version 1.0.0
 * Sublcass of Item.
 */
public abstract class CollectableItem extends Item {
    /**
     * indicates if the item has been collected to know to delete it
     */
    private boolean isCollected;
    /**
     * value of the item, particularly for loot
     */
    private int itemValue = 0;

    /** Constructs an item
     * @param entityName name used to display png on board
     * @param xCoord X-axis start point on board
     * @param yCoord Y-axis start point on board
     * @param itemValue value of item
     */
    public CollectableItem (String entityName, int xCoord, int yCoord, int itemValue){
        super(entityName, xCoord, yCoord);
        this.isCollected = false;
        this.IS_COLLECTABLE = true;
        this.itemValue = itemValue;
    }

    /**
     * Returns status of item
     * @return isCollected
     */
    public boolean getIsCollected() {
        return isCollected;
    }

    /**
     * Sets collected status
     * @param collected if the item has been picked up or not
     */
    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    protected abstract void setImageItem(String colour);

    /**
     * Collects item from the board
     * @param curBoard the current board in play
     * @param game the current game in play
     * @return value of item
     */
    public int collect(Board curBoard, GameState game) {
        curBoard.getTile(getxCoord(),getyCoord()).setOccupyingItem(null);
        this.getImageView().setVisible(false);
        return (itemValue);
    }

    /**
     * Sets value of item
     * @param itemValue value of item
     */
    public void setItemValue(int itemValue) {
        this.itemValue = itemValue;
    }

    /**
     * Gets value of item
     * @return value of item
     */
    public int getItemValue(){
        return itemValue;
    }

    /**
     * Sets loot type
     * @param lootValue value
     * @return String
     */
    protected abstract String setLootType(int lootValue);

    /**
     * Sets image of loot based on value
     * @param lootValue value of loot
     */
    protected abstract void setImageItem(int lootValue);
}