package org.teamfour.display.components.admin;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.teamfour.display.components.LoadingScreen;
import org.teamfour.display.data.ResolutionRequest;
import org.teamfour.display.data.ResolutionResponse;
import org.teamfour.display.enums.RequestType;
import org.teamfour.display.enums.ResponseType;
import org.teamfour.display.manager.DisplayManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DualLoginPage extends Panel {
    private static final int USERS_REQUIRED = 2;
    private final DisplayManager displayManager;
    private Map<Integer, AdminSignIn> signInPages;
    private int signInCount;
    private StackPane content;
    private HBox footerNotification;
    private Text authorizedUsersLabel;
    private Text footerText;
    private List<String> currentlyAuthorized;

    public DualLoginPage(DisplayManager displayManager) {
        this.displayManager = displayManager;
        this.signInCount = 0;
        this.signInPages = new HashMap<>();
        this.content = new StackPane();
        this.currentlyAuthorized = new ArrayList<>();
        this.footerNotification = new HBox();
        this.footerText = new Text();
        this.authorizedUsersLabel = new Text();
        init();
    }

    private void init() {
        int tempCount = USERS_REQUIRED;
        while (tempCount > 0) {
            AdminSignIn signIn = new AdminSignIn();
            signIn.formSubmit.setOnMouseClicked(onFormSubmit(signIn));
            signInPages.put(tempCount, signIn);
            content.getChildren().add(signIn);
            tempCount--;
        }

        authorizedUsersLabel.setText(String.format("0/%d users authorized", USERS_REQUIRED));
        authorizedUsersLabel.getStyleClass().setAll("p", "strong");
        authorizedUsersLabel.setFill(Color.WHITE);
        footerText.setText("Two polling officials are required for authorization");
        footerNotification.getStyleClass().setAll("alert");
        footerNotification.getChildren().add(footerText);
        footerNotification.setVisible(true);

        setBody(content);
        setHeading(getHeader());
        setFooter(footerNotification);
        getStyleClass().setAll("login", "login-panel");
    }

    private EventHandler<MouseEvent> onFormSubmit(AdminSignIn signInScreen) {
        return submit -> {
            String username = signInScreen.usernameField.getText();
            String password = signInScreen.passwordField.getText();

            if (invalidUsername(username)
                    || invalidPassword(password)
                    || displayManager == null) {
                handleInvalid(signInScreen.usernameField, signInScreen.passwordField);
                return;
            }

            ResolutionResponse response = displayManager.resolve(
                    new ResolutionRequest.Builder()
                            .withType(RequestType.ADMIN_LOGIN)
                            .withAdminUsername(username)
                            .withAdminPassword(password)
                            .build());

            if (response.getResponseType() == ResponseType.SUCCESS) {
                handleLoginApproved(username);
            } else {
                handleInvalid(signInScreen.usernameField, signInScreen.passwordField);
            }
        };
    }

    private boolean invalidUsername(String username) {
        return currentlyAuthorized.contains(username)
                || username.length() <= 4
                || StringUtils.isEmpty(username);
    }

    private boolean invalidPassword(String password) {
        return password.length() <= 4
                || StringUtils.isEmpty(password);
    }

    private void handleInvalid(TextField username, TextField password) {
        ParallelTransition parallelTransition
                = new ParallelTransition(getTransition(username), getTransition(password));
        parallelTransition.play();
        footerNotification.getStyleClass().setAll("alert", "alert-danger");
        footerText.setText("Invalid username or password");
        footerNotification.setVisible(true);
    }

    private void handleLoginApproved(String username) {
        signInCount += 1;
        signInPages.get(signInCount).clear();
        signInPages.get(signInCount).setVisible(false);

        currentlyAuthorized.add(username);
        authorizedUsersLabel.setText(String
                .format("%d/%d users authorized", signInCount, USERS_REQUIRED));

        footerNotification.getStyleClass().setAll("alert", "alert-success");
        footerText.setText(String
                .format("User %d successfully signed in", signInCount));

        footerNotification.setVisible(true);

        if (signInCount >= USERS_REQUIRED) {
            content.getChildren().add(new LoadingScreen());
            displayManager.resolve(new ResolutionRequest.Builder()
                            .withType(RequestType.ADMIN_LOGIN_COMPLETE)
                            .build());
        }

    }

    private HBox getHeader() {
        HBox headerContainer = new HBox();
        Text header = new Text("Admin Sign In\n");
        Text description = new Text("Please enter your username and password\n");
        header.setFill(Color.WHITE);
        description.setFill(Color.WHITE);
        header.getStyleClass().setAll("h2", "strong");
        description.getStyleClass().setAll("p", "strong");
        headerContainer.getChildren().addAll(new TextFlow(header, description, authorizedUsersLabel));
        return headerContainer;
    }

    private TranslateTransition getTransition(TextField textField) {
        TranslateTransition transition
                = new TranslateTransition(Duration.millis(100), textField);
        transition.setByX(5);
        transition.setAutoReverse(true);
        transition.setCycleCount(4);
        return transition;
    }

}
