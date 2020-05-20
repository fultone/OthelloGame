/**
 *
 * Created by gorram on 11/8/17.
 */
public class HumanPlayer implements Player {
    GameBoard board;
    int playerID;
    String tileColor;

    /**
     * Creates a human controlled player with an ID number for a specific gameboard.
     * @param playerID
     */
    public HumanPlayer(int playerID) {
        this.playerID=playerID;
        if(playerID == 1) {
            tileColor = "black";
        } else if(playerID == 2) {
            tileColor = "white";
        }
    }

    /**
     * Changes the Tile in given location to the players ID if it is a valid move,
     * as well as flipping tiles according to the rules of Reversi
     * @param row
     * @param col
     */
    @Override
    public boolean
    moveIfValid(int row, int col) {
        if (board.isValidMove(row, col, playerID)) {
            board.placeTile(row, col, playerID);
            return true;
        }
        return false;
    }

    @Override
    public String getPlayerType() {
        return "Human";
    }

    @Override
    public void setBoard(GameBoard board) {
        this.board = board;
    }

    @Override
    public int getPlayerID() {
        return playerID;
    }

    @Override
    public void setTileColor(String color) {
        tileColor = color;
    }

    @Override
    public String getTileColor() {
        return tileColor;
    }

}
