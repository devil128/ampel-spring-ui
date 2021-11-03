package de.projectdw.springfxui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class HelloFX {


    static String colorGood = "7FFF00";
    static String colorBad = "FF0000";
    static Region rect;
    static boolean wasOnline = false;
    static Logger logger = Logger.getLogger(HelloFX.class.getName());
    static String username = "";

    public void start(Stage primaryStage) {
        var submitButton = NameUI.initUI(primaryStage);
        submitButton.setOnAction((actionEvent) -> {
            initUI(primaryStage);
            username = NameUI.nameInput + NameUI.placeInput;
        });
    }

    private static void initUI(Stage primaryStage) {
        primaryStage.setTitle("Connection Tester");
        GridPane grid = new GridPane();

        StackPane root = new StackPane();
        grid.add(root, 0, 0);
        // define into box
        rect = new Region();
        rect.setMaxSize(200, 200);
        rect.setPrefSize(200, 200);
        rect.setBackground(new Background(new BackgroundFill(Color.web("#" + colorGood), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(rect);
        // shedule the task
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isValidCheck();
            }
        };
        timer.scheduleAtFixedRate(task, 500, 1000);

        // username label
        Label label1 = new Label("Name:");
        TextField textField = new TextField();
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField);
        hb.setSpacing(10);
        Button auth = new Button("Authentificate");
        Button start = new Button("Start");
        Button upload = new Button("Abgabe");
        HBox buttons = new HBox();
        buttons.setSpacing(5);
        buttons.getChildren().addAll(auth, start, upload);
        grid.add(buttons, 0, 1);

        primaryStage.setScene(new Scene(grid, 300, 251));
        primaryStage.show();
    }


    public static void isValidCheck() {
        var get = executeGet("ampel.projectdw.de", "");
        logger.info("Was online: " + wasOnline + " Timestamp of Check: " + new Date().toInstant().toEpochMilli() + " User: " + username);
        wasOnline = get.startsWith("failed");
        rect.setBackground(new Background(new BackgroundFill(Color.web("#" + (wasOnline ? colorBad : colorGood)), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public static String executeGet(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            StringBuilder result = new StringBuilder();

            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
            String urlString = builder.scheme("https").host(targetURL).path("/ping").query("name={keyword}").buildAndExpand(username).toString();
            URL url = new URL(urlString);
            System.out.println(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            return result.toString();
        } catch (Exception e) {
            return "failed " + e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
