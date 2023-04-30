package org.teamfour.display.components;

import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.teamfour.system.enums.Status;

public class IdleScreen extends VBox {
    public IdleScreen(Status status) {
        init(status);
    }

    private void init(Status status) {
        setAlignment(Pos.CENTER);
        setBackground(Background.fill(Color.valueOf("#006ee5")));

        FontIcon globe = new FontIcon(FontAwesome.GLOBE);
        globe.setIconSize(200);


        Text headerText = new Text("Voting System Idle\n\n");
        headerText.setFill(Color.WHITE);
        headerText.getStyleClass().setAll("h1");

        Text systemStatus = new Text("System status: " + status + "\n\n"
        + "System version: 1.0\n\n" + "Configuration Status: No ballot configured\n\n");
        systemStatus.getStyleClass().setAll("h4", "code", "strong");
        systemStatus.setFill(Color.WHITE);

        HBox container = new HBox(new TextFlow(headerText, systemStatus));
        container.maxWidthProperty().bind(this.widthProperty().multiply(0.6));
        container.setAlignment(Pos.CENTER);

        getChildren().add(globe);
        getChildren().add(container);
    }
}
