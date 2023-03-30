package org.teamfour.model;

public class ItemCandidate {
    private final Integer id;
    private final Integer electionItemId;
    private final String itemName;

    public ItemCandidate(Integer id, Integer electionItemId, String itemName) {
        this.id = id;
        this.electionItemId = electionItemId;
        this.itemName = itemName;
    }

    public Integer getId() {
        return id;
    }

    public Integer getElectionItemId() {
        return electionItemId;
    }

    public String getItemName() {
        return itemName;
    }
}
