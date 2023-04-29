package org.teamfour.display;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.teamfour.dao.VotingDao;
import org.teamfour.display.components.ballot.CandidateCard;
import org.teamfour.display.components.ballot.ItemDisplay;
import org.teamfour.model.bsl.Ballot;
import org.teamfour.model.db.Item;
import org.teamfour.model.db.Section;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private Integer fontSize = 17;

    @Override
    public void start(Stage primaryStage) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        org.teamfour.model.db.Ballot sample = null;
        String path = "src/main/resources/sample/ballot.json";
        VotingDao votingDao = new VotingDao();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            Ballot ballot = gson.fromJson(br, Ballot.class);
            sample = votingDao.saveBallot(ballot);
            System.out.println(ballot);
        }  catch (IOException ignored) {}
        if (sample == null) System.exit(0);


        List<ItemDisplay> displays = new ArrayList<>();

        for (Section section: sample.getSections()) {
            for (Item item: section.getItems()) {
                ItemDisplay display = new ItemDisplay(item, section.getName());
                displays.add(display);
            }
        }


        System.out.println(displays.size());

        HBox root = new HBox();
        Pagination display = new Pagination(displays.size(), 0);
        display.prefWidthProperty().bind(root.widthProperty());
        display.setPageFactory(displays::get);
        root.getChildren().add(display);

        Scene scene = new Scene(root, 800, 700);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        scene.getStylesheets().add(getClass().getResource("/custom_styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private CandidateCard getOption() {
        Text header = new Text("Jeffrey McDonald The Great III\n");
        header.getStyleClass().addAll("h3");
        Text body = new Text("Republican");
        body.getStyleClass().setAll("p","strong");
        CandidateCard option = new CandidateCard("Jeffrey McDonald The Great III","Dem",  1);
        option.getStyleClass().setAll("alert");
        option.setStyle("-fx-padding: 5px;");
        option.setOnMouseClicked(event -> {
            if (option.isSelected()) {
                option.deselect();
            }
            else option.select();
        });
        option.setMaxWidth(300);
        option.setMinWidth(300);
        return option;
    }
}
