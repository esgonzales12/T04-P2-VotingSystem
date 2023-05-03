package org.teamfour.display;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.teamfour.dao.VotingDao;
import org.teamfour.display.components.VoterLogin;
import org.teamfour.display.components.admin.DualLoginPage;
import org.teamfour.display.components.voting.CandidateCard;
import org.teamfour.display.components.voting.VoteCastingDisplay;
import org.teamfour.display.enums.Notification;
import org.teamfour.model.db.Ballot;
import org.teamfour.system.SystemRequest;
import org.teamfour.system.SystemResponse;
import org.teamfour.system.VotingSystem;
import org.teamfour.system.enums.Status;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Gui extends Application {

    Ballot ballot;
    VotingSystem votingSystem = new VotingSystem() {
        private Status status = Status.PRE_ELECTION;

        @Override
        public Status getStatus() {
            return status;
        }

        @Override
        public Ballot getBallot() {
            return ballot == null? getBallot() : ballot;
        }

        @Override
        public void setStatus(Status status) {
            this.status = status;
        }

        @Override
        public SystemResponse handleRequest(SystemRequest request) {
            return null;
        }
    };

    private Integer fontSize = 17;

    @Override
    public void start(Stage primaryStage) {
        ballot = getBallot();
        DisplayManagerImpl displayManager = new DisplayManagerImpl(votingSystem);

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
        BorderPane root = new BorderPane();
        root.setCenter(displayManager);
        root.setBottom(notificationPane);

        Scene scene = new Scene(root, 1000, 900);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        scene.getStylesheets().add(getClass().getResource("/custom_styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        final long curr = System.nanoTime();
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (Duration.ofNanos(now - curr).toSeconds() > 1) {
                    displayManager.handleNotification(Notification.STARTUP_COMPLETE);
                    stop();
                }
            }
        };
        animationTimer.start();
    }

    private Ballot getBallot() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String path = "src/main/resources/sample/ballot.json";
        VotingDao votingDao = new VotingDao();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            org.teamfour.model.bsl.Ballot ballot = gson.fromJson(br, org.teamfour.model.bsl.Ballot.class);
            return votingDao.saveBallot(ballot);
        } catch (IOException ignored) {
        }
        return null;
    }

    private CandidateCard getOption() {
        Text header = new Text("Jeffrey McDonald The Great III\n");
        header.getStyleClass().addAll("h3");
        Text body = new Text("Republican");
        body.getStyleClass().setAll("p", "strong");
        CandidateCard option = new CandidateCard("Jeffrey McDonald The Great III", "Dem", 1);
        option.getStyleClass().setAll("alert");
        option.setStyle("-fx-padding: 5px;");
        option.setOnMouseClicked(event -> {
            if (option.isSelected()) {
                option.deselect();
            } else option.select();
        });
        option.setMaxWidth(300);
        option.setMinWidth(300);
        return option;
    }

    private void sample() {
        final org.teamfour.model.db.Ballot sample = getBallot();
        if (sample == null) System.exit(0);
        StackPane root = new StackPane();
        VoteCastingDisplay display = new VoteCastingDisplay(sample, null);
        VoterLogin login = new VoterLogin(null);
        DualLoginPage loginPage = new DualLoginPage(null, null);
        root.getChildren().addAll(display, login, loginPage);
    }

}
