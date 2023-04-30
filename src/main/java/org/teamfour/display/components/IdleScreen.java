package org.teamfour.display.components;

import javafx.geometry.Pos;
import javafx.scene.layout.Background;
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
        globe.setIconSize(50);


        Text headerText = new Text("Voting System Idle");
        headerText.setFill(Color.WHITE);
        headerText.getStyleClass().setAll("h1");

        Text systemStatus = new Text("System status: " + status + "\n"
        + "System version: 1.0\n" + "No ballot configured\n");
        systemStatus.getStyleClass().setAll("p", "code");

        getChildren().add(globe);
        getChildren().add(new TextFlow(headerText, systemStatus));
    }
}
