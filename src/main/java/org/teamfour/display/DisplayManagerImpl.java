package org.teamfour.display;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.teamfour.display.components.admin.AdminMenu;
import org.teamfour.display.components.admin.DualLoginPage;
import org.teamfour.display.components.common.IdleScreen;
import org.teamfour.display.components.common.LoadingScreen;
import org.teamfour.display.components.voting.SampleVoteCastingDisplay;
import org.teamfour.display.components.voting.VoteCastingDisplay;
import org.teamfour.display.components.voting.VoterLogin;
import org.teamfour.display.data.ResolutionRequest;
import org.teamfour.display.data.ResolutionResponse;
import org.teamfour.display.enums.Notification;
import org.teamfour.display.enums.RequestType;
import org.teamfour.display.enums.ResponseType;
import org.teamfour.display.util.AllowedOperations;
import org.teamfour.display.util.OperationPrompts;
import org.teamfour.display.util.PlaceHolder;
import org.teamfour.model.db.Ballot;
import org.teamfour.system.VotingSystem;
import org.teamfour.system.data.SystemRequest;
import org.teamfour.system.data.SystemResponse;
import org.teamfour.system.enums.Operation;
import org.teamfour.system.enums.Status;
import org.teamfour.system.enums.SystemRequestType;
import org.teamfour.system.enums.SystemResponseType;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicBoolean;

public class DisplayManagerImpl extends StackPane implements DisplayManager {
    private final Logger log;
    private final Deque<Node> displayStack;
    private final VotingSystem votingSystem;
    private final AtomicBoolean doorClosed;
    private final AtomicBoolean deviceConnected;
    private Ballot ballot;
    private String currentVoterCode;

    public DisplayManagerImpl(VotingSystem votingSystem) {
        this.log = LogManager.getLogger(DisplayManagerImpl.class.getName());
        this.displayStack = new ArrayDeque<>();
        this.votingSystem = votingSystem;
        this.doorClosed = new AtomicBoolean(false);
        this.deviceConnected = new AtomicBoolean(false);
        this.ballot = null;
        this.currentVoterCode = null;
        init();
    }

