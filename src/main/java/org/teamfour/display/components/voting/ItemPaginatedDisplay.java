package org.teamfour.display.components.voting;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.teamfour.model.db.Ballot;
import org.teamfour.model.db.Item;
import org.teamfour.model.db.Section;
import org.teamfour.model.db.Vote;

import java.util.ArrayList;
import java.util.List;

public class ItemPaginatedDisplay extends BorderPane {
    private final List<ItemDisplay> itemDisplays;
    private final Pagination paginatedDisplay;
    public final Button finalize;
    public final Button exit;

    public ItemPaginatedDisplay(Ballot ballot) {
        paginatedDisplay = new Pagination();
        itemDisplays = new ArrayList<>();
        finalize = new Button("Finalize My Vote");
        exit = new Button("Exit Vote Casting");
        init(ballot);
    }

    private void init(Ballot ballot) {
        finalize.getStyleClass().setAll("btn", "btn-success", "btn-lg");
        exit.getStyleClass().setAll("btn", "btn-danger", "btn-lg");
        for (Section section : ballot.getSections()) {
            for (Item item : section.getItems()) {
                ItemDisplay display = new ItemDisplay(item, section.getName());
                itemDisplays.add(display);
            }
        }
        paginatedDisplay.setPageCount(itemDisplays.size());
        paginatedDisplay.setCurrentPageIndex(0);
        paginatedDisplay.setPageFactory(itemDisplays::get);

        HBox controls = new HBox(exit, finalize);
        controls.setAlignment(Pos.CENTER);
        controls.setBackground(Background.fill(Color.valueOf("#343a40")));
        controls.spacingProperty().bind(this.widthProperty().subtract(finalize.widthProperty()).subtract(exit.widthProperty()).multiply(0.8));
        setBottom(controls);
        setCenter(paginatedDisplay);
    }

    public List<Vote> getItemVotes() {
        List<Vote> votes = new ArrayList<>();
        for (ItemDisplay itemDisplay : itemDisplays) {
            votes.addAll(itemDisplay.getVotes());
        }
        return votes;
    }
}
