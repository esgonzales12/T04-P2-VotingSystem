package org.teamfour.display.components.keyboard;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.teamfour.display.components.KeyPressListener;
import org.teamfour.display.util.KeyCodes;

public class QwertyKeyboard extends VBox {
    private static final double KEY_SIZE = 50;
    private static final double KEY_GAP = 5;
    private static final String[] ROW_KEYS = {"1234567890", "QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM"};
    private final HBox[] rows;
    private KeyPressListener listener;

    public QwertyKeyboard() {
        rows = new HBox[5];
        init();
    }

    public void setListener(KeyPressListener listener) {
        this.listener = listener;
    }

    private void init() {
        initRows();
        for (int i = 0; i < ROW_KEYS.length; i++) {
            addKeys(rows[i], ROW_KEYS[i]);
        }
        rows[0].getChildren().add(backSpace());
        rows[2].getChildren().add(enterKey());
        rows[4].getChildren().add(spaceBar());
        getChildren().addAll(rows);
        setSpacing(KEY_GAP);
    }

    private void addKeys(HBox row, String chars) {
        for (int i = 0; i < chars.length(); i++) {
            Label key = new Label(String.valueOf(chars.charAt(i)));
            key.getStyleClass().setAll("key", "key-default");
            key.setMinWidth(KEY_SIZE);
            key.setMinHeight(KEY_SIZE);
            key.setAlignment(Pos.CENTER);
            key.setOnMouseClicked(click -> {
                if (listener != null) {
                    listener.receiveKey(key.getText());
                }
            });
            row.getChildren().add(key);
        }
    }

    private void initRows() {
        for (int i = 0; i < rows.length; i++) {
            HBox box = new HBox();
            box.setAlignment(Pos.CENTER);
            box.setSpacing(KEY_GAP);
            rows[i] = box;
        }
    }

    private Label spaceBar() {
        Label spaceBar = new Label("SPACE");
        spaceBar.getStyleClass().setAll("key", "key-default");
        spaceBar.setMinWidth(0.9 * (ROW_KEYS[0].length() * KEY_SIZE));
        spaceBar.setMinHeight(KEY_SIZE);
        spaceBar.setAlignment(Pos.CENTER);
        spaceBar.setOnMouseClicked(click -> {
            if (listener != null) {
                listener.receiveKey(" ");
            }
        });
        return spaceBar;
    }

    private Label enterKey() {
        Label enter = new Label("ENTER");
        enter.getStyleClass().setAll("key", "key-default");
        enter.setMinWidth(KEY_SIZE * 2);
        enter.setMinHeight(KEY_SIZE);
        enter.setMinHeight(KEY_SIZE);
        enter.setAlignment(Pos.CENTER);
        enter.setOnMouseClicked(click -> {
            if (listener != null) {
                listener.receiveKey(KeyCodes.ENTER);
            }
        });
        return enter;
    }

    private Label backSpace() {
        Label backSpace = new Label("DELETE");
        backSpace.getStyleClass().setAll("key", "key-default");
        backSpace.setMinWidth(KEY_SIZE * 2);
        backSpace.setMinHeight(KEY_SIZE);
        backSpace.setMinHeight(KEY_SIZE);
        backSpace.setAlignment(Pos.CENTER);
        backSpace.setOnMouseClicked(click -> {
            if (listener != null) {
                listener.receiveKey(KeyCodes.DELETE);
            }
        });
        return backSpace;
    }


}
