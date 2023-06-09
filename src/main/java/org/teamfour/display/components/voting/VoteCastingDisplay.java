package org.teamfour.display.components.voting;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.teamfour.display.DisplayManager;
import org.teamfour.display.components.common.LoadingScreen;
import org.teamfour.display.data.ResolutionRequest;
import org.teamfour.display.enums.RequestType;
import org.teamfour.model.db.Ballot;
import org.teamfour.model.db.Vote;

import java.util.List;

public class VoteCastingDisplay extends StackPane {
    protected final DisplayManager displayManager;
    protected final Ballot ballot;
    protected final LoadingScreen loadingScreen;
    protected BallotFinalizeDisplay finalizeDisplay;
    protected ItemPaginatedDisplay itemPages;

    public VoteCastingDisplay(Ballot ballot, DisplayManager displayManager) {
        this.ballot = ballot;
        this.displayManager = displayManager;
        this.loadingScreen = new LoadingScreen();
        loadingScreen.setText("Recording Your Vote...");
        render();
    }

    public void render() {
        itemPages = new ItemPaginatedDisplay(ballot);
        finalizeDisplay = new BallotFinalizeDisplay();
        finalizeDisplay.setVisible(false);
        loadingScreen.setVisible(false);
        itemPages.finalize.setOnMouseClicked(finalizeHandler());
        itemPages.exit.setOnMouseClicked(exitHandler());
        finalizeDisplay.castVote.setOnMouseClicked(castVoteSelected());
        finalizeDisplay.goBack.setOnMouseClicked(goBackSelected());
        getChildren().addAll(itemPages, finalizeDisplay, loadingScreen);
    }

    protected EventHandler<MouseEvent> finalizeHandler() {
        return finalizeSelected -> {
            List<Vote> votes = itemPages.getItemVotes();
            if (displayManager != null) {
                displayManager
                        .resolve(new ResolutionRequest.Builder()
                                .withVotes(votes)
                                .withType(RequestType.FINALIZE)
                                .build());
            }
            finalizeDisplay.render(ballot, votes);
            finalizeDisplay.setVisible(true);
        };
    }

    protected EventHandler<MouseEvent> castVoteSelected() {
        return castVoteSelected -> {
            loadingScreen.setVisible(true);
            displayManager
                    .resolve(new ResolutionRequest.Builder()
                            .withVotes(itemPages.getItemVotes())
                            .withType(RequestType.CAST_VOTE)
                            .build());
        };
    }

    protected EventHandler<MouseEvent> exitHandler() {
        return voterExit -> {
            setVisible(false);
            displayManager
                    .resolve(new ResolutionRequest.Builder()
                            .withType(RequestType.VOTER_EXIT)
                            .build());
        };
    }

    private EventHandler<MouseEvent> goBackSelected() {
        return goBack -> finalizeDisplay.setVisible(false);
    }

}
