package org.teamfour.display.components;

import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import org.teamfour.display.util.ColorProvider;

public class LoadingScreen extends HBox {
    public LoadingScreen() {
        ProgressIndicator indicator = new ProgressIndicator(-1);
        indicator.setPrefSize(200, 200);
        setBackground(Background.fill(ColorProvider.POPUP_BG));
        setAlignment(Pos.CENTER);
        getChildren().add(indicator);
        translateXProperty().set(0.0);
    }
}
