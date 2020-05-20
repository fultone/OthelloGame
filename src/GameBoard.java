/**
 * Created by gorram on 11/8/17.
 */
import java.util.ArrayList;

public class GameBoard {
    private Tile[][] allTiles;
    private ArrayList<Tile> totalList;
    private final int BOARD_WIDTH=8;
    private final int EMPTY=0;
    private final int MIDDLE_DIAGONAL=7;

    /**
     * Makes a nested Array to contain 64 Tiles who know if they are occupied or not
     * in order to check moves and flip tiles.
     */
    public GameBoard(ReversiController controller) {
        allTiles = new Tile[BOARD_WIDTH][BOARD_WIDTH];
        totalList=new ArrayList<>();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tile newTile = new Tile(i,j, controller);
                allTiles[i][j] = newTile;
            }
        }
        initializeBoard();
    }

    /*
     * Narrows down the list of all tiles in a line to just the ones that would be flipped by a move
     * of the given color in the given position.
     * Does so by expanding outwards from the position until hitting a tile that wouldn't be flipped,
     * then either adding or ignoring the previous tiles based on if the ending tile is empty or already
     * the new color.
     */
    private ArrayList<Tile> checkBoardList(ArrayList<Tile> boardList, int tilePosition, int playerID) {
        ArrayList<Tile> chain=new ArrayList<Tile>();
        ArrayList<Tile> leftchain=new ArrayList<Tile>();
        ArrayList<Tile> rightchain=new ArrayList<Tile>();
        //Scans right side of tilePosition
        if(tilePosition!=boardList.size()) {
            for (int i = tilePosition + 1; i < boardList.size(); i++) {
                Tile tile = boardList.get(i);
                if (tile.getColor() == EMPTY) {
                    break;
                } else if (tile.getColor() == playerID) {
                    chain = rightchain;
                    break;
                } else {
                    rightchain.add(tile);
                }

            }
        }
        //Scans left side of tilePosition
        if (tilePosition!=0) {
            for (int i = tilePosition - 1; i >= 0; i--) {
                Tile tile = boardList.get(i);
                if (tile.getColor() == EMPTY) {
                    break;
                } else if (tile.getColor() == playerID) {
                    for (int j = 0; j < leftchain.size(); j++) {
                        chain.add(0, leftchain.get(j));
                    }
                    break;
                } else {
                    leftchain.add(0, tile);
                }

            }
        }
        return chain;

    }

    private void tilesToBeFlipped(Tile playedTile, int playerID) {
        totalList=new ArrayList<>();
        ArrayList<Tile> rowList = getTilesInRow(playedTile.getRow());
        ArrayList<Tile> colList = getTilesInCol(playedTile.getCol());
        ArrayList<Tile> leftdiagList = getTilesInLeftDiag(playedTile.getLeftDiag());
        ArrayList<Tile> rightdiagList = getTilesInRightDiag(playedTile.getRightDiag());
        rowList=checkBoardList(rowList, playedTile.getCol(), playerID);
        colList=checkBoardList(colList, playedTile.getRow(), playerID);
        int leftdiagPosition=getDiagonalPosition(playedTile.getLeftDiag(), playedTile.getRow());
        int rightdiagPosition=getDiagonalPosition(playedTile.getRightDiag(), playedTile.getRow());
        leftdiagList=checkBoardList(leftdiagList,leftdiagPosition, playerID);
        rightdiagList=checkBoardList(rightdiagList, rightdiagPosition, playerID);
        totalList.addAll(rowList);
        totalList.addAll(colList);
        totalList.addAll(leftdiagList);
        totalList.addAll(rightdiagList);
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                placeTile(i, j, 0);
            }
        }
        placeTile(3, 3, 1);
        placeTile(3,4,2);
        placeTile(4,3,2);
        placeTile(4,4,1);
    }

    /**
     * Will eventually use the getRow, getCol, etc. methods below in order to check if the move would flip tiles
     * Currently checks if the Tile is empty
     * @param row
     * @param col
     * @return boolean
     */
    public boolean isValidMove(int row, int col, int playerID) {
        Tile tile = getTile(row, col);
        if (tile.getColor()!=EMPTY) {
            return false;
        }
        tilesToBeFlipped(getTile(row, col), playerID);
        if (totalList.size()==0) {
            return false;
        }
        return true;
    }

    /**
     * Sets the color of the given Tile and all the tiles in totalList to the ID of the given player
     * @param row
     * @param col
     * @param playerID
     */
    public void placeTile(int row, int col, int playerID) {
        getTile(row, col).setColor(playerID);
        for (int i = 0; i < totalList.size(); i++) {
            totalList.get(i).setColor(playerID);
        }
    }

    /**
     * Checks every tile in the board to see if there are moves available for the given playerID
     * @param playerID
     * @return boolean
     */
    public boolean areAvailableMoves(int playerID) {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if(isValidMove(i,j, playerID)) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Gets the position in the given diagonal of the Tile in the given row
     * @param diag
     * @param row
     * @return position
     */
    private int getDiagonalPosition(int diag, int row) {
        int position=row;
        if(diag>(MIDDLE_DIAGONAL)) {
            position=row+MIDDLE_DIAGONAL-diag;
        }
        return position;
    }

    private ArrayList<Tile> getTilesInRow(int row) {
        ArrayList<Tile> rowList = new ArrayList<>();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            Tile tile = getTile(row, i);
            rowList.add(tile);
        }
        return rowList;
    }
    private ArrayList<Tile> getTilesInCol(int col) {
        ArrayList<Tile> colList = new ArrayList<>();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            Tile tile = getTile(i, col);
            colList.add(tile);
        }
        return colList;
    }
    private ArrayList<Tile> getTilesInLeftDiag(int leftDiag) {
        ArrayList<Tile> leftDiagList = new ArrayList<>();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tile tile = getTile(i,j);
                if(tile.getLeftDiag()==leftDiag) {
                    leftDiagList.add(tile);
                }
            }

        }
        return leftDiagList;
    }
    private ArrayList<Tile> getTilesInRightDiag(int rightDiag){
        ArrayList<Tile> rightDiagList = new ArrayList<>();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tile tile = getTile(i,j);
                if(tile.getRightDiag()==rightDiag) {
                    rightDiagList.add(tile);
                }
            }

        }
        return rightDiagList;
    }

    public Tile getTile(int row, int col) {
        return allTiles[row][col];
    }
}
