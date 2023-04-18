package org.teamfour.display.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.teamfour.display.util.ColorProvider;
import org.teamfour.system.enums.Operation;

public class AdminMenuComponent extends HBox {
    public final Button operationButton;
    public final FontIcon contextPopup;

    public AdminMenuComponent(Button operationButton, FontIcon contextPopup) {
        this.operationButton = operationButton;
        this.contextPopup = contextPopup;
    }

    public static class Factory {
        public static AdminMenuComponent buildComponent(Operation operation) {
            Button operationButton = new Button();
            FontIcon info = new FontIcon(FontAwesome.INFO_CIRCLE);
            AdminMenuComponent component = new AdminMenuComponent(operationButton, info);
            component.setSpacing(10);

            operationButton.setFocusTraversable(false);
            operationButton.prefWidthProperty().bind(component.widthProperty().multiply(0.7));
            operationButton.getStyleClass().setAll("btn", "btn-lg", "btn-default");
            operationButton.setAlignment(Pos.CENTER);
            component.setAlignment(Pos.CENTER);

            info.iconSizeProperty().bind(operationButton.heightProperty().multiply(0.8));
            info.setFill(Color.TRANSPARENT);
            info.setStroke(ColorProvider.THEME_4);
            info.setOnMouseEntered(event -> info.setStroke(ColorProvider.THEME_5));
            info.setOnMouseExited(event -> info.setStroke(ColorProvider.THEME_4));
            info.setOnMousePressed(e -> info.setFill(ColorProvider.THEME_4));
            info.setOnMouseReleased(e -> info.setFill(Color.TRANSPARENT));
            info.setStrokeWidth(1.5);

            switch (operation) {
                case INITIATE_VOTING -> operationButton.setText("Initiate Voting Window");
                case VOTER_RESET -> operationButton.setText("Reset Voter Progress");
                case BALLOT_CONFIGURATION -> operationButton.setText("Initiate Ballot Configuration");
            }
            component.getChildren().addAll(info, operationButton);
            return component;
        }
    }
}
