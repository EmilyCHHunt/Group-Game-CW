package com.example.cs230gamewithjavafx;

import java.util.ArrayList;
import java.util.Random;

/**
 * SmartThief is a subclass of NPC. It takes the shortest valid path to the loot or lever.
  * If it cannot reach the loot or lever than takes random valid path.
   * After collection of all loot and lever it moves to the nearest exit.
   * Moves comparatively slowly than the player.
 * @author Emily Hunt
 * @version 0.5.0
 */


public class SmartThief extends NPC {

    private ArrayList<Vertex> shortestPath = new ArrayList<>();
    private ArrayList<Item> collectableItemArrayList;
    private boolean checking;

    /**
     * @param xCoord    is the x coordinate of the smart thief.
     * @param yCoord    is the y coordinate of the smart thief.
     * @param direction is the direction in which smart thief will be facing.
     */
    public SmartThief(int xCoord, int yCoord, String direction) {
        super("SmartThief", xCoord, yCoord, direction);
    }

    /**
     * Tries moving the SmartThief and updates the UI for smart thief:
     * 1. If there are no valid routes to desired items it will move it randomly in a valid direction
     * 2. If there is a valid route it tries moving the npc on that route
     *
     * @param directionFace the direction in which the Assassin moves
     * @param theBoard      the current board
     * @param theBoardUI    the ui representation of the board
     * @param itemArrayList the ArrayList of all active items
     */
    @Override
    public void move(String directionFace, Board theBoard, Tile[][] theBoardUI, ArrayList<Item> itemArrayList) {
        if (shortestPath.size() == 0) {
            findLoot(itemArrayList);
            if (collectableItemArrayList.size() > 0) {
                findShortestPath(theBoard);
            }
        } else if (theBoard.getTile(shortestPath.get(shortestPath.size() - 1).getX(),
                (shortestPath.get(shortestPath.size() - 1).getY())).getOccupyingItem() == null) {
            findLoot(itemArrayList);
            findShortestPath(theBoard);
        }
        //System.out.println(itemArrayList);
        theBoard.getTile(getxCoord(), getyCoord()).setOccupyingCharacter(null);
        if (shortestPath.size() > 0) {
            theBoard.getTile(getxCoord(), getyCoord()).setOccupyingCharacter(null);

            if (!tryMove(theBoard, false)) {
                System.out.println("move failed");
            } else {
                shortestPath.remove(0);
            }
        } else {
            tryMove(theBoard, true);
        }
        theBoard.getTile(getxCoord(), getyCoord()).setOccupyingCharacter(null);
        this.getImageView().setX(theBoardUI[0][0].getTranslateX() + getxCoord() * 50);
        this.getImageView().setY(theBoardUI[0][0].getTranslateY() + getyCoord() * 50);
    }

    /**
     * Unused method inherited from parent.
     *
     * @param directionFace the direction in which the Assassin moves
     * @param theBoard      the current board
     * @param theBoardUI    the ui representation of the board
     */
    @Override
    public void move(String directionFace, Board theBoard, Tile[][] theBoardUI, GameState curGameState) {

    }

    /**
     * Searches itemArrayList for any desired items and adds them to collectableItemArrayList
     *
     * @param itemArrayList ArrayList of all items on the board
     */
    private void findLoot(ArrayList<Item> itemArrayList) {
        ArrayList<Item> tempItem = new ArrayList<>();
        for (int i = 0; i < itemArrayList.size(); i++) {
            String itemType = itemArrayList.get(i).getItemType();
            if (itemType.equals("Loot") || itemType.equals("Clock")
                    || itemType.equals("Lever") || itemType.equals("Door")) {
                tempItem.add(itemArrayList.get(i));
            }
        }
        collectableItemArrayList = tempItem;
    }

