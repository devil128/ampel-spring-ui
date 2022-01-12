package de.projectdw.springfxui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * class to init name input UI
 */
public class NameUI {
    static String nameInput;
    static String placeInput;

    public static Button initUI(Stage primaryStage) {
        primaryStage.setTitle("Namens und Sitzplatz Eingabe");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
        /// name field
        final TextField name = new TextField();
        name.setPromptText("Namenseingabe");
        name.setPrefColumnCount(10);
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            nameInput = newValue;
        });
        GridPane.setConstraints(name, 0, 0);
        grid.getChildren().add(name);
        // place field
        final TextField place = new TextField();
        place.setPromptText("Sitzplatz Eingabe");
        place.textProperty().addListener((observable, oldValue, newValue) -> {
            placeInput = newValue;
        });
        GridPane.setConstraints(place, 0, 1);
        grid.getChildren().add(place);

        // button

        Button submit = new Button("Bestätigen");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);

        primaryStage.setScene(new Scene(grid, 200, 200));
        primaryStage.show();
        return submit;
    }
}
