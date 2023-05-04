package org.teamfour;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.teamfour.display.DisplayManagerImpl;
import org.teamfour.display.enums.Notification;
import org.teamfour.system.VotingSystemImpl;

import java.util.ArrayList;
import java.util.List;

public class Demo extends Application {
    private DisplayManagerImpl displayManager;
    @Override
    public void start(Stage primaryStage) throws Exception {
        VotingSystemImpl votingSystem = new VotingSystemImpl();
        displayManager = new DisplayManagerImpl(votingSystem);

        BorderPane root = new BorderPane();
        root.setCenter(displayManager);
        root.setBottom(getInputPane());

        Scene scene = new Scene(root, 1000, 900);

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        scene.getStylesheets().add(getClass().getResource("/custom_styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private HBox getInputPane() {
        HBox notificationPane = new HBox();
        notificationPane.setAlignment(Pos.CENTER);
        List<Button> notificationButtons = new ArrayList<>();
        for (Notification notification: Notification.values()) {
            Button button = new Button(notification.toString());
            button.getStyleClass().setAll("btn", "btn-sm", "btn-default");
            button.setOnMouseClicked(event -> displayManager.handleNotification(notification));
            notificationButtons.add(button);
        }
        notificationPane.getChildren().addAll(notificationButtons);
        notificationPane.setSpacing(10);
        return notificationPane;
    }
}
