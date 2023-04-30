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
import org.teamfour.display.util.ColorProvider;
import org.teamfour.display.util.KeyCodes;


public class AdminSignIn extends VBox implements KeyPressListener {

    private static final double SIGN_IN_SPACING = 10;
    private final HexKeyboard keypad;
    public final Button formSubmit;
    public final PasswordField passwordField;
    public final TextField usernameField;

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

        passwordField.setFocusTraversable(false);
        passwordField.setPromptText("Enter Your Password");
        initTextField(passwordField);

        usernameField.setFocusTraversable(false);
        usernameField.setPromptText("Enter Your Username");
        initTextField(usernameField);

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

    private void initTextField(TextField textField) {
        textField.setTextFormatter(new TextFormatter<String>(KeyCodes.HEX_FILTER));
        textField.maxWidthProperty().bind(this.widthProperty().multiply(0.35));
        textField.getStyleClass().setAll("alert", "strong");
    }

}
