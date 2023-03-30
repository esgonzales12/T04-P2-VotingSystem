package org.teamfour.model;

public class OfficeCandidate {
    private final Integer id;
    private final Integer officeId;
    private final Integer electionId;
    private final String firstname;
    private final String lastname;
    private final String party;

    public OfficeCandidate(Integer id, String firstname, String lastname,
                           String party, Integer electionId, Integer officeId) {
        this.id = id;
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

    @Override
    public String toString() {
        return "OfficeCandidate{" +
                "id=" + id +
                ", officeId=" + officeId +
                ", electionId=" + electionId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", party='" + party + '\'' +
                "}\n";
    }
}
