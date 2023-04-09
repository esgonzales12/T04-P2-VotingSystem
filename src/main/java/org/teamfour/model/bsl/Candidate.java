package org.teamfour.model.bsl;

public class Candidate {
    private final String name;
    private final String party;

    public Candidate(String firstname, String party) {
        this.name = firstname;
        this.party = party;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    @Override
    public String toString() {
        return "OfficeCandidate{" +
                ", name='" + name + '\'' +
                ", party='" + party + '\'' +
                "}";
    }
}
