/**
 * Created by fultone on 11/5/17.
 */
public class Tile {

    private int color;
    private int rowNum;
    private int colNum;
    private int leftDiag;
    private int rightDiag;
    private final int EMPTY=0;
    private final int MIDDLE_DIAGONAL=7;
    private ReversiController controller;

    /**
     * Makes an empty Tile that knows information about its position
     * @param rowNum
     * @param colNum
     */
    public Tile(int rowNum, int colNum) {
        this.rowNum=rowNum;
        this.colNum=colNum;
        this.leftDiag=MIDDLE_DIAGONAL-colNum+rowNum;
        this.rightDiag=colNum+rowNum;
        this.color=EMPTY;
    }

    /**
     * Makes an empty Tile that knows information about its position and controlller
     * @param rownum
     * @param colnum
     */
    public Tile(int rownum, int colnum, ReversiController controller){
        this(rownum, colnum);
        this.controller = controller;
    }

    public int getRow() {
        return rowNum;
    }

    public int getCol() {
        return colNum;
    }

    public int getLeftDiag() {
        return leftDiag;
    }

    public int getRightDiag() {
        return rightDiag;
    }

    public void setColor(int playerID) {
        this.color = playerID;
        controller.updateTileColor(this, playerID);
    }

    public int getColor() {
        return color;
    }


}
