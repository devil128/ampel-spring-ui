package de.projectdw.springfxui;

import de.projectdw.springfxui.data.InputLogData;
import de.projectdw.springfxui.data.NetworkPair;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
/**
 * FX class to start the UI
 */
public class HelloFX {
    private static NetworkManager networkManager;


    static String colorGood = "7FFF00";
    static String colorBad = "FF0000";
    static Region rect;
    static boolean wasOnline = false;
    static Logger logger = Logger.getLogger(HelloFX.class.getName());
    static String username = "";
    static String place = "";

    public void start(Stage primaryStage) {
        // loads first the UI to input the name and place of the user
        var submitButton = NameUI.initUI(primaryStage);
        submitButton.setOnAction((actionEvent) -> {
            initUI(primaryStage);
            username = NameUI.nameInput;
            place = NameUI.placeInput;
        });
    }

    private static void initUI(Stage primaryStage) {
        // basic UI setup and box
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
        timer.scheduleAtFixedRate(task, 100, 5000);

        // username label
        Label label1 = new Label("Name:");
        TextField textField = new TextField();
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField);
        hb.setSpacing(10);
        Button upload = new Button("Abgabe");

        Label status = new Label();
        upload.setOnAction(actionEvent -> {
            if (networkManager.uploadLogs()) {
                status.setText("Upload fertiggestellt");
            } else {
                status.setText("Upload fehlgeschlagen");
            }
        });
        HBox buttons = new HBox();
        buttons.setSpacing(5);
        buttons.getChildren().add(upload);
        buttons.getChildren().add(status);

        grid.add(buttons, 0, 1);

        primaryStage.setScene(new Scene(grid, 300, 251));
        primaryStage.show();
    }

    /**
     * tries to connect to URL activated regulary through TimerTask
     */
    public static void isValidCheck() {
        InputLogData inputLogData = new InputLogData();
        inputLogData.setName(username);
        inputLogData.setPlace(place);
        inputLogData.setTimestamp(System.currentTimeMillis() + "");

        inputLogData.setNetworkInterfaces(getNetworkInterfaces());

        var wasOnline = networkManager.tryToConnect(inputLogData);
        logger.info("Was online: " + wasOnline + " Timestamp of Check: " + new Date().toInstant().toEpochMilli() + " User: " + username);

        rect.setBackground(new Background(new BackgroundFill(Color.web("#" + (wasOnline ? colorBad : colorGood)), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * lists network interfaces which are available for the client
     * @return
     */
    private static List<NetworkPair> getNetworkInterfaces() {
        ArrayList<NetworkPair> res = new ArrayList<>();
        try {
            List<NetworkInterface> networkInterface = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : networkInterface) {
                NetworkPair networkPair = new NetworkPair();
                networkPair.setNetwork(ni.getName());
                networkPair.setOnline(false);
                if (ni.isUp() && !ni.isLoopback()) {
                    logger.info(ni.toString());
                    networkPair.setOnline(true);
                    res.add(networkPair);
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Exception: ", ex);
        }

        return res;
    }


    @Autowired
    public void setNetworkManager(NetworkManager networkManager) {
        HelloFX.networkManager = networkManager;
    }
}
