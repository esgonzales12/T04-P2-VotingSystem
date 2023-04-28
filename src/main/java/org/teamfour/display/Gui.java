package org.teamfour.display;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.teamfour.display.components.AdminMenu;
import org.teamfour.display.manager.ManagerImpl;
import org.teamfour.system.enums.Operation;

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

    @Override
    public void start(Stage primaryStage) {
        VBox flowPane = new VBox();
        Accordion accordion = new Accordion();
        Label [] labels = new Label[10];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label();
            labels[i].setStyle("-fx-font-size: 17;");
            labels[i].setText(String.format("""
                    %s
                    %s
                    """, politicians[i].split(",")[0], politicians[i].split(",")[1]));
            labels[i].getStyleClass().setAll("alert");
            labels[i].setMaxWidth(300);
            labels[i].setGraphic(new FontIcon(FontAwesome.CHECK));
            labels[i].setGraphic(null);
        }
        flowPane.getChildren().addAll(labels);
//        AdminMenu root = new AdminMenu(new ManagerImpl(), List.of(Operation.INITIATE_VOTING, Operation.BALLOT_CONFIGURATION, Operation.VOTER_RESET));
        Scene scene = new Scene(flowPane, 800, 700);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
