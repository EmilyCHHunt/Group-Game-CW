package com.example.cs230gamewithjavafx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * The bomb item on the board, including its detonation
 * @author Euan
 * @version 1.0.0
 */

public class Bomb extends Item {
    /**
     * how many ticks till bomb explodes
     */
    private int timeToDetonate;
    /**
     * when the bomb was found to start the fuse countdown
     */
    private int timeFound;

    /**
     * loads the images used by the bomb
     */
    private Image[] images;
    /**
     * loads the image view for the bombs appearance
     */
    private ImageView[] imageViews;
    /**
     * indicates if the bomb is armed to explode or not
     */
    private boolean armed;
    /**
     * counts down to the detonation
     */
    private int detonateTick;

    /**
     * Creates the bomb
     *
     * @param xCoord place on X-axis of board
     * @param yCoord place on Y-axis of board
     * @param timeToDetonate how long the bomb has till it explodes
     */
    public Bomb(int xCoord, int yCoord, int timeToDetonate) {
        super("Bomb", xCoord, yCoord);
        this.timeToDetonate = timeToDetonate;
        this.timeFound = 0;
        this.armed = false;

        images = new Image[4];
        imageViews = new ImageView[4];

        for (int i = 0; i < 4; i++) {
            images[i] = new Image("file:CS230GameWithJavaFX/src/main/resources/Items/" + this.getEntityName() + i + ".png");
        }
    }

    /**
     * Gets time to detonate
     * @return time to detonate
     */
    public int getTimeToDetonate() {
        return timeToDetonate;
    }

    /**
     * Sets time to detonate
     * @param timeToDetonate time value
     */
    public void setTimeToDetonate(int timeToDetonate) {
        this.timeToDetonate = timeToDetonate;
    }

    /**
     * If a bomb is found for the first time,
     * time found is set,
     * the tick for detonation is set
     * and Set known
     *
     * @param curGame current game in play
     */
    public void bombHit(GameState curGame) {
        if (!isArmed()) {
            detonateTick = 0;
            setArmed(true);
        }
    }

    /**
     * @return which tick the bomb is blowing up on
     */
    public int getDetonateTick() {
        return detonateTick;
    }

    /**
     * slowly ticks to zero to specify when to explode
     */
    public void incrementDetonateTick() {
        if (3-detonateTick >= 0) {
            getImageView().setImage(images[3 - detonateTick]);
            this.detonateTick++;
        }else {
            getImageView().setImage(null);
        }
    }


    @Override
    protected void setImageItem(String colour) {

    }

    /**
     * String information of bomb to write to file
     * @return string that can be used to write to txt file
     */
    @Override
    public String getStringForFile() {
        return "Bomb " + getxCoord() + "," + getyCoord() + " " + getTimeToDetonate();
    }

    /**
     * Gets type of item for comparisons
     * @return String of item name
     */
    @Override
    public String getItemType() {
        return "Bomb";
    }

    /**
     * @return if the bomb is armed or not
     */
    public boolean isArmed() {
        return armed;
    }

    /**
     * @param armed boolean for if the bomb is armed or not
     */
    public void setArmed(boolean armed) {
        this.armed = armed;
    }


    /**
     * Gets rid of any collectable item on the board if
     * they are on the same X axis or Y axis.
     *
     * @param curGameState current game in play
     * @param curBoard     current board in play
     * @param bomb         the bomb to detonate
     * @return true if detonated, else false
     */
    public boolean detonate(GameState curGameState, Board curBoard, Bomb bomb) {
        System.out.println("BLOWING: " + bomb.getxCoord() + "," + bomb.getyCoord());
        if (!isArmed()) {
            setArmed(true);
            return false;
        } else {
            System.out.println("KABOOOOOM!");
            for (int i = 0; i < curGameState.getItems().size(); i++) {
                Item item = curGameState.getItems().get(i);
                if ((item.getxCoord() == bomb.getxCoord() || item.getyCoord() == bomb.getyCoord()) && item.IS_COLLECTABLE) {
                    System.out.println(item.getEntityName() +
                            " at: " + item.getxCoord() + "," + item.getyCoord() +
                            " destroyed");
                    curBoard.getTile(item.getxCoord(), item.getyCoord()).setOccupyingItem(null);
                    item.getImageView().setVisible(false);
                    curGameState.getItems().remove(item);
                } else if ((item.getxCoord() == bomb.getxCoord() || item.getyCoord() == bomb.getyCoord()) && item.getEntityName().equals("Bomb")) {
                    System.out.println("BANG^2");
                    System.out.println(item.getxCoord() + "," + item.getyCoord());
                    curGameState.getItems().remove(item);
                    curBoard.getTile(item.getxCoord(), item.getyCoord()).setOccupyingItem(null);
                    item.getImageView().setVisible(false);
                    detonate(curGameState, curBoard, (Bomb) item);
                }
            }
            return true;
        }
    }
}
