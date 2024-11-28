package com.example.cs230gamewithjavafx;

/**
 * If collected by a player, time is added
 * If Stolen, time is taken away.
 * @author Euan Phillips
 * @version 1.0.0
 * Subclass of CollectableItem.

 */
public class Clock extends CollectableItem{
    /**
     * amount of time the clock impacts
     */
    private int timeValue;

    /**
     * Creates a clock
     * @param xCoord X-axis start point
     * @param yCoord Y-axis start point
     * @param timeValue value of clock
     */
    public Clock(int xCoord, int yCoord, int timeValue){
        super("Clock",xCoord, yCoord,0);
        this.timeValue = timeValue;
    }

    @Override
    protected void setImageItem(String colour) {

    }

    /**
     * Adds time value of clock to game timer.
     * Sets collected to true.
     * @return Time Value of clock
     */
    @Override
    public int collect(Board curBoard, GameState game) {
        game.removeItem(this);
        curBoard.getTile(getxCoord(),getyCoord()).setOccupyingItem(null);
        setCollected(true);
        this.getImageView().setVisible(false);
        return getTimeValue();
    }

    @Override
    protected String setLootType(int lootValue) {
        return null;
    }

    @Override
    protected void setImageItem(int lootValue) {

    }

    /**
     * String information of clock
     * @return String
     */
    @Override
    public String getStringForFile() {
        return "Clock " + getxCoord() + "," + getyCoord() + " " + getTimeValue();
    }

    @Override
    public String getItemType() {
        return "Clock";
    }

    /**
     * Gets value of clock
     * @return timeValue
     */
    public int getTimeValue() {
        return timeValue;
    }

    /**
     * Sets value of clock
     * @param timeValue the amount of time the item adds to the clock
     */
    public void setTimeValue(int timeValue) {
        this.timeValue = timeValue;
    }

}
