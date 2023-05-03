package org.teamfour.display.components.common;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.ArrayDeque;
import java.util.Deque;

public class StepDisplay extends StackPane {
    private final Deque<Node> pages;
    public StepDisplay() {
        pages = new ArrayDeque<>();
    }
    public void addPage(Node page) {
        pages.addFirst(page);
    }

    public void render() {
        pages.forEach(page -> getChildren().add(page));
    }

    public void nextPage() {
        if (pages.isEmpty()) return;
        Node curr = pages.pollLast();
        getChildren().remove(curr);
        curr.setVisible(false);
    }

}
