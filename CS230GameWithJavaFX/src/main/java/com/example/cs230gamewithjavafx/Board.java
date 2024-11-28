package com.example.cs230gamewithjavafx;

/**
 * Board is where the current game level is stored
 * @author Emily Hunt
 * @version 0.5.0
 */
public class Board {
    private final Tile[][] theBoard;
    private final int boardMaxX;
    private final int boardMaxY;

    /**
     * @param boardMaxX The maximum X index of the board
     * @param boardMaxY The maximum Y index of the board
     */
    public Board(int boardMaxX, int boardMaxY) {
        this.boardMaxX = boardMaxX;
        this.boardMaxY = boardMaxY;
        this.theBoard = new Tile[boardMaxY][boardMaxX];
    }

    public Board(int boardMaxX, int boardMaxY, Tile[][] theBoardUI) {
        this.boardMaxX = boardMaxX;
        this.boardMaxY = boardMaxY;
        this.theBoard = theBoardUI;
    }

    /**
     * @param xLoc   X coordinate of tile
     * @param yLoc   Y coordinate of tile
     * @param colour Colour code of string
     */
    public void setTile(int xLoc, int yLoc, String colour, double width) {
        theBoard[yLoc][xLoc] = new Tile(colour, xLoc, yLoc, width);
    }

    /**
     * @param xLoc X coordinate of tile
     * @param yLoc Y coordinate of tile
     * @return the Tile at given coordinates
     */
    public Tile getTile(int xLoc, int yLoc) {
        try {
            return theBoard[yLoc][xLoc];
        } catch (Exception e) {
            System.out.print("");
            return null;
        }
    }

    /**
     * @return maximum x bound of board
     */
    public int getBoardMaxX() {
        return boardMaxX;
    }

    /**
     *
     * @return maximum y bound of board
     */
    public int getBoardMaxY() {
        return boardMaxY;
    }
}
