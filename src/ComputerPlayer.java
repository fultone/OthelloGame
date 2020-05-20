import java.lang.Math;

public class ComputerPlayer implements Player {
    GameBoard board;
    int playerID;
    String tileColor;

    /**
     * Makes a computer-controlled player with the given playerID that moves at random
     * @param playerID
     */
    public ComputerPlayer(int playerID) {
        this.playerID = playerID;
        if (playerID == 1) {
            tileColor = "black";
        } else if (playerID == 2) {
            tileColor = "white";
        }
    }

    /**
     * Places a tile on a random square, and does all resulting tile flips,
     * and returns true if any moves are available,
     * returns false if there are no moves left for the player
     * @param row
     * @param col
     * @return boolean if completed
     */
    @Override
    public boolean moveIfValid(int row, int col) {
        while (board.areAvailableMoves(playerID)) {
            int selectedRow = (int)(8 * Math.random());
            int selectedCol = (int)(8 * Math.random());
            if (board.isValidMove(selectedRow, selectedCol, playerID)) {
                board.placeTile(selectedRow, selectedCol, playerID);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPlayerType() {
        return "Computer";
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
