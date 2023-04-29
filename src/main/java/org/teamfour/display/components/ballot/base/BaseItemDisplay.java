package org.teamfour.display.components.ballot.base;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.teamfour.display.components.ballot.CandidateCard;
import org.teamfour.model.db.Item;
import org.teamfour.model.db.Vote;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseItemDisplay extends BorderPane {
    protected final Integer allowedSelections;
    protected final Integer itemId;
    protected final VBox headerLabel;
    protected List<CandidateCard> candidateCards;

    public BaseItemDisplay(Item item, String sectionName) {
        this.headerLabel = new VBox();
        this.candidateCards = new ArrayList<>();
        this.itemId = item.getId();
        this.allowedSelections = item.getAllowedSelections();
        setHeaderLabel(item, sectionName);
        setTop(headerLabel);
    }

    public List<Vote> getVotes() {
        List<Vote> votes = new ArrayList<>();
        for (CandidateCard option: candidateCards) {
            Vote vote = option.toVote();
            vote.setItemId(this.itemId);
        }
        return votes;
    }

    private void setHeaderLabel(Item item, String sectionName) {
        Text itemHeader = new Text("Item: " + item.getName() + "\n");
        Text sectionHeader = new Text("Section: " + sectionName + "\n");
        Text descriptionHeader = new Text(item.getDescription() + "\n");
        Text selectionHeader = new Text(String.format("You can make up to %d selection(s)", allowedSelections));

        itemHeader.setFill(Color.WHITE);
        sectionHeader.setFill(Color.WHITE);
        selectionHeader.setFill(Color.WHITE);
        descriptionHeader.setFill(Color.WHITE);

        itemHeader.getStyleClass().setAll("h3", "strong");
        sectionHeader.getStyleClass().setAll("h4", "strong");
        selectionHeader.getStyleClass().setAll("p", "em", "strong");
        descriptionHeader.getStyleClass().setAll("h5", "strong");

        TextFlow textFlow = new TextFlow(itemHeader, sectionHeader, descriptionHeader, selectionHeader);

        headerLabel.getStyleClass().setAll("item-header");
        headerLabel.getChildren().add(textFlow);
    }

}
