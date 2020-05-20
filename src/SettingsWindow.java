import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.text.Text;

/**
 * SettingsWindow class creates a window which allows the user to alter tile color and game board color.
 * The window is accessed by clicking "Settings" under the dropdown top "Menu" which appears in "Reversi View."
*/
public class SettingsWindow extends Application {
   private static final double SCENE_WIDTH = 350;
   private static final double SCENE_HEIGHT = 175;
   private ReversiController controller;
   private String player1TileColor;
   private String player2TileColor;

   public SettingsWindow(ReversiController controller, String player1TileColor, String player2TileColor) {
      this.controller = controller;
      this.player1TileColor = player1TileColor;
      this.player2TileColor = player2TileColor;
   }

   /**
    * Creates a primary stage which contains the "Settings" window.
    * Settings window is comprised of a TilePane layout.
    */
   @Override
   public void start(Stage primaryStage) {
      primaryStage.setTitle("Settings");
      TilePane root = new TilePane();
      root.setPadding(new Insets(10, 10, 10, 10));
      root.setVgap(6);
      root.setHgap(20);
      VBox playerOneOptions = new VBox(createToggleGroup("Player 1 Tile Color:",
         "Black", "Blue","Yellow", convertTileColorToInt(player1TileColor)));
      VBox playerTwoOptions = new VBox(createToggleGroup("Player 2 Tile Color:",
         "White", "Red", "Purple", convertTileColorToInt(player2TileColor)));
      root.getChildren().add(playerOneOptions);
      root.getChildren().add(playerTwoOptions);
      root.getChildren().add(createSaveButton());
      Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
      scene.getStylesheets().add("main.css");
      primaryStage.setScene(scene);
      primaryStage.show();
   }

   private int convertTileColorToInt(String tileColor) {
      int tileColorInt = 0;
      tileColor = tileColor.toLowerCase();
      String[] player1Colors = {"black", "blue", "yellow"};
      String[] player2Colors = {"white", "red", "purple"};
      for(int i=0; i<3; i++) {
         if(tileColor.equals(player1Colors[i]) || tileColor.equals(player2Colors[i])) {
            tileColorInt = i + 1;
         }
      }
      return tileColorInt;
   }

   private Button createSaveButton() {
      Button save = new Button("Save");
      save.getStyleClass().add("playButton");
      save.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();
         }
      });
      return save;
   }

   private VBox createToggleGroup(String title, String color1, String color2, String color3, int setSelectedButton){
      ToggleGroup colorOptions = new ToggleGroup();

      RadioButton firstColor = new RadioButton(color1);
      firstColor.setToggleGroup(colorOptions);

      RadioButton secondColor = new RadioButton(color2);
      secondColor.setToggleGroup(colorOptions);

      RadioButton thirdColor = new RadioButton(color3);
      thirdColor.setToggleGroup(colorOptions);

      if(setSelectedButton == 1) {
         firstColor.setSelected(true);
      } else if(setSelectedButton == 2){
         secondColor.setSelected(true);
      } else {
         thirdColor.setSelected(true);
      }

      VBox v = new VBox();
      Text theTitle = new Text(title);
      theTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
      v.getChildren().add(theTitle);
      v.getChildren().add(firstColor);
      v.getChildren().add(secondColor);
      v.getChildren().add(thirdColor);
      v.setSpacing(5);

      colorOptions.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
         @Override
         public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
            if(colorOptions.getSelectedToggle() != null) {
               RadioButton selected = (RadioButton)newValue.getToggleGroup().getSelectedToggle();
               String newColor = selected.getText().replace(" ","").toLowerCase();

               if(title.equals("Player 1 Tile Color:")) {
                  controller.changePlayersTileColor(1, newColor);
               } else if(title.equals("Player 2 Tile Color:")) {
                  controller.changePlayersTileColor(2, newColor);
               }
            }
         }
      });
      return v;
   }

}
