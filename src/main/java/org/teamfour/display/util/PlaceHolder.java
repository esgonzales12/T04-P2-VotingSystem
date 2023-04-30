package org.teamfour.display.util;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class PlaceHolder extends VBox {

    public final Button exit;
    public final Text text;

    public PlaceHolder() {
        setBackground(Background.fill(Color.valueOf("#006ee5")));
        setAlignment(Pos.CENTER);

        text = new Text("This is a placeholder page :)");
        text.getStyleClass().setAll("h1");
        text.setFill(Color.WHITE);

        exit = new Button("Continue");
        exit.getStyleClass().setAll("btn", "btn-lg", "btn-default");
        HBox container = new HBox(new TextFlow(text));
        container.maxWidthProperty().bind(this.widthProperty().multiply(0.5));

        setSpacing(40);
        getChildren().add(container);
        getChildren().add(exit);
    }
}
