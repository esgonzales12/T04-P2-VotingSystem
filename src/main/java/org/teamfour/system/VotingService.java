package org.teamfour.system;

import org.teamfour.model.db.Ballot;
import org.teamfour.system.enums.Status;

public class VotingService implements VotingSystem{
    private Status status;
    private Ballot ballot;
    @Override
    public Status getStatus() {
        return status;
    }
    public boolean setElection(Ballot ballot, Integer[] Times){

        return false;
    }
    public boolean voterSignIn(Integer IDNumber){
        return false;
    }
    public boolean voteCasting(){
        return false;
    }
    @Override
    public Ballot getBallot() {
        return ballot;
    }

    @Override
    public void setStatus(Status statusIn) {
        status = statusIn;
    }

    @Override
    public SystemResponse handleRequest(SystemRequest request) {
        return null;
    }
}
