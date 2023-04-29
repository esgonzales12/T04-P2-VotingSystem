package org.teamfour.display.components.voting.base;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import org.teamfour.model.db.Vote;

public abstract class OptionCard extends HBox {
    private final TextFlow textFlow;
    protected final Integer optionId;
    protected VBox graphicPane;
    protected boolean selected;

    public OptionCard(TextFlow textFlow, Integer optionId) {
        this.textFlow = textFlow;
        this.selected = false;
        this.graphicPane = new VBox();
        this.optionId = optionId;
        this.graphicPane.setMinWidth(30);
        this.graphicPane.setMaxWidth(30);
        this.graphicPane.setMinHeight(30);
        this.graphicPane.setMaxHeight(30);
        this.graphicPane.setAlignment(Pos.CENTER);
        this.graphicPane.setStyle("-fx-border-color: black");
        this.setSpacing(10);
        this.textFlow.minWidthProperty().bind(this.widthProperty().multiply(0.9));
        this.getChildren().addAll(graphicPane, textFlow);
        this.setMaxWidth(300);
        this.setMinWidth(300);
        this.setMaxHeight(50);

    }

    public void select() {
    }

    public void select(String content) {
    }

    public void deselect() {
    }

    public boolean isSelected() {
        return selected;
    }

    public Vote toVote() {
        return null;
    }
}
