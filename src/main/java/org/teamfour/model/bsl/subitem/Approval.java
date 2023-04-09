package org.teamfour.model.bsl.subitem;

import org.teamfour.model.bsl.BallotItem;
import org.teamfour.model.bsl.Candidate;

import java.util.List;

public class Approval extends BallotItem {

    private final List<Candidate> candidates;

    public Approval(Integer allowedSelections, String name, String type, String description, List<Candidate> candidates) {
        super(allowedSelections, name, type, description);
        this.candidates = candidates;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "candidates=" + candidates +
                ", allowedSelections=" + allowedSelections +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}