    /**
     * Tries to move the NPC along given path if movement is not random
     * If movement is random a random direction is chosen and the game checks if it valid
     * If it isn't then it calls tryMove again repeaeting this until movement is valid
     * If movement isn't random tiles in direction are checked until there's a valid one or the edge is reached
     *
     * @param theBoard the current level board
     * @param isRandom whether movement is random
     * @return Whether the movement was successful of not
     */
    private boolean tryMove(Board theBoard, boolean isRandom) {
        int checkX = getxCoord();
        int checkY = getyCoord();
        String[] possibleDir = {"N", "E", "S", "W"};
        String[] curTileColours = theBoard.getTile(checkX, checkY).getColour().split("");
        while (checkX < theBoard.getBoardMaxX() && checkX >= 0 && checkY < theBoard.getBoardMaxY() && checkY >= 0) {
            String direction;
            if (isRandom) {
                direction = possibleDir[new Random().nextInt(possibleDir.length)];
            } else {
                direction = shortestPath.get(0).getDirectionFromParent();
            }
            switch (direction) {
                case "N" -> checkY -= 1;
                case "S" -> checkY += 1;
                case "E" -> checkX += 1;
                case "W" -> checkX -= 1;
                default -> System.out.print("Improper direction for Smart Thief");
            }
            if (!isRandom) {
                if (!(checkX < theBoard.getBoardMaxX() && checkX >= 0 && checkY < theBoard.getBoardMaxY() && checkY >= 0)) {
                    return false;
                }
                if (shortestPath.get(0).getX() == checkX && shortestPath.get(0).getY() == checkY) {
                    if (theBoard.getTile(checkX, checkY).getOccupyingCharacter() == null && !theBoard.getTile(checkX, checkY).doesHoldPlayer()) {
                        setxCoord(checkX);
                        setyCoord(checkY);
                        return true;
                    }
                }
            } else {
                if (checkX < theBoard.getBoardMaxX() && checkX >= 0 && checkY < theBoard.getBoardMaxY() && checkY >= 0) {
                    Tile tileToCheck = theBoard.getTile(checkX, checkY);
                    String[] nextTileColours = tileToCheck.getColour().split("");

                    for (String curColour : curTileColours) {
                        for (String nextColour : nextTileColours) {
                            if (nextColour.equals(curColour)) {
                                if (tileToCheck.getOccupyingItem() != null) {
                                    if (tileToCheck.getOccupyingItem().getEntityName().equals("Gate")
                                            || tileToCheck.getOccupyingItem().getEntityName().equals("Bomb")) {
                                        return tryMove(theBoard, true);
                                    } else if (tileToCheck.getOccupyingItem().getEntityName().equals("Door")) {
                                        if (!((Door) tileToCheck.getOccupyingItem()).isOpen()) {
                                            return tryMove(theBoard, true);
                                        }
                                    }
                                }
                                if (tileToCheck.getOccupyingCharacter() != null) {
                                    return tryMove(theBoard, true);
                                }
                                if (tileToCheck.doesHoldPlayer()) {
                                    return tryMove(theBoard, true);
                                }
                                setxCoord(checkX);
                                setyCoord(checkY);
                                return true;
                            }
                        }
                    }
                } else {
                    return tryMove(theBoard, true);
                }
            }
        }
        return false;
    }

    /**
     * @return The smart thief as a string formatted for being saved
     */
    @Override
    public String getStringForFile() {
        return "SmartThief " + getxCoord() + "," + getyCoord() + " " + getDirection();
    }

    /**
     * @return Type of NPC
     */
    @Override
    public String getNPCType() {
        return "SmartThief";
    }

