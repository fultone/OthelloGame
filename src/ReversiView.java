/**
 * Created by fultone on 11/5/17.
 */
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;

/**
 * The ReversiView class provides the main view of the reversi game.
 * Includes the game board, ability to select game mode and start game,
   and brief instructions.
 */
public class ReversiView extends Application {
    private static final double SCENE_WIDTH = 700;
    private static final double SCENE_HEIGHT = 620;
    private ReversiController controller;
    private Button[][] allButtons;
    private boolean gameIsBeingPlayed = false;
    private BorderPane root;
    private Button playButton;
    private final int BOARD_WIDTH=8;

    public ReversiView(ReversiController controller) {
        this.controller = controller;
    }

    /**
     * Creates the primaryStage for ReversiView which is displayed using the
       BorderPane layout.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Reversi");
        root = new BorderPane();
        //Build the "top" of BorderPane "root"
        HBox menuPane = addMenus();
        menuPane.setPadding(new Insets(0, 0, 30, 0));
        root.setTop(menuPane);
        //Build the "bottom" of BorderPane "root"
        FlowPane flowPaneBottom = createFlowPane();
        root.setBottom(flowPaneBottom);
        //Build the "center" of BorderPane "root" -- contains the game board
        GridPane gameBoard = createGameBoard();
        root.setCenter(gameBoard);
        //Build the "left" side of BorderPane "root"
        FlowPane flowPaneLeft = createLeftPane();
        root.setLeft(flowPaneLeft);
        //Build the "right" side of BorderPane "root"
        FlowPane flowPaneRight = createRightPane();
        root.setRight(flowPaneRight);
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add("main.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates a "Play Game" button to appear directly below the game board.
     * Text in button changes to "End Game" on-click
     * "Play Game" button begins the game, "End Game" ends the current game.
     * @return
     */
    private Button createPlayButton() {
        playButton = new Button("Play Game");
        playButton.getStyleClass().add("playGameButton");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(gameIsBeingPlayed) {
                    resetView();
                } else {
                    FlowPane left = createLeftPane();
                    root.setLeft(left);
                    FlowPane right = createRightPane();
                    root.setRight(right);
                    controller.startGame();
                    playButton.setText("End Game");
                    playButton.getStyleClass().add("endGameButton");
                    gameIsBeingPlayed = true;
                }
            }
        });
        return playButton;
    }

    /**
     * Changes the "End Game" button to "Play Game."
     * Changes class variable "gameIsBeingPlayed" to false.
     */
    public void resetView() {
        if (gameIsBeingPlayed) {
            controller.endGame();
            playButton.setText("Play Game");
            playButton.getStyleClass().remove("endGameButton");
            gameIsBeingPlayed = false;
        }
    }

    private ChoiceBox createChoiceBox() {
        ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList(
           "Player vs. Player", "Player vs. Computer", "Computer Simulation")
        );
        choiceBox.getSelectionModel().selectFirst();
        choiceBox.setTooltip(new Tooltip("Select game mode"));
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                controller.setGameType((int)newValue);
            }
        });
        return choiceBox;
    }

    private GridPane createGameBoard() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_CENTER);
        this.allButtons = new Button[BOARD_WIDTH][BOARD_WIDTH];
        for(int i=0; i<BOARD_WIDTH; i++) {
            for(int j=0; j<BOARD_WIDTH; j++) {
                Button button = new Button("");
                int row = i;
                int col = j;
                pane.add(button, j, i, 1, 1);
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        controller.buttonClicked(row, col);
                    }
                });
                allButtons[i][j] = button;
            }
        }
        return pane;
    }

    /**
     * Changes the color of a specific button on the game board
     * The color changes depending on the playerID
     * playerID: 0 = no tile placed, 1 = player one's tile, 2 = player two's tile
     * @param row
     * @param col
     * @param playerID
     */
    public void changeButtonColor(int row, int col, int playerID) {
        if(playerID == 0) {
            allButtons[row][col].getStyleClass().clear();
            allButtons[row][col].getStyleClass().add("button");
            allButtons[row][col].getStyleClass().add("mintgreenBoard");
        } else if(playerID == 1) {
            allButtons[row][col].getStyleClass().clear();
            allButtons[row][col].getStyleClass().add("button");
            allButtons[row][col].getStyleClass().add(controller.getPlayersTileColor(1) + "Tile");
        } else if(playerID == 2) {
            allButtons[row][col].getStyleClass().clear();
            allButtons[row][col].getStyleClass().add("button");
            allButtons[row][col].getStyleClass().add(controller.getPlayersTileColor(2) + "Tile");
        }
    }

    private void alertUser() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Opening Settings Will End Current Game");
        alert.setContentText("Would you like to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            resetView();
            String player1TileColor = controller.getPlayersTileColor(1);
            String player2TileColor = controller.getPlayersTileColor(2);
            SettingsWindow settings = new SettingsWindow(controller, player1TileColor, player2TileColor);
            Stage stage = new Stage();
            settings.start(stage);
        }
    }

    private HBox addMenus() {
        HBox pane = new HBox();
        pane.setAlignment(Pos.CENTER);
        MenuBar menuBar = new MenuBar();
        menuBar.setMinWidth(SCENE_WIDTH);
        Menu fileMenu = new Menu("Menu");
        MenuItem settings = new MenuItem("Settings");
        settings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(gameIsBeingPlayed) {
                    alertUser();
                } else {
                    String player1TileColor = controller.getPlayersTileColor(1);
                    String player2TileColor = controller.getPlayersTileColor(2);
                    SettingsWindow settings = new SettingsWindow(controller, player1TileColor, player2TileColor);
                    Stage stage = new Stage();
                    settings.start(stage);
                }
            }
        });
        MenuItem help = new MenuItem("Help");
        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HelpWindow help = new HelpWindow();
                Stage stage = new Stage();
                help.start(stage);

            }
        });
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        fileMenu.getItems().addAll(settings, help, exit);
        menuBar.getMenus().addAll(fileMenu);
        pane.getChildren().add(menuBar);
        return pane;
    }

    private FlowPane createFlowPane() {
        FlowPane pane = new FlowPane();
        pane.getChildren().add(new Text("Select Game Mode: "));
        pane.getChildren().add(createChoiceBox());
        pane.getChildren().add(createPlayButton());
        pane.getChildren().add(new Text("How to Play: Reversi is a " +
           "two-player game in which players take turns \nplacing pieces " +
           "on the board. They flip the other player's pieces to" +
           "\ntheir color by outflanking them."));
        pane.setHgap(5);
        pane.setVgap(10);
        pane.setPadding(new Insets(5, 50, 20, 50));
        return pane;
    }

    /**
     * Adds the final score of player one and player two to the left and right panes
       of the main window, respectively.
     * Calls "displayWinner" for whichever player has the highest score.
     * If the players are tied, displays the message "It's a tie."
     * @param player1Score
     * @param player2Score
     */
    public void displayFinalScore(int player1Score, int player2Score) {
        FlowPane left = createLeftPane();
        FlowPane right = createRightPane();
        right.getChildren().add(new Text("Final Score: " + player2Score));
        left.getChildren().add(new Text("Final Score: " + player1Score));
        if(player1Score > player2Score) {
            left.getChildren().add(displayWinner(1));
        } else if(player2Score > player1Score) {
            right.getChildren().add(displayWinner(2));
        } else {
            right.getChildren().add(new Text("It's a tie"));
            left.getChildren().add(new Text("It's a tie"));
        }
        root.setLeft(left);
        root.setRight(right);
        left.setPadding(new Insets(5, 20, 5, 20));
        right.setPadding(new Insets(5, 20, 5, 20));
    }


    /**
     * Takes in an int parameter "playerID," corresponding to the player with
       with the highest score.
     * Returns Text object "winner" which gives the message that the specified
       player won the game.
     * @param playerID
     * @return
     */
    public Text displayWinner(int playerID) {
        Text winner = new Text("PLAYER " + playerID + "\nWINS!");
        winner.getStyleClass().add("winner");
        return winner;
    }

    //Pane includes information about Player 1; displays their tile color.
    private FlowPane createLeftPane() {
        FlowPane left = new FlowPane(Orientation.VERTICAL);
        left.setPrefWidth(100);
        Button player1ExampleButton = new Button();
        player1ExampleButton.getStyleClass().add(controller.getPlayersTileColor(1) + "Tile");
        Text player1 = new Text("Player 1");
        player1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        left.getChildren().add(player1);
        left.getChildren().add(player1ExampleButton);
        left.setPadding(new Insets(10, 0, 10, 10));
        return left;
    }

    //Pane includes information about Player 2; displays their tile color.
    private FlowPane createRightPane() {
        FlowPane right = new FlowPane(Orientation.VERTICAL);
        right.setPrefWidth(100);
        Button player2ExampleButton = new Button();
        player2ExampleButton.getStyleClass().add(controller.getPlayersTileColor(2) + "Tile");
        Text player2 = new Text("Player 2");
        player2.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        right.getChildren().add(player2);
        right.getChildren().add(player2ExampleButton);
        right.setPadding(new Insets(10, 10, 10, 0));
        return right;
    }

    /**
        * Notifies either player 1 or player 2 when it's their turn.
        * Adds the message "It's your turn!" to the pane on the specified
          side of the board.
     **/
    public void addCurrentTurn(int playerID) {
        if(playerID == 2) {
            FlowPane pane = createRightPane();
            pane.getChildren().add(new Text("It's your turn!"));
            root.setRight(pane);
        } else if(playerID == 1) {
            FlowPane pane = createLeftPane();
            pane.getChildren().add(new Text("It's your turn!"));
            root.setLeft(pane);
        }
    }

    /**
        * Removes the message "It's your turn!" from the specified side of the board
        * Each side (right/left) corresponds to a specific player (Player 1 / Player 2)
     **/
    public void removeCurrentTurn(int playerID) {
        if(playerID == 2) {
            FlowPane pane = createRightPane();
            pane.getChildren().remove(new Text("It's your turn!"));
            root.setRight(pane);
        } else if(playerID == 1) {
            FlowPane pane = createLeftPane();
            pane.getChildren().remove(new Text("It's your turn!"));
            root.setLeft(pane);
        }
    }

    /**
        * Notifies a player when they have no available moves on the board.
        * Creates a pass button that the player must click in order for the game
          to continue.
        * Note: the pass button is only created if the player who has no available
          moves is a Human player.
     **/
    public void displayPass(int playerID) {
        if(playerID==1) {
            FlowPane pane = createLeftPane();
            pane.getChildren().add(new Text("There are no\navailable moves.\nPASS!"));
            root.setLeft(pane);
        } else if(playerID==2) {
            FlowPane pane = createRightPane();
            pane.getChildren().add(new Text("There are no\navailable moves.\nPASS!"));
            root.setRight(pane);
        }
    }
}
