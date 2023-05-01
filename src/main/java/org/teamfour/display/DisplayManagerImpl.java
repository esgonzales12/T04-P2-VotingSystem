package org.teamfour.display;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.teamfour.display.components.IdleScreen;
import org.teamfour.display.components.LoadingScreen;
import org.teamfour.display.components.StepDisplay;
import org.teamfour.display.components.VoterLogin;
import org.teamfour.display.components.admin.AdminMenu;
import org.teamfour.display.components.admin.DualLoginPage;
import org.teamfour.display.components.voting.VoteCastingDisplay;
import org.teamfour.display.data.ResolutionRequest;
import org.teamfour.display.data.ResolutionResponse;
import org.teamfour.display.enums.Notification;
import org.teamfour.display.enums.RequestType;
import org.teamfour.display.enums.ResponseType;
import org.teamfour.display.util.AllowedOperations;
import org.teamfour.display.util.PlaceHolder;
import org.teamfour.model.db.Ballot;
import org.teamfour.system.VotingSystem;
import org.teamfour.system.enums.Operation;
import org.teamfour.system.enums.Status;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class DisplayManagerImpl extends StackPane implements DisplayManager {
    private final Logger log;
    private final Deque<Node> displayStack;
    private final Deque<StepDisplay> adminStack;
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
        adminStack = new ArrayDeque<>();
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
            case FINALIZE -> {
                // TODO: ONLY SEND FINALIZE REQUEST, NO RETURN NEEDED
            }
            case CAST_VOTE -> {
                // TODO: SEND VOTE CASTING REQUEST, NO RETURN NEEDED
                PlaceHolder placeHolder = new PlaceHolder();
                placeHolder.text.setText("Your vote has been recorded.");
                placeHolder.exit.setOnMouseClicked(exit -> clearAndPush(new VoterLogin(this)));
                clearAndPush(placeHolder);
            }
            case VOTER_EXIT -> clearAndPush(new VoterLogin(this));
            case VOTER_LOGIN -> {
                // TODO: REQUEST LOGIN FROM VOTING SYSTEM, ASSUME SUCCESS
                if (ballot == null) {
                    ballot = votingSystem.getBallot();
                }
                clearAndPush(new VoteCastingDisplay(ballot, this));
                return new ResolutionResponse(ResponseType.SUCCESS);
            }
            case ADMIN_LOGIN -> {
                // TODO: SEND REQUEST TO VOTING SYSTEM FOR ADMIN SIGN IN
                return new ResolutionResponse(ResponseType.SUCCESS);
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
                        AllowedOperations.ALLOWED_OPERATIONS.get(votingSystem.getStatus()));
                clearAndPush(adminMenu);
            }
        }
        return null;
    }

    @Override
    public void dispatchOperation(Operation operation) {
        adminStack.clear();
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
                Status status = votingSystem.getStatus();
                if (status == Status.IN_PROCESS) {
                    // TODO: LOCKOUT SCREEN
                }
            }
            case STARTUP_COMPLETE -> {
                PlaceHolder placeHolder = new PlaceHolder();
                placeHolder.text.setText("System Startup Complete :)");
                placeHolder.exit.setOnMouseClicked(exit -> statusDispatch(votingSystem.getStatus()));
                clearAndPush(placeHolder);
            }
            case DOOR_CLOSE -> {
                doorClosed.set(true);
                statusDispatch(votingSystem.getStatus());
            }
            case DEVICE_CONNECT -> {
                deviceConnected.set(true);
                AdminMenu adminMenu = new AdminMenu(this,
                        AllowedOperations.ALLOWED_OPERATIONS.get(votingSystem.getStatus()));
                clearAndPush(adminMenu);
            }
            case DEVICE_DISCONNECT -> {
                getChildren().clear();
                deviceConnected.set(false);
                statusDispatch(votingSystem.getStatus());
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
        switch (operation) {
            case BEGIN_VOTING_WINDOW -> {
                votingSystem.setStatus(Status.IN_PROCESS);
                placeHolder.text.setText("Voting window has been started");
            }
            case CONFIGURATION -> {
                votingSystem.setStatus(Status.PRE_ELECTION);
                placeHolder.text.setText("Configuration Complete, (display ballot format)");
            }
            case VOTE_COUNT_EXPORT -> {
                log.info("PERFORMING VOTE EXPORT");
                placeHolder.text.setText("Vote tabulation results have been exported");
            }
            case SYSTEM_LOG_EXPORT -> {
                log.info("PERFORMING LOG EXPORT");
                placeHolder.text.setText("System logs have been exported.");
            }
            case BEGIN_VOTE_COUNTING -> {
                votingSystem.setStatus(Status.VOTE_COUNTING);
                placeHolder.text.setText("Vote tabulation complete, the vote window has been ended");
            }
            case END_VOTE_PROCESS -> {
                votingSystem.setStatus(Status.POST_ELECTION);
                placeHolder.text.setText("The configured ballot and have been ended");
            }
        }
        placeHolder.exit.setOnMouseClicked(exit -> resolve(new ResolutionRequest.Builder().withType(RequestType.OPERATION_EXIT).build()));
        clearAndPush(placeHolder);
    }
}