    @Override
    public ResolutionResponse resolve(ResolutionRequest request) {
        log.info("RESOLUTION REQUEST RECEIVED: " + request.getType());
        switch (request.getType()) {
            case FINALIZE -> votingSystem.handleRequest(new SystemRequest.Builder()
                                    .withType(SystemRequestType.VOTE_FINALIZE)
                                    .withVotes(request.getVotes())
                                    .build());
            case CAST_VOTE -> handleVoteCast(request);
            case VOTER_EXIT -> clearAndPush(new VoterLogin(this));
            case VOTER_LOGIN -> handleVoterLogin(request);
            case ADMIN_LOGIN -> {
                SystemResponse response = votingSystem.handleRequest(new SystemRequest.Builder()
                                .withType(SystemRequestType.ADMIN_LOGIN)
                                .withAdminUsername(request.getAdminUsername())
                                .withAdminPassword(request.getAdminPassword())
                                .build());
                ResponseType success = response.getResponseType() == SystemResponseType.FAILURE ?
                        ResponseType.FAILURE : ResponseType.SUCCESS;
                return new ResolutionResponse(success);
            }
            case ADMIN_LOGIN_COMPLETE -> {
                LoadingScreen loadingScreen = new LoadingScreen();
                loadingScreen.setText("Performing operation: " + request.getOperation());
                clearAndPush(loadingScreen);
                long start = System.nanoTime();
                AnimationTimer animationTimer = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        if (Duration.ofNanos(now - start).toSeconds() > 1) {
                            performOperation(request.getOperation());
                            stop();
                        }
                    }
                };
                animationTimer.start();
            }
            case OPERATION_EXIT -> {
                AdminMenu adminMenu = new AdminMenu(this,
                        AllowedOperations.ALLOWED_OPERATIONS.get(systemStatus()));
                clearAndPush(adminMenu);
            }
        }
        return null;
    }

    @Override
    public void dispatchOperation(Operation operation) {
        log.info("DISPATCHING: " + operation);
        if (operation == Operation.BEGIN_VOTE_COUNTING) {
            ballot = null;
        }
        clearAndPush(new DualLoginPage(operation, this));
    }

    @Override
    public void handleNotification(Notification notification) {
        log.info("RECEIVED NOTIFICATION: " + notification);
        switch (notification) {
            case DOOR_OPEN -> {
                doorClosed.set(false);
                Status status = systemStatus();
                if (status == Status.IN_PROCESS) {
                    // TODO: LOCKOUT SCREEN
                }
            }
            case STARTUP_COMPLETE -> {
                PlaceHolder placeHolder = new PlaceHolder();
                placeHolder.text.setText("System Startup Complete :)");
                placeHolder.exit.setOnMouseClicked(exit -> statusDispatch(systemStatus()));
                clearAndPush(placeHolder);
            }
            case DOOR_CLOSE -> {
                doorClosed.set(true);
                statusDispatch(systemStatus());
            }
            case DEVICE_CONNECT -> {
                boolean valid = votingSystem.validDeviceConnected();
                if (valid) {
                    deviceConnected.set(true);
                    AdminMenu adminMenu = new AdminMenu(this,
                            AllowedOperations.ALLOWED_OPERATIONS.get(systemStatus()));
                    clearAndPush(adminMenu);
                } else {
                    PlaceHolder placeHolder = new PlaceHolder();
                    placeHolder.text.setText("Invalid device connected, please contact vendor.");
                    placeHolder.exit.setOnMouseClicked(event -> statusDispatch(systemStatus()));
                }
            }
            case DEVICE_DISCONNECT -> {
                getChildren().clear();
                deviceConnected.set(false);
                statusDispatch(systemStatus());
            }
        }
    }

    private void handleVoteCast(ResolutionRequest request) {
        SystemResponse response = votingSystem.handleRequest(new SystemRequest.Builder()
                .withType(SystemRequestType.CAST_VOTE)
                .withVotes(request.getVotes())
                .withVoterAccessCode(currentVoterCode == null ? "" : currentVoterCode)
                .build());
        log.info("VOTE CASTING RECEIVED RESPONSE:" + response.toString());
        Platform.runLater(() -> {
            PlaceHolder placeHolder = new PlaceHolder();
            placeHolder.text.setText(response.getResponseType() == SystemResponseType.FAILURE ?
                    "An error occurred while recording your vote. Please speak to a polling official."
                    : "Your vote has been recorded.");
            placeHolder.exit.setOnMouseClicked(exit -> clearAndPush(new VoterLogin(this)));
            clearAndPush(placeHolder);
        });
    }

    private void handleVoterLogin(ResolutionRequest request) {
        SystemResponse response = votingSystem.handleRequest(new SystemRequest.Builder()
                .withType(SystemRequestType.VOTER_LOGIN)
                .withVoterAccessCode(request.getVoterAccessCode())
                .build());
        if (ballot == null) {
            ballot = votingSystem.getBallot();
        }
        Platform.runLater(() -> {
            if (response.getResponseType() == SystemResponseType.FAILURE) {
                PlaceHolder placeHolder = new PlaceHolder();
                placeHolder.text.setText("Unable to perform login, please try again or speak to a polling official.");
                placeHolder.exit.setOnMouseClicked(exit -> clearAndPush(new VoterLogin(this)));
                clearAndPush(placeHolder);
            } else {
                currentVoterCode = request.getVoterAccessCode();
                clearAndPush(new VoteCastingDisplay(ballot, this));
            }
        });
    }

    private void statusDispatch(Status status) {
        switch (status) {
            case PRE_ELECTION, POST_ELECTION -> {
                getChildren().clear();
                getChildren().add(new IdleScreen(status));
            }
            case IN_PROCESS -> {
                getChildren().clear();
                VoterLogin voterLogin = new VoterLogin(this);
                getChildren().add(voterLogin);
            }
            case VOTE_COUNTING -> {
                Ballot countBallot = votingSystem.getBallot();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String ballotJson = gson.toJson(countBallot);
                ScrollPane scrollPane = new ScrollPane();
                try {
                    JsonNode jsonNodeTree = new ObjectMapper().readTree(ballotJson);
                    String ballotYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);
                    Text title = new Text("Vote Tabulation Results\n\n");
                    title.getStyleClass().setAll("h1", "strong");
                    title.setFill(Color.WHITE);

                    Text count = new Text(ballotYaml);
                    count.getStyleClass().setAll("h4", "code", "strong");
                    count.setFill(Color.WHITE);

                    VBox box = new VBox(new TextFlow(title, count));
                    box.setAlignment(Pos.CENTER);
                    box.setBackground(Background.fill(Color.valueOf("#006ee5")));
                    scrollPane.setContent(box);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                }
                scrollPane.setFitToHeight(true);
                scrollPane.setFitToWidth(true);

                clearAndPush(scrollPane);
            }
        }
    }

    private void performOperation(Operation operation) {
        PlaceHolder placeHolder = new PlaceHolder();
        SystemResponse response = votingSystem.handleRequest(new SystemRequest.Builder()
                .withType(SystemRequestType.OPERATION)
                .withOperation(operation)
                .build());
        if (response.getResponseType() == SystemResponseType.FAILURE) {
            placeHolder.text.setText("Unable to complete operation " + operation + " at this time.");
            placeHolder.exit.setOnMouseClicked(exit -> resolve(new ResolutionRequest.Builder()
                    .withType(RequestType.OPERATION_EXIT)
                    .build()));
        } else {
            placeHolder.text.setText(OperationPrompts.COMPLETION_PROMPTS.get(operation));
            if (operation == Operation.CONFIGURATION) ballot = votingSystem.getBallot();
            EventHandler<MouseEvent> continueHandler = operation == Operation.CONFIGURATION ?
                    exit -> clearAndPush(new SampleVoteCastingDisplay(ballot, this))
                    : exit -> resolve(new ResolutionRequest.Builder().withType(RequestType.OPERATION_EXIT).build());
            placeHolder.exit.setOnMouseClicked(continueHandler);
        }
        Platform.runLater(() -> clearAndPush(placeHolder));
    }

    private void init() {
        LoadingScreen startupScreen = new LoadingScreen();
        startupScreen.setBackground(Background.fill(Color.BLACK));
        startupScreen.setText("Voting System - Start Up");
        clearAndPush(startupScreen);
    }

    private void clearAndPush(Node node) {
        getChildren().clear();
        displayStack.add(node);
        getChildren().add(node);
    }

    private Status systemStatus() {
        return votingSystem.getSystemMetadata().getStatus();
    }
}
