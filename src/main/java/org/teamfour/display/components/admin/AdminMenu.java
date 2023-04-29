package org.teamfour.display.components.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.teamfour.display.components.InfoPopup;
import org.teamfour.display.manager.DisplayManager;
import org.teamfour.display.util.ColorProvider;
import org.teamfour.display.util.OperationDescription;
import org.teamfour.system.enums.Operation;

import java.util.List;

public class AdminMenu extends StackPane {
    private final DisplayManager displayManager;
    private final VBox menu;
    private final VBox contextDisplay;
    private final InfoPopup popup;

    public AdminMenu(DisplayManager displayManager, List<Operation> operations) {
        this.displayManager = displayManager;
        this.menu = new VBox();
        this.contextDisplay = new VBox();
        this.popup = new InfoPopup();
        init(operations);
    }

    private void init(List<Operation> operations) {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        for (Operation operation : operations) {
            AdminMenuComponent component = AdminMenuComponent.Factory.buildComponent(operation);
            component.operationButton.setOnMouseClicked(click -> {
                displayManager.dispatchOperation(operation);
            });
            component.contextPopup.setOnMouseClicked(event -> {
                popup.setContent(OperationDescription.DESCRIPTIONS.get(operation));
                popup.setTitle(component.operationButton.getText());
                popup.setVisible(true);
            });
            menu.getChildren().add(component);
        }

        menu.setAlignment(Pos.CENTER);
        menu.prefWidthProperty().bind(container.widthProperty().multiply(0.7));
        menu.setSpacing(10);

        contextDisplay.setAlignment(Pos.CENTER);
        contextDisplay.setBackground(Background.fill(ColorProvider.THEME_5));
        contextDisplay.setPadding(new Insets(20));
        contextDisplay.prefWidthProperty().bind(container.widthProperty().multiply(0.3));

        FontIcon globe = new FontIcon(FontAwesome.GLOBE);
        globe.setIconSize(50);
        contextDisplay.getChildren().add(globe);

        Text title = new Text("Team 4 Voting System");
        Text sub = new Text("Administrative Menu");

        title.getStyleClass().setAll("h1");
        sub.getStyleClass().setAll("h2");

        contextDisplay.getChildren().addAll(title, sub);
        container.getChildren().addAll(contextDisplay, menu);
        getChildren().addAll(container, popup);
    }

}
