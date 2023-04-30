package org.teamfour.display.components.voting;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.teamfour.display.components.voting.common.BallotUtils;
import org.teamfour.display.components.voting.common.VoteValue;
import org.teamfour.display.util.Icons;
import org.teamfour.model.db.*;
import org.teamfour.model.enums.ItemType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BallotFinalizeDisplay extends BorderPane {
    private final ScrollPane content;
    public Button goBack;
    public Button castVote;

    public BallotFinalizeDisplay(Ballot ballot, List<Vote> votes) {
        content = new ScrollPane();
        content.setFitToHeight(true);
        content.setFitToWidth(true);
        getButtons();
        addControls();
        render(ballot, votes);
    }

    public BallotFinalizeDisplay() {
        content = new ScrollPane();
        content.setFitToHeight(true);
        content.setFitToWidth(true);
        getButtons();
        addControls();
    }

    private void addControls() {
        Text header = new Text("Vote Finalizing\n");
        Text body = new Text("Please review and confirm your selections.");
        header.getStyleClass().setAll("h3", "strong");
        body.getStyleClass().setAll("p", "strong");
        header.setFill(Color.WHITE);
        body.setFill(Color.WHITE);

        TextFlow headerText = new TextFlow(header, body);
        VBox container = new VBox(headerText);
        container.setStyle("-fx-padding: 10;");
        container.setBackground(Background.fill(Color.valueOf("#343a40")));


        HBox controls = new HBox(goBack, castVote);
        controls.setStyle("-fx-padding: 10;");
        controls.setAlignment(Pos.CENTER);
        controls.setBackground(Background.fill(Color.valueOf("#343a40")));
        controls.spacingProperty().bind(this.widthProperty()
                .subtract(goBack.widthProperty())
                .subtract(castVote.widthProperty())
                .multiply(0.8));
        setTop(container);
        setBottom(controls);
    }

    private void getButtons() {
        goBack = new Button("Go Back");
        castVote = new Button("Cast My Vote");
        goBack.getStyleClass().setAll("btn", "btn-warning", "btn-lg");
        castVote.getStyleClass().setAll("btn", "btn-success", "btn-lg");
    }

    public void render(Ballot ballot, List<Vote> votes) {
        VBox scrollPaneContent = new VBox();
        Map<Integer, List<Vote>> itemIdMap = new HashMap<>();
        List<SectionCard> sectionCards = new ArrayList<>();

        ballot.getSections().forEach(section -> section.getItems().forEach(item -> itemIdMap.put(item.getId(), new ArrayList<>())));
        votes.forEach(vote -> {
            List<Vote> itemVotes = itemIdMap.getOrDefault(vote.getItemId(), new ArrayList<>());
            itemVotes.add(vote);
            itemIdMap.put(vote.getItemId(), itemVotes);
        });

        for (Section section : ballot.getSections()) {
            SectionCard sectionCard = new SectionCard(section.getName());
            for (Item item : section.getItems()) {

                Map<Integer, Option> optionMap = new HashMap<>();
                item.getOptions().forEach(option -> optionMap.put(option.getId(), option));
                boolean proposition = item.getType().equals(ItemType.PROPOSITION.value());
                int selections = 0;

                ItemCard itemCard = new ItemCard();

                for (Vote vote : itemIdMap.get(item.getId())) {
                    if (vote.getValue().equals(VoteValue.NONE)) continue;
                    CandidateCard candidateCard = BallotUtils.getCandidateCard(optionMap.get(vote.getOptionId()), proposition);
                    itemCard.addCandidate(candidateCard);
                    selections++;
                }

                String alertColor;
                FontIcon voteIcon;

                if (selections == 0) {
                    alertColor = "#98384f";
                    voteIcon = new Icons.Builder(FontAwesome.WARNING).withSize(25).withFill(Color.valueOf(alertColor)).build();
                } else if (selections < item.getAllowedSelections()) {
                    alertColor = "#ffbb08";
                    voteIcon = new Icons.Builder(FontAwesome.EXCLAMATION).withSize(25).withFill(Color.valueOf(alertColor)).build();
                } else {
                    alertColor = "#4f7849";
                    voteIcon = new Icons.Builder(FontAwesome.CHECK_CIRCLE).withSize(25).withFill(Color.valueOf(alertColor)).build();
                }

                Text itemHeader = new Text(item.getName() + "\n");
                itemHeader.getStyleClass().setAll("h4", "strong");
                itemHeader.setFill(Color.WHITE);
                Text selectionPrompt = new Text(String.format("You used %d/%d selections.", selections, item.getAllowedSelections()));
                selectionPrompt.getStyleClass().setAll("p", "strong");
                TextFlow cardText = new TextFlow(itemHeader, selectionPrompt);

                VBox header = new VBox();
                header.getChildren().addAll(cardText);
                itemCard.setHeading(header);
                itemCard.setFooter(voteIcon);
                sectionCard.addItemCard(itemCard);
            }
            sectionCards.add(sectionCard);
        }

        scrollPaneContent.getChildren().addAll(sectionCards);
        scrollPaneContent.setSpacing(10);
        content.setContent(scrollPaneContent);
        setCenter(content);
    }


    public static class SectionCard extends Panel {
        private final VBox content = new VBox();

        public SectionCard(String titleText) {
            Text text = new Text(titleText);
            text.getStyleClass().setAll("h4", "strong");
            text.setFill(Color.WHITE);
            TextFlow textFlow = new TextFlow(text);
            setHeading(textFlow);
            getStyleClass().setAll("sec-card", "sec-card-default");
            content.setSpacing(10);
            content.setAlignment(Pos.CENTER);
            setBody(content);
        }

        public void addItemCard(ItemCard card) {
            content.getChildren().add(card);
        }
    }

    public static class ItemCard extends Panel {
        private final FlowPane content;

        public ItemCard(String titleText) {
            Text text = new Text(titleText);
            text.getStyleClass().setAll("h5", "strong");
            text.setFill(Color.WHITE);
            TextFlow textFlow = new TextFlow(text);
            setHeading(textFlow);
            getStyleClass().setAll("sec-card", "sec-card-inner");
            content = new FlowPane();
            setBody(content);
            maxHeightProperty().bind(content.heightProperty());
        }

        public ItemCard() {
            getStyleClass().setAll("sec-card", "sec-card-inner");
            content = new FlowPane();
            setBody(content);
            maxHeightProperty().bind(content.heightProperty());
        }

        public void addCandidate(CandidateCard candidateCard) {
            candidateCard.select();
            content.getChildren().add(candidateCard);
        }

    }
}
