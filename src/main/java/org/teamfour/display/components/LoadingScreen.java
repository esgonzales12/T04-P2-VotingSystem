package org.teamfour.display.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.teamfour.display.util.ColorTheme;

public class LoadingScreen extends VBox {
    private String text;
    private Label label;

    public LoadingScreen() {
        label = new Label();
        label.getStyleClass().setAll("h2", "strong");
        label.setTextFill(Color.WHITE);
        ProgressIndicator indicator = new ProgressIndicator(-1);
        indicator.setPrefSize(250, 250);
        indicator.setMinWidth(250);
        indicator.setMinHeight(250);
        setBackground(Background.fill(ColorTheme.POPUP_BG));
        setAlignment(Pos.CENTER);
        getChildren().add(indicator);
        getChildren().add(label);
        translateXProperty().set(0.0);
        label.setVisible(false);
        setSpacing(15);
    }

    public void setText(String text) {
        label.setText(text);
        label.setVisible(true);
    }
}