    /**
     * Finds the shortest path to all items then saves the shortest one as long as it's greater than 0 tiles away:
     * Assigns the distance from the root for every accessible tile
     * Uses a version of Dijkstra's algorithm to find the shortest route to the item
     */
    public void findShortestPath(Board curBoard) {
        shortestPath = new ArrayList<Vertex>();
        ArrayList<Vertex> vertexQueue = new ArrayList<>();

        Vertex[][] vertexBoard = new Vertex[0][];
        for (Item curItem : collectableItemArrayList) {
            if (collectableItemArrayList.size() == 1 || !curItem.getEntityName().equals("Door")) {
                vertexBoard = initialiseVertexBoard(curBoard, getxCoord(), getyCoord());
                int checkX;
                int checkY;
                int shortestDist = 0;
                ArrayList<Vertex> shortestPathNodes = new ArrayList<>();
                vertexQueue = new ArrayList<>();
                ArrayList<Vertex> tempShortestPath = new ArrayList<>();

                vertexQueue.add(vertexBoard[getyCoord()][getxCoord()]);
                Boolean found = false;
                int tempShortest = -1;
                while (vertexQueue.size() > 0 && !found) {
                    for (int i = 0; i < vertexQueue.size(); i++) {
                        if (tempShortest + vertexQueue.get(0).getDistance() < shortestDist) {
                            tempShortest += vertexQueue.get(0).getDistance();
                            vertexQueue.add(vertexQueue.get(0));
                            vertexQueue.remove(0);
                        }
                    }
                    Vertex curVertex = vertexQueue.get(0);
                    tempShortest = curVertex.getDistance();
                    checkX = curVertex.getX();
                    checkY = curVertex.getY();
                    if (!curVertex.isVisited()) {
                        int jumpCheckY = checkY;
                        int jumpCheckX = checkX;
                        checking = true;
                        while (jumpCheckY >= 0 && checking) {
                            jumpCheckY--;
                            if (checkTile(checkX, jumpCheckY, curVertex, curBoard, vertexBoard, curItem)) {
                                vertexQueue.add(vertexBoard[jumpCheckY][checkX]);
                                vertexBoard[jumpCheckY][checkX].setDistance(tempShortest + 1);
                                vertexBoard[jumpCheckY][checkX].setParentVertex(curVertex, "N");
                            }
                        }
                        jumpCheckX = checkX;
                        checking = true;
                        while (jumpCheckX < curBoard.getBoardMaxX() && checking) {
                            jumpCheckX++;
                            if (checkTile(jumpCheckX, checkY, curVertex, curBoard, vertexBoard, curItem)) {
                                vertexQueue.add(vertexBoard[checkY][jumpCheckX]);
                                vertexBoard[checkY][jumpCheckX].setDistance(tempShortest + 1);
                                vertexBoard[checkY][jumpCheckX].setParentVertex(curVertex, "E");
                            }
                        }
                        jumpCheckY = checkY;
                        checking = true;
                        while (jumpCheckY < curBoard.getBoardMaxY() && checking) {
                            jumpCheckY++;
                            if (checkTile(checkX, jumpCheckY, curVertex, curBoard, vertexBoard, curItem)) {
                                vertexQueue.add(vertexBoard[jumpCheckY][checkX]);
                                vertexBoard[jumpCheckY][checkX].setDistance(tempShortest + 1);
                                vertexBoard[jumpCheckY][checkX].setParentVertex(curVertex, "S");
                            }
                        }
                        jumpCheckX = checkX;
                        checking = true;
                        while (jumpCheckX >= 0 && checking) {
                            jumpCheckX--;
                            if (checkTile(jumpCheckX, checkY, curVertex, curBoard, vertexBoard, curItem)) {
                                vertexQueue.add(vertexBoard[checkY][jumpCheckX]);
                                vertexBoard[checkY][jumpCheckX].setDistance(tempShortest + 1);
                                vertexBoard[checkY][jumpCheckX].setParentVertex(curVertex, "W");
                            }
                        }
                    }

                    if (checkX == curItem.getxCoord() && checkY == curItem.getyCoord()) {
                        if (tempShortest < shortestDist) ;
                        {
                            shortestDist = curVertex.getDistance();
                            found = true;
                            shortestPathNodes.add(curVertex);
                        }
                    }
                    curVertex.setVisited();

                    vertexQueue.remove(curVertex);
                }
                for (int i = 0; i < shortestDist - 1; i++) {
                    shortestPathNodes.add(shortestPathNodes.get(shortestPathNodes.size() - 1).getParent());
                }
                for (int i = 0; i < shortestPathNodes.size(); i++) {
                    tempShortestPath.add(shortestPathNodes.get(shortestPathNodes.size() - 1 - i));
                }
                if ((tempShortestPath.size() < shortestPath.size() || shortestPath.size() == 0) && tempShortestPath.size() != 0) {
                    shortestPath = tempShortestPath;
                }

            }
        }
        //System.out.println(boardToString(vertexBoard));
    }

    /**
     * Creates a replica of the current board made of Vertices
     * Assigns the starting vertex with distance 0
     *
     * @param curBoard The current game board
     * @param rootX    The X coordinate for the thieves current location
     * @param rootY    The X coordinate for the thieves current location
     * @return The vertex board representation
     */
    private Vertex[][] initialiseVertexBoard(Board curBoard, int rootX, int rootY) {
        int maxxBound = curBoard.getBoardMaxX();
        int maxyBound = curBoard.getBoardMaxY();
        Vertex[][] vertexBoard = new Vertex[maxyBound][maxxBound];
        for (int y = 0; y < maxyBound; y++) {
            for (int x = 0; x < maxxBound; x++) {
                vertexBoard[y][x] = new Vertex(x, y, curBoard.getTile(x, y).getColour());
            }
        }
        vertexBoard[rootY][rootX].setDistance(0);
        return vertexBoard;
    }

