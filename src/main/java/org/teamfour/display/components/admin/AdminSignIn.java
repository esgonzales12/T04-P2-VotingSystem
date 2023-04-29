package org.teamfour.display.components.admin;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import org.teamfour.display.KeyPressListener;
import org.teamfour.display.components.keyboard.HexKeyboard;
import org.teamfour.display.data.ResolutionRequest;
import org.teamfour.display.manager.DisplayManager;
import org.teamfour.display.util.ColorProvider;
import org.teamfour.display.util.KeyCodes;


public class AdminSignIn extends VBox implements KeyPressListener {

    private static final double SIGN_IN_SPACING = 20;
    private final PasswordField passwordField;
    private final TextField usernameField;
    private final Button formSubmit;
    private final HexKeyboard keypad;
    private DisplayManager resolver;

    public AdminSignIn() {
        passwordField = new PasswordField();
        usernameField = new TextField();
        formSubmit = new Button("Submit");
        keypad = new HexKeyboard();
        init();
    }

    private void init() {

        keypad.setListener(this);
        formSubmit.getStyleClass().setAll("btn", "btn-lg", "btn-default");
        formSubmit.setOnMouseClicked(click -> {
            if (resolver != null) {
                resolver.resolve(new ResolutionRequest.Builder().build());
            }
        });

        passwordField.setFocusTraversable(false);
        passwordField.setPromptText("Enter Your Password");
        passwordField.setTextFormatter(new TextFormatter<String>(KeyCodes.HEX_FILTER));
        passwordField.maxWidthProperty().bind(this.widthProperty().multiply(0.25));
        passwordField.setOnMouseClicked(event -> {
            passwordField.setStyle(null);
            passwordField.getStyleClass().clear();
            passwordField.getStyleClass().setAll("text-field", "text-input");
        });

        usernameField.setFocusTraversable(false);
        usernameField.setPromptText("Enter Your Username");
        usernameField.setTextFormatter(new TextFormatter<String>(KeyCodes.HEX_FILTER));
        usernameField.maxWidthProperty().bind(this.widthProperty().multiply(0.25));
        usernameField.setOnMouseClicked(event -> {
            usernameField.setStyle(null);
            usernameField.getStyleClass().clear();
            usernameField.getStyleClass().setAll("text-field", "text-input");
        });

        setSpacing(SIGN_IN_SPACING);
        setAlignment(Pos.CENTER);
        setBackground(Background.fill(ColorProvider.THEME_1));
        getChildren().addAll(usernameField, passwordField, keypad, formSubmit);
    }

    @Override
    public void receiveKey(String key) {
        if (!passwordField.isFocused() && !usernameField.isFocused()) {
            return;
        } else if (!KeyCodes.HEX_ALPHA_NUM.contains(key)
                && !key.equals(KeyCodes.ENTER)
                && !key.equals(KeyCodes.DELETE)) {
            return;
        }

        TextField focused = passwordField.isFocused() ?
                passwordField : usernameField;
        switch (key) {
            case KeyCodes.ENTER -> {
            }
            case KeyCodes.DELETE -> focused.deletePreviousChar();
            default -> focused.appendText(key);
        }
    }

    public void clear() {
        usernameField.clear();
        passwordField.clear();
    }

}
