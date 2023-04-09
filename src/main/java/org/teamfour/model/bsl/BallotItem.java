package org.teamfour.model.bsl;

public class BallotItem {
    protected transient final String type;
    protected final String name;
    protected final String description;
    protected final Integer allowedSelections;

    public BallotItem(Integer allowedSelections, String name, String type, String description) {
        this.allowedSelections = allowedSelections;
        this.name = name;
        this.type = type;
        this.description = description;
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
}
