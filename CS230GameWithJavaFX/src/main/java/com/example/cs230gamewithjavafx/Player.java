package com.example.cs230gamewithjavafx;

/**
 * The Player class responsible for the functions of the player,
 * including how it interacts with items or NPCs and moves around the board,
 * as well as storing player specific data
 * @author Emily Hunt
 * @version 1.0.0
 *
 */
public class Player extends Entity {
    /**
     * the profile of the user playing the game
     */
    private final PlayerProfile playerProfile;
    /**
     * the score the player has achieved so far on the current level
     */
    private int score;

    /**
     * Creates the player class with a generic player profile ready to be written over
     *
     * @param x x-axis location
     * @param y y-axis location
     */
    @Deprecated
    public Player(int x, int y) {
        super("Player", x, y);
        this.score = 0;
        this.playerProfile = new PlayerProfile("Undefined Name", 0);
    }

    /**
     * creates the player class with a specified profile
     *
     * @param x             x-axis location
     * @param y             y-axis location
     * @param playerProfile the profile of the user playing the game
     */
    public Player(int x, int y, PlayerProfile playerProfile) {
        super("Player", x, y);
        this.score = 0;
        this.playerProfile = playerProfile;
    }


    /**
     * looks for a valid movement colour when the player tries to move
     *
     * @param dir      direction trying to move
     * @param theBoard the game map
     * @return if the location is valid or not
     */
    public Boolean checkPlayerMoveColour(String dir, Board theBoard) {
        int checkX = getxCoord();
        int checkY = getyCoord();

        String[] curTileColours = theBoard.getTile(checkX, checkY).getColour().split("");
        while (checkX < theBoard.getBoardMaxX() && checkX >= 0 && checkY < theBoard.getBoardMaxY() && checkY >= 0) {
            switch (dir) {
                case "N" -> checkY -= 1;
                case "S" -> checkY += 1;
                case "E" -> checkX += 1;
                case "W" -> checkX -= 1;
                default -> System.out.println("Improper direction for Player: ");
            }
            Tile tileToCheck = theBoard.getTile(checkX, checkY);
            if (!(checkX < theBoard.getBoardMaxX() && checkX >= 0 && checkY < theBoard.getBoardMaxY() && checkY >= 0)) {
                return false;
            }
            String[] nextTileColours = tileToCheck.getColour().split("");

            for (String curColour : curTileColours) {
                for (String nextColour : nextTileColours) {
                    if (nextColour.equals(curColour)) {
                        if (tileToCheck.getOccupyingItem() != null) {
                            if (tileToCheck.getOccupyingItem().getEntityName().equals("Gate")
                                    || tileToCheck.getOccupyingItem().getEntityName().equals("Bomb")) {
                                return false;
                            } else if (tileToCheck.getOccupyingItem().getEntityName().equals("Door")) {
                                if (!((Door) tileToCheck.getOccupyingItem()).isOpen()) {
                                    return false;
                                }
                            }
                        }
                        if (tileToCheck.getOccupyingCharacter() != null) {
                            if (!tileToCheck.getOccupyingCharacter().getEntityName().equals("FlyingAssassin")) {
                                return false;
                            }
                        }
                        setxCoord(checkX);
                        setyCoord(checkY);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @return the players profile
     */
    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    /**
     * @return the score the player has earn't on the active level
     */
    public int getScore() {
        return score;
    }

    /**
     * increase the score the player has earn't
     *
     * @param score the amount to add onto the score
     */
    public void increaseScore(int score) {
        this.score += score;
    }

    /**
     * @return a string ready for formatting as a txt file on a level
     */
    public String getStringForFile() {
        return playerProfile.getName() + " " + getxCoord() + "," + getyCoord();
    }

}
