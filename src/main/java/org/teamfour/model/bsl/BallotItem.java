package org.teamfour.model.bsl;

import java.util.List;

public class BallotItem {
    private final String type;
    private final String name;
    private final String description;
    private final Integer allowedSelections;
    private final List<Candidate> candidates;
    private final List<String> options;


    public BallotItem(Integer allowedSelections, String name, String type, String description, List<Candidate> candidates, List<String> options) {
        this.allowedSelections = allowedSelections;
        this.name = name;
        this.type = type;
        this.description = description;
        this.candidates = candidates;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Integer getAllowedSelections() {
        return allowedSelections;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public List<String> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return "BallotItem{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", allowedSelections=" + allowedSelections +
                ", candidates=" + candidates +
                ", options=" + options +
                '}';
    }
}
