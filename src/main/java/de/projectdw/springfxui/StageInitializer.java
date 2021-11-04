package de.projectdw.springfxui;

import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<UiInitializer.StageReadyEvent> {


    public StageInitializer() {
    }

    @Override
    public void onApplicationEvent(UiInitializer.StageReadyEvent event) {
        Stage stage = event.getStage();
        new HelloFX().start(stage);
        stage.show();
    }
}
