package org.teamfour.display.components.ballot;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.teamfour.display.components.ballot.base.OptionCard;
import org.teamfour.display.util.Icons;
import org.teamfour.model.db.Vote;

public class CandidateCard extends OptionCard {

    public CandidateCard(String name, String party, Integer optionId) {
        super(StringUtils.isEmpty(party) ? new TextFlow(headerText(name))
                : new TextFlow(headerText(name), bodyText(party)), optionId);
        getStyleClass().setAll("candidate-card");
    }

    @Override
    public void select(){
        this.selected = true;
        this.graphicPane.getChildren().clear();
        this.graphicPane
                .getChildren()
                .add(new Icons.Builder(FontAwesome.CHECK).withSize(30).build());
    }

    @Override
    public void select(String content){}

    @Override
    public void deselect(){
        this.selected = false;
        this.graphicPane.getChildren().clear();
    }
    @Override
    public Vote toVote() {
        Vote vote = new Vote();
        vote.setOptionId(optionId);
        vote.setValue(selected ? VoteValue.SELECTED : VoteValue.NONE);
        return vote;
    }

    private static Text headerText(String header) {
        Text text = new Text(header + "\n");
        text.getStyleClass().setAll("h4");
        return text;
    }

    private static Text bodyText(String body) {
        Text text = new Text(body);
        text.getStyleClass().setAll("p", "strong");
        return text;
    }
}
