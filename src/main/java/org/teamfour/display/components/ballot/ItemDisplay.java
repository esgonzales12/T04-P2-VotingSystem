package org.teamfour.display.components.ballot;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.teamfour.display.components.ballot.base.BaseItemDisplay;
import org.teamfour.model.db.Item;
import org.teamfour.model.db.Option;
import org.teamfour.model.enums.ItemType;

public class ItemDisplay extends BaseItemDisplay {

    private int totalSelected;
    private ScrollPane scrollPane;

    public ItemDisplay(Item item, String sectionName) {
        super(item, sectionName);
        totalSelected = 0;
        scrollPane = new ScrollPane();
        init(item);
    }

    private void init(Item ballotItem) {
        boolean proposition = ballotItem.getType().equals(ItemType.PROPOSITION.value());
        for (Option option: ballotItem.getOptions()) {
            String primaryText = proposition ? option.getChoice() : option.getName();
            String secondaryText = proposition ? "" : option.getParty();
            CandidateCard optionCard = new CandidateCard(primaryText, secondaryText, option.getId());
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
