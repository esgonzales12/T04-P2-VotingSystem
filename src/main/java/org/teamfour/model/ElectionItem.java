package org.teamfour.model;

public class ElectionItem {
    private final Integer id;
    private final String itemName;
    private final Integer electionId;
    private final Integer allowedSelections;

    public ElectionItem(Integer id, String itemName, Integer electionId, Integer allowedSelections) {
        this.id = id;
        this.itemName = itemName;
        this.electionId = electionId;
        this.allowedSelections = allowedSelections;
    }

    public Integer getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getElectionId() {
        return electionId;
    }

    public Integer getAllowedSelections() {
        return allowedSelections;
    }
}
