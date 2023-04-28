package org.teamfour.display.components.ballot;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public abstract class Option extends HBox {
    private boolean selected;
    private final TextFlow textFlow;
    private VBox graphicPane;

    public Option(String text) {
        this.textFlow = new TextFlow(new Text(text));
        this.selected = false;
        this.graphicPane = new VBox();
        graphicPane.minWidthProperty().bind(this.widthProperty().multiply(0.1));
        textFlow.minWidthProperty().bind(this.widthProperty().multiply(0.9));
    }
    public void toggleSelected(){}
    public void toggleSelected(String content){}
    public boolean isSelected() {
        return selected;
    }
}