    /**
     * Checks whether the thief could move to the tile in a single movement by checking:
     * they are in bounds
     * they share a common colour
     * the tile has not already been visited
     *
     * @param checkX      tile to checks X coordinate
     * @param checkY      tile to checks Y coordinate
     * @param vertex      vertex representation of tile to check
     * @param curBoard    the current game board
     * @param vertexBoard the vertex representation of the board
     * @param curItem     the target item
     * @return Whether the Tile is valid
     */
    private boolean checkTile(int checkX, int checkY, Vertex vertex, Board curBoard, Vertex[][] vertexBoard, Item curItem) {
        int maxX = curBoard.getBoardMaxX();
        int maxY = curBoard.getBoardMaxY();

        if (checkY >= 0 && checkY < maxY &&
                checkX < maxX && checkX >= 0) {
            String[] nextTileColours = vertex.getColour().split("");
            String[] curTileColours = curBoard.getTile(checkX, checkY).getColour().split("");
            for (String curColour : curTileColours) {
                for (String nextColour : nextTileColours) {
                    if (nextColour.equals(curColour)) {
                        if ((!vertex.isVisited() && vertexBoard[checkY][checkX].getDistance() < 0)
                                && (curBoard.getTile(checkX, checkY).getOccupyingItem() == curItem
                                || curBoard.getTile(checkX, checkY).getOccupyingItem() == null)) {
                            checking = false;
                            return true;
                        } else {
                            checking = false;
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns the vertex board in a printable format
     *
     * @param vertexBoard the vertex representation of the board
     * @return the string representation of the board
     */
    private String boardToString(Vertex[][] vertexBoard) {
        String outputString = "";
        for (int y = 0; y < vertexBoard.length; y++) {
            for (int x = 0; x < vertexBoard[0].length; x++) {
                outputString += vertexBoard[y][x];
            }
            outputString += "\n";
        }
        return outputString;
    }

    /**
     * Represents a simplified version of a Tile, only storing information necessary for finding the shortest path.
     */
    private class Vertex {
        private int x;
        private int y;
        private int distance = -1;
        private Vertex parentVertex;
        private boolean visited = false;
        private String colour;
        private String directionFromParent = "H";

        /**
         * @param x      x Location of vertex in the board
         * @param y      y Location of vertex in the board
         * @param colour Colours of the vertex
         */
        public Vertex(int x, int y, String colour) {
            this.x = x;
            this.y = y;
            this.colour = colour;
        }

        /**
         * @return Return colour of tile
         */
        public String getColour() {
            return colour;
        }

        /**
         * @return distance of node from root
         */
        public int getDistance() {
            return distance;
        }

        /**
         * @param distance of node from root
         */
        public void setDistance(int distance) {
            this.distance = distance;
        }

        /**
         * @return Previous vertex in traverse of board
         */
        public Vertex getParent() {
            return this.parentVertex;
        }

        /**
         * @param parentVertex        Previous vertex in traverse of board
         * @param directionFromParent Direction to move to get to this Tile
         */
        public void setParentVertex(Vertex parentVertex, String directionFromParent) {
            this.parentVertex = parentVertex;
            this.directionFromParent = directionFromParent;
        }

        /**
         * @return Whether Tile has been checked during finding the shortest route
         */
        public boolean isVisited() {
            return visited;
        }

        /**
         * set the vertex to visited
         */
        public void setVisited() {
            this.visited = true;
        }

        /**
         * @return get X coordinate of Vertex
         */
        public int getX() {
            return x;
        }

        /**
         * @return get Y coordinate of Vertex
         */
        public int getY() {
            return y;
        }

        /**
         * @return get direction fromm to get here
         */
        public String getDirectionFromParent() {
            return directionFromParent;
        }

        /**
         * @return String in printable format
         */
        @Override
        public String toString() {
            return "((" + x +
                    ", " + y +
                    "):" + distance +
                    " ," + directionFromParent + ")";
        }
    }
}
