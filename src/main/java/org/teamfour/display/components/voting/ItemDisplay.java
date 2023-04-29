package org.teamfour.display.components.voting;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import org.teamfour.display.components.voting.base.BaseItemDisplay;
import org.teamfour.display.components.voting.common.BallotUtils;
import org.teamfour.model.db.Item;
import org.teamfour.model.db.Option;
import org.teamfour.model.db.Vote;
import org.teamfour.model.enums.ItemType;

import java.util.ArrayList;
import java.util.List;

public class ItemDisplay extends BaseItemDisplay {

    private int totalSelected;
    private ScrollPane scrollPane;

    public ItemDisplay(Item item, String sectionName) {
        super(item, sectionName);
        totalSelected = 0;
        scrollPane = new ScrollPane();
        init(item);
    }

    public List<Vote> getVotes() {
        List<Vote> votes = new ArrayList<>();
        for (CandidateCard option : candidateCards) {
            Vote vote = option.toVote();
            vote.setItemId(this.itemId);
            votes.add(vote);
        }
        return votes;
    }

    private void init(Item ballotItem) {
        boolean proposition = ballotItem.getType().equals(ItemType.PROPOSITION.value());
        for (Option option : ballotItem.getOptions()) {
            CandidateCard optionCard = BallotUtils.getCandidateCard(option, proposition);
            optionCard.setOnMouseClicked(selectHandler(optionCard));
            candidateCards.add(optionCard);
        }

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(5);
        flowPane.setVgap(5);
        flowPane.setBackground(Background.fill(Color.valueOf("#D3D3D3FF")));
        flowPane.setPrefWrapLength(600);
        flowPane.setAlignment(Pos.TOP_CENTER);
        flowPane.getChildren().addAll(candidateCards);
        flowPane.setStyle("-fx-padding: 10px;");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(flowPane);
        setCenter(scrollPane);
        BorderPane.setAlignment(scrollPane, Pos.CENTER);
    }

    private EventHandler<MouseEvent> selectHandler(CandidateCard newSelection) {

        return event -> {
            if (newSelection.isSelected()) {
                newSelection.deselect();
                totalSelected -= 1;
            } else if (totalSelected < allowedSelections) {
                newSelection.select();
                totalSelected += 1;
            }
        };
    }


}
