package org.teamfour.display.resolver;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.teamfour.display.components.ControlPane;
import org.teamfour.display.enums.ResolutionPolicy;
import org.teamfour.display.manager.DisplayManager;
import org.teamfour.display.resolver.data.ResolutionRequest;
import org.teamfour.display.resolver.data.ResolutionResponse;
import org.teamfour.display.resolver.data.Step;
import org.teamfour.display.components.LoadingScreen;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResolutionChain extends BorderPane implements Resolver {
    private final Logger log;
    private final DisplayManager displayManager;
    private final LinkedList<Step> steps;
    private final ListIterator<Step> stepIterator;
    private final StackPane display;
    private final ControlPane controlPane;
    private VBox notificationPane;
    private Label notification;

    public ResolutionChain(Builder builder) {
        this.log = LogManager.getLogger(ResolutionChain.class.getName());
        this.displayManager = builder.displayManager;
        this.steps = builder.steps;
        this.stepIterator = steps.listIterator();
        this.controlPane = new ControlPane();
        this.display = new StackPane();
        this.notification = new Label();
        this.notificationPane = new VBox(notification);
        init();
    }

    @Override
    public ResolutionResponse handle(ResolutionRequest request) {
        Step current = steps.get(stepIterator.nextIndex());
        ((Node) current.getForm()).setDisable(true);
        ResolutionResponse response = displayManager.resolve(request);
        switch (response.getResponseType()) {
            case SUCCESS -> {
                notificationPane.getStyleClass().setAll("alert", "alert-success");
                notification.setText("SUCCESS");
                notificationPane.setVisible(true);
                current.getForm().valid();
                handleApproval(current);
            }
            case FAILURE -> {
                notificationPane.getStyleClass().setAll("alert", "alert-danger");
                notification.setText("INVALID");
                notificationPane.setVisible(true);
                current.getForm().invalid();
                ((Node) current.getForm()).setDisable(false);
            }
        }
        return null;
    }

    private void handleApproval(Step step) {
        Node current = (Node) step.getForm();

        final Callback<Void, Void> callBack = new Callback<>() {
            final AtomicBoolean complete = new AtomicBoolean(false);

            @Override
            public Void call(Void param) {
                if (complete.get()) return null;
                notificationPane.setVisible(false);
                controlPane.continueButton.setDisable(true);
                stepIterator.next();
                switch (step.getStepPolicy()) {
                    case DESTROY -> {
                        step.getForm().clear();
                        step.invalidate();
                        controlPane.previousButton.setDisable(true);
                    }
                    case CLEAR -> {
                        step.getForm().clear();
                        controlPane.previousButton.setDisable(false);
                    }
                    case CACHE -> controlPane.previousButton.setDisable(false);
                }
                Node next = stepIterator.hasNext() ?
                        (Node) steps.get(stepIterator.nextIndex()).getForm() : new LoadingScreen();
                EventHandler<ActionEvent> onTransitionComplete = event -> {
                    display.getChildren().clear();
                    display.getChildren().add(next);
                    revertTransition(next);
                    next.setDisable(false);
                };
                if (!stepIterator.hasNext()) {
                    displayManager.handleChainExit();
                }
                doContinue(current, onTransitionComplete);
                complete.set(true);
                return null;
            }
        };

        switch (step.getTransitionPolicy()) {
            case AUTO -> callBack.call(null);
            case CONFIRM -> {
                controlPane.continueButton.setDisable(false);
                controlPane.continueButton.setOnMouseClicked(
                        event -> callBack.call(null)
                );
            }
        }
    }

    private void init() {
        steps.forEach(step -> step.getForm().setResolver(this));
        display
            .getChildren()
            .add((Node) steps.get(stepIterator.nextIndex()).getForm());
        controlPane.spacingProperty().bind(this.heightProperty().divide(4));
        controlPane.continueButton.setDisable(true);
        controlPane.previousButton.setDisable(true);
        controlPane.exitButton.setOnMouseClicked(event -> log.info("exit pls"));
        controlPane.previousButton.setOnMouseClicked(event -> tryPrev());
        notification.getStyleClass().add("b");
        notificationPane.getStyleClass().setAll("alert");
        notificationPane.setVisible(false);
        setTop(notificationPane);
        setRight(controlPane);
        setCenter(display);
    }

    private void doContinue(Node subject, EventHandler<ActionEvent> callback) {
        final double width = getWidth();
        final double start = subject.getLayoutX();
        TranslateTransition away
                = new TranslateTransition(Duration.millis(500), subject);
        away.setByX(-2 * width);
        away.setOnFinished(callback);
        away.play();
    }

    private void doPrev(Node subject, EventHandler<ActionEvent> onFinish) {
        TranslateTransition transition
                = new TranslateTransition(Duration.millis(500), subject);
        transition.setByX(2 * getWidth());
        transition.setOnFinished(onFinish);
        transition.play();
    }

    private void tryPrev() {
        if (!stepIterator.hasPrevious()) {
            log.warn("UNEXPECTED RETURN REQUEST RECEIVED");
            return;
        }
        notificationPane.setVisible(false);
        Step previous = steps.get(stepIterator.previousIndex());
        Step current = steps.get(stepIterator.nextIndex());
        if (previous.invalidated()
                || previous.getStepPolicy() == ResolutionPolicy.DESTROY
                || !stepIterator.hasPrevious()) {
            log.warn("UNEXPECTED RETURN REQUEST RECEIVED");
            return;
        }
        controlPane.previousButton.setDisable(true);
        current.getForm().clear();
        stepIterator.previous();
        final Callback<Void, Void> callback = param -> {
           if (!stepIterator.hasPrevious()) {
               controlPane.previousButton.setDisable(true);
           } else {
               Step newPrev = steps.get(stepIterator.previousIndex());
               boolean disabled = newPrev.invalidated()
                       || previous.getStepPolicy() == ResolutionPolicy.DESTROY;
               controlPane.previousButton.setDisable(disabled);
           }
           return null;
        };
        EventHandler<ActionEvent> onFinish = event -> {
            display.getChildren().clear();
            display.getChildren().add((Node) previous.getForm());
            ((Node) previous.getForm()).setDisable(false);
            revertTransition((Node) previous.getForm());
            log.info("FORM ADDED");
            callback.call(null);
        };
        doPrev((Node) current.getForm(), onFinish);
    }

    private void revertTransition(Node subject) {
        if (subject.translateXProperty().get() == 0.0) {
            return;
        }
        TranslateTransition transition
                = new TranslateTransition(Duration.millis(100), subject);
        transition.setByX( -1 * subject.translateXProperty().get());
        transition.play();
    }


    public static class Builder {
        private final DisplayManager displayManager;
        private final LinkedList<Step> steps;

        public Builder(DisplayManager displayManager) {
            this.displayManager = displayManager;
            steps = new LinkedList<>();
        }

        public Builder withStep(Step step) {
            steps.add(step);
            return this;
        }

        public ResolutionChain build() {
            return new ResolutionChain(this);
        }
    }
}
