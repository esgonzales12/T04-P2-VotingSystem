package org.teamfour.display.components.common;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.teamfour.display.util.ColorTheme;

public class InfoPopup extends HBox {
    public final Button exitButton;
    public final Panel content;

    public InfoPopup() {
        this.exitButton = new Button("DONE");
        this.content = new Panel();
        init();
    }

    public void setContent(String text) {
        content.setBody(new TextFlow(new Text(text)));
    }

    public void setTitle(String title) {
        content.setText(title);
    }

    private void init() {
        setBackground(Background.fill(ColorTheme.POPUP_BG));
        setAlignment(Pos.CENTER);

        exitButton.setAlignment(Pos.CENTER);
        exitButton.getStyleClass().setAll("btn", "btn-default");
        exitButton.prefWidthProperty().bind(content.widthProperty());
        exitButton.setOnMouseClicked(e -> this.setVisible(false));

        content.setFooter(exitButton);
        content.prefWidthProperty().bind(this.widthProperty().multiply(0.7));
        content.maxHeightProperty().bind(this.heightProperty().multiply(0.1));
        content.getStyleClass().add("panel-default");
        getChildren().add(content);
        setVisible(false);

    }


}
