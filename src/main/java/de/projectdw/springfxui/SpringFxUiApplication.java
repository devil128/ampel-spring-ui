package de.projectdw.springfxui;


import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringFxUiApplication {
    public static void main(String[] args) {
        Application.launch(ChartApplication.class, args);
    }

}
