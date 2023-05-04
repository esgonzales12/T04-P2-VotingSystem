package org.teamfour.display;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.teamfour.display.components.common.IdleScreen;
import org.teamfour.display.components.common.LoadingScreen;
import org.teamfour.display.components.voting.SampleVoteCastingDisplay;
import org.teamfour.display.components.voting.VoterLogin;
import org.teamfour.display.components.admin.AdminMenu;
import org.teamfour.display.components.admin.DualLoginPage;
import org.teamfour.display.components.voting.VoteCastingDisplay;
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

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.teamfour.system.enums.SystemRequestType;
import org.teamfour.system.enums.SystemResponseType;

public class DisplayManagerImpl extends StackPane implements DisplayManager {
    private final Logger log;
    private final Deque<Node> displayStack;
    private final VotingSystem votingSystem;
    private final AtomicBoolean doorClosed;
    private final AtomicBoolean deviceConnected;
    private Ballot ballot;

    public DisplayManagerImpl(VotingSystem votingSystem) {
        this.log = LogManager.getLogger(DisplayManagerImpl.class.getName());
        this.displayStack = new ArrayDeque<>();
        this.votingSystem = votingSystem;
        this.doorClosed = new AtomicBoolean(false);
        this.deviceConnected = new AtomicBoolean(false);
        this.ballot = null;
        init();
    }

    private void init() {
        LoadingScreen startupScreen = new LoadingScreen();
        startupScreen.setBackground(Background.fill(Color.BLACK));
        startupScreen.setText("Voting System - Start Up");
        clearAndPush(startupScreen);
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

    private void handleVoteCast(ResolutionRequest request) {
        SystemResponse response = votingSystem.handleRequest(new SystemRequest.Builder()
                .withType(SystemRequestType.CAST_VOTE)
                .withVotes(request.getVotes())
                .build());
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
                        .withType(SystemRequestType.ADMIN_LOGIN)
                        .withAdminUsername(request.getAdminUsername())
                        .withAdminPassword(request.getAdminPassword())
                        .build());
        if (ballot == null) {
            ballot = votingSystem.getBallot();
        }
        Platform.runLater(() -> {
            if (response.getResponseType() == SystemResponseType.FAILURE) {
                PlaceHolder placeHolder = new PlaceHolder();
                placeHolder.text.setText("Unable to perform login, please try again or speak to a polling official.");
                placeHolder.exit.setOnMouseClicked(exit -> clearAndPush(new VoteCastingDisplay(ballot, this)));
                clearAndPush(placeHolder);
            }
            clearAndPush(new VoteCastingDisplay(ballot, this));
        });
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
                deviceConnected.set(true);
                AdminMenu adminMenu = new AdminMenu(this,
                        AllowedOperations.ALLOWED_OPERATIONS.get(systemStatus()));
                clearAndPush(adminMenu);
            }
            case DEVICE_DISCONNECT -> {
                getChildren().clear();
                deviceConnected.set(false);
                statusDispatch(systemStatus());
            }
        }
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
                // TODO: CREATE VOTE COUNT VIEW
                clearAndPush(new IdleScreen(Status.VOTE_COUNTING));
            }
        }
    }

    private void clearAndPush(Node node) {
        getChildren().clear();
        displayStack.add(node);
        getChildren().add(node);
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
            EventHandler<MouseEvent> continueHandler = operation == Operation.CONFIGURATION ?
                    exit -> clearAndPush(new SampleVoteCastingDisplay(ballot, this))
                        : exit -> resolve(new ResolutionRequest.Builder().withType(RequestType.OPERATION_EXIT).build());
            placeHolder.exit.setOnMouseClicked(continueHandler);
        }
        Platform.runLater(() -> clearAndPush(placeHolder));
    }

    private Status systemStatus() {
        return votingSystem.getSystemMetadata().getStatus();
    }
}
