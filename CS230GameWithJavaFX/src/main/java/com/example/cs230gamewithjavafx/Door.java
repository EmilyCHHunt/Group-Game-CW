package com.example.cs230gamewithjavafx;
/**
 * If open and player lands on the door tile, game ends.
 * Door is only open if every leaver and loot is collected.
 * @author Euan Phillips
 * @version 1.0.0
 * Subclass of item
 */
public class Door extends Item {
    /**
     * indicates if the door is open and the game is ready to win
     */
    private boolean isOpen;

    /**
     *
     * @param xCoord doors x location
     * @param yCoord doors y location
     * @param isOpen to win the level or not
     */
    public Door(int xCoord, int yCoord, boolean isOpen){
        super("Door", xCoord, yCoord);
        this.isOpen = isOpen;
    }

    /**
     * @return if the door is open or not
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * @param open is the door open or not
     */
    public void setOpen(boolean open) {
        isOpen = open;
    }


    @Override
    protected void setImageItem(String colour) {

    }

    /**
     * String information of door to write to file
     * @return string that can be used to write to txt file
     */
    public String getStringForFile(){
        return "Door " + getxCoord() + "," + getyCoord() + " " + isOpen();
    }

    /**
     * @return the item type for comparison
     */
    @Override
    public String getItemType() {
        return "Door";
    }
}
