package org.teamfour.model.bsl.subitem;

import org.teamfour.model.bsl.BallotItem;

import java.util.List;

public class Proposition extends BallotItem {

    private final List<String> options;

    public Proposition(String name, String type, String description, Integer allowedSelections, List<String> options) {
        super(allowedSelections, name, type, description);
        this.options = options;
    }


    public List<String> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return "Proposition{" +
                "options=" + options +
                ", allowedSelections=" + allowedSelections +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
