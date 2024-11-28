package com.example.cs230gamewithjavafx;

/**
 * Supper class of items for use on board.
 * @author Euan Phillips
 * @version 1.0
 */
public abstract class Item extends Entity {
    protected boolean IS_COLLECTABLE;
    /**
     * @param xCoord place on X-axis of board
     * @param yCoord place on Y-axis of board
     */
    public Item (String entityName, int xCoord, int yCoord) {
        super(entityName, xCoord, yCoord);
        this.IS_COLLECTABLE = false;
    }

    /**
     * Sets image based on colour
     * @param colour the colour to change to
     */
    protected abstract void setImageItem(String colour);

    /**
     * String information of item to write to a file
     * @return string to pass to a file
     */
    public abstract String getStringForFile();

    /**
     * Gets item type
     * @return string of item type
     */
    public abstract String getItemType();

    /**
     * Checks if item is collectable
     * @return boolean. True if collectable, else false.
     */
    public boolean isIS_COLLECTABLE() {
        return IS_COLLECTABLE;
    }
}
