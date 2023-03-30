package org.teamfour.model;

public class ElectionOffice {
    private final Integer id;
    private final String officeName;
    private final Integer allowedSelections;
    private final Integer electionId;

    public ElectionOffice(Integer id, String officeName, Integer allowedSelections, Integer electionId) {
        this.id = id;
        this.officeName = officeName;
        this.allowedSelections = allowedSelections;
        this.electionId = electionId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public Integer getAllowedSelections() {
        return allowedSelections;
    }

    public Integer getElectionId() {
        return electionId;
    }

    public Integer getId() {
        return id;
    }
}
