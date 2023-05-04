package org.teamfour.display.components.voting;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.teamfour.display.DisplayManager;
import org.teamfour.display.components.KeyPressListener;
import org.teamfour.display.components.keyboard.QwertyKeyboard;
import org.teamfour.display.data.ResolutionRequest;
import org.teamfour.display.enums.RequestType;
import org.teamfour.display.util.KeyCodes;

public class VoterLogin extends Panel implements KeyPressListener {
    private static final String PROMPT_TEXT = "Please Enter Your Voter Access Code";
    private final DisplayManager displayManager;
    private final QwertyKeyboard keyboard;
    private final TextField accessCodeField;
    private final VBox content;
    private boolean invalid = false;

    public VoterLogin(DisplayManager displayManager) {
        this.displayManager = displayManager;
        this.keyboard = new QwertyKeyboard();
        this.keyboard.setListener(this);
        this.accessCodeField = new TextField();
        content = new VBox();
        init();
    }

    @Override
    public void receiveKey(String key) {
        if (invalid) {
            invalid = false;
            accessCodeField.getStyleClass().setAll("alert", "strong");
        }
        switch (key) {
            case KeyCodes.ENTER -> formSubmit();
            case KeyCodes.DELETE -> accessCodeField.deletePreviousChar();
            default -> accessCodeField.appendText(key);
        }
    }

    private void formSubmit() {
        String code = accessCodeField.getText();

        if (StringUtils.isBlank(code)) {
            invalidAccessCode();
        } else if (displayManager == null) {
            System.out.println("NO MANAGER PROVIDED");
        } else {
            displayManager.resolve(new ResolutionRequest.Builder()
                                        .withType(RequestType.VOTER_LOGIN)
                                        .withAccessCode(code)
                                        .build());
        }
        accessCodeField.clear();
    }

    private void invalidAccessCode() {
        invalid = true;
        accessCodeField.getStyleClass().setAll("alert", "alert-danger", "strong");
    }

    private void init() {
        getStyleClass().setAll("login", "login-panel");
        accessCodeField.setPromptText(PROMPT_TEXT);
        accessCodeField.maxWidthProperty().bind(this.widthProperty().multiply(0.4));
        accessCodeField.getStyleClass().setAll("alert", "strong");
        content.setSpacing(30);
        content.getChildren().addAll(accessCodeField, keyboard);
        setBackground(Background.fill(Color.WHITE));
        content.setAlignment(Pos.CENTER);
        setBody(content);
        setHeading(descriptionText());
    }

    private HBox descriptionText() {
        HBox hBox = new HBox();
        Text header = new Text("Voter Login\n");
        Text description = new Text("Please enter your voter access code to begin voting");
        header.setFill(Color.WHITE);
        description.setFill(Color.WHITE);
        header.getStyleClass().setAll("h2", "strong");
        description.getStyleClass().setAll("p", "strong");
        hBox.getChildren().addAll(new TextFlow(header, description));
        return hBox;
    }
}
