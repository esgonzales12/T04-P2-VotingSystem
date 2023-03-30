package org.teamfour.model;

import java.util.List;

public class ElectionOffice {
    private final Integer id;
    private final Integer electionId;
    private final String officeName;
    private final Integer allowedSelections;
    private final List<OfficeCandidate> candidates;

    public ElectionOffice(Integer id, String officeName, Integer allowedSelections, Integer electionId, List<OfficeCandidate> candidates) {
        this.id = id;
        this.officeName = officeName;
        this.allowedSelections = allowedSelections;
        this.electionId = electionId;
        this.candidates = candidates;
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

    public List<OfficeCandidate> getCandidates() {
        return candidates;
    }

    @Override
    public String toString() {
        return "ElectionOffice{" +
                "id=" + id +
                ", officeName='" + officeName + '\'' +
                ", allowedSelections=" + allowedSelections +
                ", electionId=" + electionId +
                ",\ncandidates=" + candidates +
                '}';
    }
}
