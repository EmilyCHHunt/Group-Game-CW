package com.example.cs230gamewithjavafx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 *  GameIOManager deals with data persistence, managing the games file handling, inputting and outputting data
 *   for the save features, Highscore tracking and Player Profile.
 * @author Joseph Dawson
 * @version 1.0.0
 */
public class GameIOManager {
    /**
     * location of where levels are saved
     */
    private static final String SAVE_DIRECTORY_PATH =
        "CS230GameWithJavaFX/src/main/resources/";
    /**
     * location of the highscore txt file
     */
    private static final String HIGHSCORE_FILE =
        "CS230GameWithJavaFX/src/main/resources/highscores.txt";
    /**
     * location of the player profile txt save file
     */
    private static final String PLAYER_PROFILE_FILE =
            "CS230GameWithJavaFX/src/main/resources/PlayerProfiles.txt";
    /**
     * the size of a cell on the game board
     */
    private static final int GRID_CELL_WIDTH = 50;

    /**
     * The method to save a game level mid play-through
     * so that you can return to it later.
     *
     * Names files in the format "levelXPlayerName.txt" where X is the level number
     * and PlayerName is the player profile name.
     *
     * Writes files in accordance with the design outlined in the partial design document.
     *
     * @param game the state of the game at the moment we want to save it.

     * @return true or false based on if the save was a success or not.
     */
    public static boolean saveGame(GameState game) {
        String saveName = "level" + game.getLevelNum() + game.getThePlayer().getPlayerProfile().getName() + ".txt";
        File saveFile = new File(SAVE_DIRECTORY_PATH + saveName );
        try {
            if (saveFile.createNewFile()) {
                System.out.println("File created: " + saveFile.getName());
            } else {
                System.out.println("File already exists.");
            }

            FileWriter out = new FileWriter(saveFile);

            out.write(game.getTimeLimit() + "\n");
            out.write(game.getTheBoard().getBoardMaxX() + " " + game.getTheBoard().getBoardMaxY() + "\n");


            //writes board tile colours
            //wrong way around atm
            for (int i = 0; i < game.getTheBoard().getBoardMaxY(); i++) {
                for (int j = 0; j < game.getTheBoard().getBoardMaxX(); j++) {
                    out.write(game.getTheBoard().getTile(j, i).getColour() + " ");
                }
                out.write("\n");
            }

            //writes items information
            out.write(game.getItems().size() + "\n");
            for (int i = 0; i < game.getItems().size(); i++) {
                out.write(game.getItems().get(i).getStringForFile());
                out.write("\n");
            }

            //writes NPCs information
            out.write(game.getNPCs().size() + "\n");
            for (int i = 0; i < game.getNPCs().size(); i++) {
                out.write(game.getNPCs().get(i).getStringForFile());
                out.write("\n");
            }

            //writes the player profile information, i.e. name
            out.write(game.getThePlayer().getStringForFile());

            out.close();
            return true;
        } catch (IOException e) {
            System.out.println("save file creation error");
        }
        return false;
    }


    /**
     * Loads a game into the class GameState which can then
     * interact with other classes to make a playable level.
     *
     * looks for the levels with the player username first to return to a paused level.
     *
     * @return a GameState complete with the data to make a playable level.
     */
    public static GameState loadGame(int levelNum, String playerName) {
        try {
            File file;
            String fileNamePPExists = "level" + levelNum + playerName + ".txt";
            String fileNameNoPP = "level" + levelNum + ".txt";
            if ( new File(getSAVE_DIRECTORY_PATH() + fileNamePPExists).exists()) {
                file = new File(getSAVE_DIRECTORY_PATH() + fileNamePPExists);
            } else {
                file = new File(getSAVE_DIRECTORY_PATH() + fileNameNoPP);
            }
            Scanner in = new Scanner(file);

            int timeLimit = in.nextInt();
            int mapX = in.nextInt();
            int mapY = in.nextInt();

            //Board colours
            Board theBoard = new Board(mapX, mapY);
            for (int i = 0; i < mapY; i++) {
                for (int j = 0; j < mapX; j++) {
                    theBoard.setTile(j, i, in.next(), GRID_CELL_WIDTH);
                }
                in.nextLine();
            }

            // all  items
            ArrayList<Item> items = loadItems(in, theBoard);

            // all NPCs
            ArrayList<NPC> NPCs = loadNPCs(in, theBoard);


            String[] ppTemp = in.nextLine().split(" ");
            String[] coords = ppTemp[1].split(",");

            int playerX = Integer.parseInt(coords[0]);
            int playerY = Integer.parseInt(coords[1]);

            //need to pass in a player profile at somepoint but still working on this

            Player player = new Player(playerX, playerY, new PlayerProfile(playerName, levelNum));

            in.close();
            return new GameState(timeLimit, theBoard,
                    items, NPCs, player, levelNum);

        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found");
        }

        //not good form but means if it doesn't work the code will break
        return null;
    }

