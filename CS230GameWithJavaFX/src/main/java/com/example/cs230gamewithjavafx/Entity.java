package com.example.cs230gamewithjavafx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * stores entity information like coordinates and the images
 * for NPCs, items and players that appear on the board
 * @author Joseph Dawson
 * @version 1.0.0
 */
public abstract class Entity {
    /**
     * the image file to use for the Imageview on the board
     */
    private Image image;
    /**
     * the imageview to make the image appear on the board
     */
    private ImageView imageView;
    /**
     * name of the entity
     */
    private String entityName;

    /**
     * entities x coordinate
     */
    private int xCoord;
    /**
     * entities y coordinate
     */
    private int yCoord;

    /**
     *
     * @param entityName the name of the entity, used to load the png
     * @param xCoord the x coordinate
     * @param yCoord the y coordinate
     */
    public Entity(String entityName, int xCoord, int yCoord) {
        this.entityName = entityName;
        this.image = new Image("file:CS230GameWithJavaFX/src/main/resources/Items/" + entityName + ".png");

        this.imageView = new ImageView(this.image);
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    /**
     * @return the image file
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image a new image file for the entity
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return the imageview of the entity to use on the board
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * @param imageView set a new Imageview for the entity
     */
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    /**
     * @return the name of the entity used for identification
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName the name of the entity used for identification
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @param dimensions size the imageview appears on the board
     */
    public void setFitHeightandWidth(int dimensions) {
        imageView.setFitHeight(dimensions);
        imageView.setFitWidth(dimensions);
    }

    /**
     * @return x coordinate for the board location
     */
    public int getxCoord() {
        return xCoord;
    }

    /**
     * @param xCoord the x-axis coordinate on the board
     */
    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    /**
     * @return the y-axis coordinate on the board
     */
    public int getyCoord() {
        return yCoord;
    }

    /**
     * @param yCoord set a new y-axis coordinate
     */
    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    /**
     * @param dir amount to change x-axis by
     */
    public void incrementX(int dir){
        xCoord += dir;
    }

    /**
     * @param dir amount to change y-axis by
     */
    public void incrementY(int dir){
        yCoord += dir;
    }
}
