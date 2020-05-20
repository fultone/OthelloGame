import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.image.Image;

/**
 * HelpWindow class creates a window which provides more detailed Reversi
 * instructions.
 * The window is accessed by clicking "Help" under the dropdown top "Menu"
 * which appears in "Reversi View."
 */
public class HelpWindow extends Application {
   private static final double SCENE_WIDTH = 975;
   private static final double SCENE_HEIGHT = 360;

   /**
    * Creates a primary stage which houses the "Help" window.
    * Help window is comprised of a BorderPane layout.
    */
   @Override
   public void start(Stage primaryStage) {
      primaryStage.setTitle("Help");
      BorderPane root = new BorderPane();
      root.setTop(createTop());
      root.setCenter(createCenter());
      root.setBottom(createFooter());
      root.setRight(createClose());
      Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
      scene.getStylesheets().add("main.css");
      primaryStage.setScene(scene);
      primaryStage.show();
   }

   /**
    * Creates a header for the Help Window.
    */
   public HBox createTop() {
      Text instructionsTitle = new Text("Official Reversi Instructions\n");
      instructionsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 15));
      HBox title = new HBox(instructionsTitle);
      title.setPadding(new Insets(20, 30, 0, 20));
      return title;
   }

   /**
    * Creates a vertical box that contains detailed game instructions complete
    * with a diagram.
    */
   public VBox createCenter() {
      VBox center = new VBox();
      ImageView flanking = new ImageView(new Image("flanking.png"));
      flanking.setFitHeight(600);
      flanking.setFitWidth(600);
      flanking.setPreserveRatio(true);
      Text startInstructions = new Text("The game begins with two white tiles and " +
         "two black tiles placed diagonally to one another in the center of the " +
         "board,\nforming a square. 'Player One' moves first by 'outflanking' " +
         "their opponent's tile(s) to their color. To outflank means\nto place " +
         "a tile on the board so that your opponent's row (or rows) of tiles is " +
         "bordered at each end by a tile of your color.");
      Text moreInstructions = new Text("A row may be made up of one or more tiles " +
         "and can be formed horizontally, vertically, or diagonally. If a player\n" +
         "cannot outflank and flip at least one opposing tile, they forfeit their " +
         "turn and their opponent moves again. If a play\nis available, a player " +
         "may not forfeit their turn.");
      center.getChildren().add(startInstructions);
      center.getChildren().add(flanking);
      center.getChildren().add(moreInstructions);
      center.setPadding(new Insets(0, 30, 0, 20));
      return center;
   }

   /**
    * Hyperlinks the word "here."
    * Links to official Mattel's official Othello (Reversi) rules.
    */
   public Hyperlink createLink() {
      Hyperlink link = new Hyperlink("https://service.mattel.com/instruction_sheets/52551.pdf");
      link.setText("here.");
      link.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
            getHostServices().showDocument("https://service.mattel.com/instruction_sheets/52551.pdf");
         }
      });
      return link;
   }

   /**
    * Creates a "Close" button to appear in the bottom right of the window.
    * On-click, the "Close" button closes the Help window.
    */
   public HBox createClose() {
      Button close = new Button("Close");
      close.getStyleClass().add("playButton");
      close.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
         }
      });
      HBox rightSide = new HBox(close);
      rightSide.setAlignment(Pos.BOTTOM_RIGHT);
      rightSide.setPadding(new Insets(20, 30, 0, 0));
      return rightSide;
   }

   /**
    * Creates a footer which prompts user to view the full reversi instructions on Mattel's website.
    * Footer provides hyperlink to said website.
    */
   public HBox createFooter() {
      HBox footer = new HBox();
      Text clickable = new Text("To view Mattel's official Reversi (Othello) instructions, click the link ");
      footer.getChildren().add(clickable);
      footer.getChildren().add(createLink());
      footer.setPadding(new Insets(0, 30, 5, 20));
      return footer;
   }
}