    /**
     * Used to load and create items in the loadGame method
     * @param in file scanner
     * @param theBoard used to place items onto the board
     * @return ArrayList of Item to store when playing the map
     */
    private static ArrayList<Item> loadItems (Scanner in, Board theBoard) {
        int numItems = in.nextInt();
        in.nextLine();
        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            String itemType = in.next();

            String[] details;
            //for some disgusting reason the scanner creates
            //the first location as just an empty string
            details = in.nextLine().substring(1).split(" ");
            String[] coords = details[0].split(",");


            switch (itemType) {
                case "Clock" -> {
                    items.add(new Clock(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]),
                            Integer.parseInt(details[1])));
                }
                case "Loot" -> {
                    items.add(new Loot(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]),
                            Integer.parseInt(details[1])));
                }
                case "Bomb" -> {
                    items.add(new Bomb(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]),
                            Integer.parseInt(details[1])));
                }
                case "Lever" -> {
                    items.add(new Lever(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]),
                            details[1]));
                }
                case "Gate" -> {
                    items.add(new Gate(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]),
                            Boolean.parseBoolean(details[1]), details[2]));
                }
                case "Door" -> {
                    items.add(new Door(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]),
                            Boolean.parseBoolean(details[1])));
                }
                default -> System.out.println("unidentified item");
            }
            theBoard.getTile(items.get(i).getxCoord(),items.get(i).getyCoord()).setOccupyingItem(items.get(i));
        }
        return items;
    }

    /**
     * loads, creates and places NPCs on the map
     * @param in file scanner
     * @param theBoard used to place the NPCs on the board
     * @return the list of NPCs on the board
     */
    private static ArrayList<NPC> loadNPCs(Scanner in, Board theBoard) {
        int numNPCs = in.nextInt();
        in.nextLine();
        ArrayList<NPC> NPCs = new ArrayList<>();
        for (int i = 0; i < numNPCs; i++) {
            String npcType = in.next();
            String[] details;
            //for some disgusting reason the scanner creates
            //the first location as just an empty string
            details = in.nextLine().split(" ");
            String[] coords = details[1].split(",");
            switch (npcType) {
                case "SmartThief" -> {
                    NPCs.add(new SmartThief(
                            Integer.parseInt(coords[0]),
                            Integer.parseInt(coords[1]),
                            details[2]));
                }
                case "FloorFollowingThief" -> {
                    NPCs.add(new FloorFollowingThief(
                            Integer.parseInt(coords[0]),
                            Integer.parseInt(coords[1]),
                            details[2], details[3]));
                }
                case "FlyingAssassin" -> {
                    NPCs.add(new FlyingAssassin(
                            Integer.parseInt(coords[0]),
                            Integer.parseInt(coords[1]),
                            details[2]));
                }
                default -> System.out.println("unidentified NPC");
            }
            theBoard.getTile(NPCs.get(i).getxCoord(),NPCs.get(i).getyCoord()).setOccupyingCharacter(NPCs.get(i));
        }
        return NPCs;
    }


    /**
     * adds a single HighScore to the txt file if it is valid,
     * otherwise just discards it
     * @param curGameState the gameState that includes the information required to make a highscore
     */
    public static void addHighScore(GameState curGameState) {
        PlayerProfile pp = curGameState.getThePlayer().getPlayerProfile();

        int score = curGameState.getThePlayer().getScore();
        HighScore highScoreCandidate = new HighScore(curGameState.getLevelNum(), pp.getName(), score);
        ArrayList<HighScore> allScores = readHighScores();
        allScores.add(highScoreCandidate);
        writeHighScores(allScores);

    }

    /**
     * read the highscores from a txt file.
     * @return an ArrayList of Highscores for that level
     */
    public static ArrayList<HighScore> readHighScores() {
        try {
            File file = new File(getHIGHSCORE_FILE_PATH());
            Scanner in = new Scanner(file);

            ArrayList<HighScore> highScores = new ArrayList<>();
            String playerName;
            int levelName;
            int playerScore;

            while (in.hasNextLine()) {
                levelName = in.nextInt();
                in.nextLine();
                playerName = in.nextLine();
                playerScore = in.nextInt();
                if (in.hasNextLine()) {
                    in.nextLine();
                }

                highScores.add(new HighScore(levelName, playerName, playerScore));
            }
            return highScores;

        } catch (java.io.FileNotFoundException e) {
            System.out.println("Highscore file not found");
        }

        return null;
    }

    /**
     * writes the top 10 highscores per level to a txt file, sorted by level number and score
     * @param theScores the scores and names to be written into a txt file
     */
    public static void writeHighScores(ArrayList<HighScore> theScores) {
        sortScore(theScores);
        File file = new File(getHIGHSCORE_FILE_PATH());

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("highscore file already exists.");
            }
            FileWriter out = new FileWriter(file);

            for (HighScore score : theScores) {
                out.write(score.getLevelNum() + "\n");
                out.write(score.getName() + "\n");
                out.write(score.getScore() + "\n");
            }

            out.close();
        } catch (IOException e) {
            System.out.println("highscore creation file error");
        }

    }

    /**
     * sorts the list of highscores, with highest level first, then highest score
     * with number per level limited to max 10.
     * @param theScores the list of scores to sort by order.
     */
    private static void sortScore(ArrayList<HighScore> theScores) {
        //orders the entire list by level number and then by score
        theScores.sort(Collections.reverseOrder());

       /*
        checks a score and  the score 10 indexes away, if they are the same level
        then more than 10 scores are being stored for that level where it removes the lowest
        scores until there is only the 10 needed per level.

        Efficiency can be improved as there are a lot of redundant checks, oh well
        */
        for (int i = 0; i < theScores.size(); i++) {
            while (theScores.size() > i+10
                    && theScores.get(i).getLevelNum() == theScores.get(i + 10).getLevelNum()) {
                theScores.remove(i + 10);
            }
        }

    }

    /**
     * updates the txt file that stores player profiles, used when a new maxLevel is achieved
     * @param ppToUpdate the player profile being used that needs updating
     */
    public static void updatePlayerProfileLevelUnlock(PlayerProfile ppToUpdate) {
        ArrayList<PlayerProfile> allPlayers = getPlayerProfileListFileAsArray();
        allPlayers.removeIf(pp -> pp.getName().equals(ppToUpdate.getName()));
        clearAllProfiles();
        for (PlayerProfile pp: allPlayers) {
            savePlayerProfile(pp);
        }
        savePlayerProfile(ppToUpdate);
    }

    /**
     * gets the player profiles that have been saved as an Array of PlayerProfile
     * @return the list of all PlayerProfiles as an ArrayList
     */
    public static ArrayList<PlayerProfile> getPlayerProfileListFileAsArray() {
        try {
            File file = new File(getPlayerProfileFile());
            Scanner in = new Scanner(file);
            String playerName;
            int playerLevel;
            ArrayList<PlayerProfile> lstOfPlayerProfiles = new ArrayList<>();
            while (in.hasNextLine()) {
                playerName = in.nextLine();
                playerLevel = in.nextInt();
                if (in.hasNextLine()){
                    in.nextLine();
                }

                lstOfPlayerProfiles.add(new PlayerProfile(playerName, playerLevel));
            }

            return lstOfPlayerProfiles;
        } catch (FileNotFoundException e) {
            System.out.println("playerprofile text file not found");
            e.printStackTrace();
        }

        return new ArrayList<PlayerProfile>();
    }

    /**
     * deletes a player profile from the txt file.
     * @param playerName the player to delete
     */
    public static void deletePlayerProfile(String playerName) {
        ArrayList<PlayerProfile> allPlayers = getPlayerProfileListFileAsArray();
        allPlayers.removeIf(pp -> pp.getName().equals(playerName));
        clearAllProfiles();
        for (PlayerProfile pp: allPlayers) {
            savePlayerProfile(pp);
        }
    }

    /**
     * loads a player profile to play the game with
     * @param playerName the name of the player you want to load
     * @return the loaded player profile or a new player profile with the name and starting unlocks
     */
    public static PlayerProfile loadPlayerProfile(String playerName) {
        ArrayList<PlayerProfile> playerProfileArray = getPlayerProfileListFileAsArray();
        for (PlayerProfile playerProfile:
                playerProfileArray) {
            if (playerProfile.getName().equals(playerName)) {
                return playerProfile;
            } else {
                System.out.println("new player created");
                return new PlayerProfile(playerName, 1);
            }

        }
        System.out.println("No profiles yet");
        return new PlayerProfile(playerName, 1);

    }

    /**
     * Clears the txt file of all profiles
     * normally used to re-write the list back onto a fresh file
     */
    private static void clearAllProfiles() {
        File saveFile = new File(PLAYER_PROFILE_FILE);
        try {
            if (saveFile.createNewFile()) {
                System.out.println("File created: " + saveFile.getName());
            } else {
                System.out.println("player profile file already exists.");
            }

            FileWriter out = new FileWriter(saveFile);
            out.write("");

            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * saves a new PlayerProfile to the bottom of the txt file.
     * @param playerProfile the profile to save to the txt file
     */
    public static void savePlayerProfile(PlayerProfile playerProfile) {
        File saveFile = new File(PLAYER_PROFILE_FILE);
        try {
            if (saveFile.createNewFile()) {
                System.out.println("File created: " + saveFile.getName());
            } else {
                System.out.println("player profile file already exists.");
            }

            FileWriter out = new FileWriter(saveFile, true);

            out.write(playerProfile.getName() + "\n");
            out.write(playerProfile.getLevelUnlocked() + "\n");

            out.close();
        } catch (IOException e) {
            System.out.println("playerProfile save error");
            e.printStackTrace();
        }
    }

    /**
     * @return the HIGHSCORE_FILE_PATH
     */
    private static String getHIGHSCORE_FILE_PATH() {
        return HIGHSCORE_FILE;
    }

    /**
     * @return the SAVE_DIRECTORY_PATH
     */
    private static String getSAVE_DIRECTORY_PATH() {
        return SAVE_DIRECTORY_PATH;
    }

    /**
     * @return the PLAYER_PROFILE_FILE path
     */
    private static String getPlayerProfileFile() {
        return PLAYER_PROFILE_FILE;
    }
}
