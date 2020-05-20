import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;

/**
 * The reversi controller communicates with the view and the
 * model (game board and players).
 */
public class ReversiController extends Application {
    private GameBoard gameBoard;
    private Player currentPlayer;
    private ReversiView reversiView;
    private Player player1;
    private Player player2;
    private int gameType = 0;
    private final int BOARD_WIDTH=8;

    /**
     * Start method takes in a stage where the main reversi game window will
     * be built.
     * Initializes reversi view with this (controller) as a parameter.
     * Creates two human players initially, which are subject to change (to
     * computer players) depending on game type.
     */
    @Override
    public void start(Stage stage) {
        this.reversiView = new ReversiView(this);
        player1 = new HumanPlayer(1);
        player2 = new HumanPlayer(2);
        reversiView.start(stage);
    }

    /**
     * Initializes gameboard and prepares the players to play on that board.
     *
     */
    public void startGame() {
        gameBoard = new GameBoard(this);
        player1.setBoard(gameBoard);
        player2.setBoard(gameBoard);
        currentPlayer = player1;
        if(gameType==2){
            buttonClicked(0,0);
        }
    }

    /**
     * Prevents players from continuing to play.
     */
    public void endGame() {
        showScore();
        for(int i = 0; i < BOARD_WIDTH; i++) {
            for(int j = 0; j < BOARD_WIDTH; j++) {
                gameBoard.getTile(i,j).setColor(3);
            }
        }
    }

    /*
        * Game Type 0 = "Player vs. Player"
        * Game Type 1 = "Player vs. Computer
        * Game Type 2 = "Computer Simulation"
     */
    /**
     * Creates new Human or Computer players depending on the type of game.
     * @param gameType
     */
    public void setGameType(int gameType) {
        this.gameType = gameType;
        String player1Color = player1.getTileColor();
        String player2Color = player2.getTileColor();
        if(this.gameType == 0){
            player1 = new HumanPlayer(1);
            player2 = new HumanPlayer(2);
        } if(this.gameType == 1){
            player1 = new HumanPlayer(1);
            player2 = new ComputerPlayer(2);
        } else if(this.gameType == 2){
            player1 = new ComputerPlayer(1);
            player2 = new ComputerPlayer(2);
        }
        player1.setTileColor(player1Color);
        player2.setTileColor(player2Color);
    }

    private void showScore() {
        int player1Score = 0;
        int player2Score = 0;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if(gameBoard.getTile(i,j).getColor()==1) {
                    player1Score++;
                } else if (gameBoard.getTile(i,j).getColor()==2) {
                    player2Score++;
                }
            }
        }
        reversiView.displayFinalScore(player1Score, player2Score);
    }

    /**
     * Handles the button click and a player's turn.
     * @param row
     * @param col
     */
    public void buttonClicked(int row, int col) {
        if(gameBoard != null) {
            if(!gameBoard.areAvailableMoves(currentPlayer.getPlayerID())) {
                changeCurrentPlayer();
                if(!gameBoard.areAvailableMoves(currentPlayer.getPlayerID())) {
                    reversiView.resetView();
                } else {
                    pass();
                }
            } else {
                if(currentPlayer.moveIfValid(row, col)) {
                    changePlayersTurn();
                }
                if(currentPlayer.getPlayerType().equals("Computer")) {
                    computerDelay();
                }
            }
        }
    }

    private void pass() {
        changeCurrentPlayer();
        reversiView.displayPass(currentPlayer.getPlayerID());
        changeCurrentPlayer();
        if(currentPlayer.getPlayerType().equals("Computer")) {
            computerDelay();
        }
    }

    private void changePlayersTurn() {
        if(currentPlayer.getPlayerID() == 1) {
            reversiView.removeCurrentTurn(1);
            reversiView.addCurrentTurn(2);
        } else if (currentPlayer.getPlayerID() == 2) {
            reversiView.removeCurrentTurn(2);
            reversiView.addCurrentTurn(1);
        }
        changeCurrentPlayer();
    }

    private void computerDelay() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int iterations;
                for (iterations = 0; iterations < 2000000000; iterations++) {
                    if (isCancelled()) {
                        break;
                    }
                }
                Platform.runLater(() -> buttonClicked(0,0));
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    /**
     * Changes the current player to the other player.
     * @return currentPlayer
     */
    public Player changeCurrentPlayer() {
        if(currentPlayer.equals(player1)) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
        return currentPlayer;
    }

    /**
     * Change display color for a given tile to given player's display color.
     * @param tile
     * @param playerID
     */
    public void updateTileColor(Tile tile, int playerID) {
        int row = tile.getRow();
        int col = tile.getCol();

        reversiView.changeButtonColor(row, col, playerID);
    }

    /**
     * Change given player's tile color to given color.
     * @param playerID
     * @param newColor
     */
    public void changePlayersTileColor(int playerID, String newColor) {
        if(playerID == 1) {
            player1.setTileColor(newColor);
        } else if(playerID == 2) {
            player2.setTileColor(newColor);
        }
    }

    /**
     * Returns String associated with the tile color for the given player.
     * Defaults to black for player1, white for player2, and empty string for null
     * @param playerID
     * @return tileColor
     */
    public String getPlayersTileColor(int playerID) {
        if(playerID == 1) {
            if (player1 != null){
                return player1.getTileColor();
            } else {
                return "black";
            }
        } else if(playerID == 2) {
            if(player2 != null) {
                return player2.getTileColor();
            } else {
                return "white";
            }
        } else {
            return "";
        }
    }

    public static void main(String args[]) {
        launch(args);
    }
}