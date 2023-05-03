package org.teamfour.system;

import org.teamfour.model.db.Ballot;
import org.teamfour.model.db.Vote;
import org.teamfour.system.data.Metadata;
import org.teamfour.system.enums.Status;

import java.util.List;
import java.util.Map;

public class VotingService implements VotingSystem{
    private Status status;
    private Ballot ballot;
    private Vote votes;
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


    public List<Vote> getVotes() {
        return (List<Vote>) votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = (Vote) votes;
    }

    public void saveVoteResults(Map<String, Integer> candidateVotes, int totalVotes) {
    }
    public void markAllUsersExpired() {
    }

    public void setVotesCounted(boolean b) {
    }
}
