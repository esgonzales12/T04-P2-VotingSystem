package org.teamfour.display;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.teamfour.dao.VotingDao;
import org.teamfour.display.components.VoterLogin;
import org.teamfour.display.components.voting.VoteCastingDisplay;
import org.teamfour.display.components.voting.CandidateCard;
import org.teamfour.display.enums.ResponseType;
import org.teamfour.display.manager.DisplayManager;
import org.teamfour.display.data.ResolutionRequest;
import org.teamfour.display.data.ResolutionResponse;
import org.teamfour.model.db.Ballot;
import org.teamfour.system.enums.Operation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Gui extends Application {

    String[] politicians = {
            "Joe Biden, Democratic Party",
            "Kamala Harris, Democratic Party",
            "Nancy Pelosi, Democratic Party",
            "Chuck Schumer, Democratic Party",
            "Mitch McConnell, Republican Party",
            "Kevin McCarthy, Republican Party",
            "Liz Cheney, Republican Party",
            "Ted Cruz, Republican Party",
            "Bernie Sanders, Independent",
            "Alexandria Ocasio-Cortez, Democratic Party"
    };

    DisplayManager manager = new DisplayManager() {
        @Override
        public ResolutionResponse resolve(ResolutionRequest request) {
            System.out.println(request.toString());
            return new ResolutionResponse(ResponseType.SUCCESS);
        }

        @Override
        public void dispatchOperation(Operation operation) {

        }

        @Override
        public void handleNotification() {

        }
    };

    private Integer fontSize = 17;

    @Override
    public void start(Stage primaryStage) {


        final org.teamfour.model.db.Ballot sample = getBallot();

        if (sample == null) System.exit(0);
        StackPane root = new StackPane();
        VoteCastingDisplay display = new VoteCastingDisplay(sample, manager);
        VoterLogin login = new VoterLogin(manager);
        root.getChildren().addAll(display, login);

        Scene scene = new Scene(root, 800, 700);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        scene.getStylesheets().add(getClass().getResource("/custom_styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
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

}
