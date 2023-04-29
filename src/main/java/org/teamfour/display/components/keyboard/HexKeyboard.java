package org.teamfour.display.components.keyboard;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.teamfour.display.KeyPressListener;
import org.teamfour.display.util.Icons;
import org.teamfour.display.util.KeyCodes;

public class HexKeyboard extends VBox {

    private static final double KEY_SIZE = 50;
    private static final double KEY_SPACING = 2.5;
    private final HBox[] rows;
    private KeyPressListener listener;

    public HexKeyboard() {
        rows = new HBox[6];
        init();
    }

    public void setListener(KeyPressListener listener) {
        this.listener = listener;
    }

    private void init() {
        initRows();
        initKeys();
        getChildren().addAll(rows);
        setSpacing(KEY_SPACING);
    }

    private void initKeys() {
        int k = 0;
        for (int i = 0; i < rows.length - 1; i++) {
            for (int j = 0; j < 3; j++) {
                String keyText = String
                        .valueOf(KeyCodes.HEX_ALPHA_NUM.charAt(k++));
                rows[i].getChildren().add(issueKey(keyText));
            }
        }
        rows[5].getChildren().add(issueFunctionKey(KeyCodes.DELETE));
        rows[5].getChildren().add(issueKey("0"));
        Label spacer = issueFunctionKey("NONE");
        spacer.setVisible(false);
        rows[5].getChildren().add(spacer);
    }

    private void initRows() {
        for (int i = 0; i < rows.length; i++) {
            HBox row = new HBox();
            row.setSpacing(KEY_SPACING);
            row.setAlignment(Pos.CENTER);
            rows[i] = row;
        }
    }

    private EventHandler<MouseEvent> keyHandler(String text) {
        return click -> {
            if (listener != null) {
                listener.receiveKey(text);
            }
        };
    }

    private Label issueKey(String text) {
        Label key = new Label(text);
        key.getStyleClass().setAll("h-key", "key-default");
        key.setMinWidth(KEY_SIZE);
        key.setMinHeight(KEY_SIZE);
        key.setOnMouseClicked(keyHandler(text));
        key.setAlignment(Pos.CENTER);
        return key;
    }

    private Label issueFunctionKey(String keyType) {
        Label key = new Label();
        key.getStyleClass().setAll("h-key", "key-default");
        key.setMinWidth(KEY_SIZE);
        key.setMinHeight(KEY_SIZE);
        key.setAlignment(Pos.CENTER);
        switch (keyType) {
            case KeyCodes.DELETE -> {
                key.setGraphic(new Icons.Builder(FontAwesome.CHEVRON_CIRCLE_LEFT)
                        .withSize(20)
                        .withFill(Color.valueOf("#98384f"))
                        .build());
                key.setOnMouseClicked(click -> {
                    if (listener != null) listener.receiveKey("DELETE");
                });
            }
            case KeyCodes.ENTER -> {
                key.setGraphic(Icons.ENTER_ICON);
                key.setOnMouseClicked(click -> {
                    if (listener != null) listener.receiveKey("ENTER");
                });
            }
        }
        return key;
    }

}
