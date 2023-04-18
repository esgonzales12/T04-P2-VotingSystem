package org.teamfour.display.components;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.teamfour.display.util.ColorProvider;
import org.teamfour.display.util.Icons;

public class ControlPane extends VBox {
    public final Button exitButton;
    public final Button continueButton;
    public final Button previousButton;

    public ControlPane() {
        exitButton = new Button();
        continueButton = new Button();
        previousButton = new Button();
        init();

    }

    private void init() {
        exitButton.setGraphic(new Icons.Builder(FontAwesome.REMOVE)
                .withSize(40)
                .withFill(Color.valueOf("#98384f"))
                .build());
        exitButton.setAlignment(Pos.CENTER);

        continueButton.setGraphic(new Icons.Builder(FontAwesome.CHEVRON_CIRCLE_RIGHT)
                .withSize(40)
                .withFill(Color.valueOf("#4f7849"))
                .build());
        continueButton.setDisable(true);
        continueButton.setAlignment(Pos.CENTER);

        previousButton.setGraphic(new Icons.Builder(FontAwesome.CHEVRON_CIRCLE_LEFT)
                .withSize(40)
                .withFill(ColorProvider.THEME_4)
                .build());
        previousButton.setAlignment(Pos.CENTER);
        previousButton.setDisable(true);

        setAlignment(Pos.CENTER);
        setBackground(Background.fill(ColorProvider.CONTEXT_BG));
        getChildren().addAll(exitButton, continueButton, previousButton);
    }

    public ReadOnlyDoubleProperty getButtonHeight() {
        return exitButton.heightProperty();
    }
}
