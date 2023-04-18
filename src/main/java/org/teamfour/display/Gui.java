package org.teamfour.display;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.teamfour.display.components.AdminMenu;
import org.teamfour.display.manager.ManagerImpl;
import org.teamfour.system.enums.Operation;

import java.util.List;

public class Gui extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AdminMenu root = new AdminMenu(new ManagerImpl(), List.of(Operation.INITIATE_VOTING, Operation.BALLOT_CONFIGURATION, Operation.VOTER_RESET));
        Scene scene = new Scene(root, 800, 700);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
