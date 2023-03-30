package org.teamfour.model;

public class OfficeCandidate {
    private final String firstname;
    private final String lastname;
    private final String party;
    private final Integer electionId;
    private final Integer officeId;

    public OfficeCandidate(String firstname, String lastname, String party, Integer electionId, Integer officeId) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.party = party;
        this.electionId = electionId;
        this.officeId = officeId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getParty() {
        return party;
    }

    public Integer getElectionId() {
        return electionId;
    }

    public Integer getOfficeId() {
        return officeId;
    }
}
