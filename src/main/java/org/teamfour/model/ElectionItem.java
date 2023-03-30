package org.teamfour.model;

import java.util.List;

public class ElectionItem {
    private final Integer id;
    private final String itemName;
    private final Integer electionId;
    private final Integer allowedSelections;
    private final List<ItemCandidate> candidates;

    public ElectionItem(Integer id, String itemName, Integer electionId, Integer allowedSelections, List<ItemCandidate> candidates) {
        this.id = id;
        this.itemName = itemName;
        this.electionId = electionId;
        this.allowedSelections = allowedSelections;
        this.candidates = candidates;
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

    public List<ItemCandidate> getCandidates() {
        return candidates;
    }

    @Override
    public String toString() {
        return "ElectionItem{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", electionId=" + electionId +
                ", allowedSelections=" + allowedSelections +
                ",\ncandidates=" + candidates +
                '}';
    }
}
