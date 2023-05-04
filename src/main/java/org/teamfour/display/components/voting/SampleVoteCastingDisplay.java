package org.teamfour.display.components.voting;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.teamfour.display.DisplayManager;
import org.teamfour.display.data.ResolutionRequest;
import org.teamfour.display.enums.RequestType;
import org.teamfour.model.db.Ballot;
import org.teamfour.model.db.Vote;

import java.util.List;

public class SampleVoteCastingDisplay extends VoteCastingDisplay {
    public SampleVoteCastingDisplay(Ballot ballot, DisplayManager displayManager) {
        super(ballot, displayManager);
    }

    @Override
    protected EventHandler<MouseEvent> finalizeHandler() {
        return finalizeSelected -> {
            List<Vote> votes = itemPages.getItemVotes();
            finalizeDisplay.render(ballot, votes);
            finalizeDisplay.setVisible(true);
        };
    }

    @Override
    protected EventHandler<MouseEvent> castVoteSelected() {
        return castVoteSelected -> {
            loadingScreen.setVisible(true);
            displayManager.resolve(new ResolutionRequest.Builder()
                            .withType(RequestType.OPERATION_EXIT)
                            .build());
        };
    }

}
