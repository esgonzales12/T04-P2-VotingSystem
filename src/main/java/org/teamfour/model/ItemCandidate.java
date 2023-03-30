package org.teamfour.model;

public class ItemCandidate {
    private final Integer id;
    private final Integer itemId;
    private final Integer electionId;
    private final String itemName;

    public ItemCandidate(Integer id, Integer electionItemId, Integer electionId, String itemName) {
        this.id = id;
        this.itemId = electionItemId;
        this.electionId = electionId;
        this.itemName = itemName;
    }

    public Integer getId() {
        return id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getElectionId() {
        return electionId;
    }

    @Override
    public String toString() {
        return "ItemCandidate{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", electionId=" + electionId +
                ", itemName='" + itemName + '\'' +
                "}\n";
    }
}
