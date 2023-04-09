package org.teamfour.model.bsl;

import java.util.List;

import static java.util.Objects.isNull;

public class BallotSection {
    private final String name;
    private final List<BallotItem> items;

    public BallotSection(String name, List<BallotItem> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public List<BallotItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "BallotSection{" +
                "name='" + name + '\'' +
                ", numItems=" + (isNull(items) ? null : items.size()) +
                "items='" + items +
                '}';
    }

}
