/**
 *
 * Created by eisend on 11/6/17.
 *
 */
public interface Player {
    String getPlayerType();
    boolean moveIfValid(int row, int col);
    void setBoard(GameBoard board);
    int getPlayerID();
    void setTileColor(String color);
    String getTileColor();
}
