package org.teamfour.display.components.admin;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import org.teamfour.display.Form;
import org.teamfour.display.KeyPressListener;
import org.teamfour.display.components.keyboard.HexKeyboard;
import org.teamfour.display.resolver.data.ResolutionRequest;
import org.teamfour.display.resolver.Resolver;
import org.teamfour.display.util.ColorProvider;
import org.teamfour.display.util.KeyCodes;


public class AdminSignIn extends VBox implements KeyPressListener, Form {

    private static final double SIGN_IN_SPACING = 20;
    private final PasswordField passwordField;
    private final TextField usernameField;
    private final Button formSubmit;
    private final HexKeyboard keypad;
    private Resolver resolver;

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
                resolver.handle(new ResolutionRequest());
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
        if (!passwordField.isFocused() && !usernameField.isFocused()){
            return;
        } else if (!KeyCodes.HEX_ALPHA_NUM.contains(key)
                    && !key.equals(KeyCodes.ENTER)
                        && !key.equals(KeyCodes.DELETE)) {
            return;
        }

        TextField focused = passwordField.isFocused() ?
                            passwordField : usernameField;
        switch (key) {
            case KeyCodes.ENTER ->  {}
            case KeyCodes.DELETE -> focused.deletePreviousChar();
            default -> focused.appendText(key);
        }
    }

    @Override
    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void invalid() {
        String style = """
                -fx-background-color:#a94442,linear-gradient(to bottom, #f2dede 0px, #e7c3c3 100%);
                -fx-background-insets: 0, 1;
                -fx-background-radius: 0, 0;
                -fx-text-fill: #BD0300FF;
                """;
        usernameField.clear();
        passwordField.clear();
        usernameField.setStyle(style);
        passwordField.setStyle(style);
    }

    @Override
    public void valid() {
        String style = """
                -fx-background-color:#3c763d, linear-gradient(to bottom, #dff0d8 0px, #c8e5bc 100%);
                -fx-background-insets: 0, 2;
                -fx-background-radius: 0, 0;
                -fx-text-fill: #086c00;
                """;
        usernameField.clear();
        passwordField.clear();
        usernameField.setStyle(style);
        passwordField.setStyle(style);
    }

    @Override
    public void clear() {
        usernameField.clear();
        passwordField.clear();
    }

}
