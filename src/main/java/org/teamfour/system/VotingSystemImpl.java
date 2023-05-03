package org.teamfour.system;

import com.google.gson.Gson;
import org.teamfour.logging.LogBase;
import org.teamfour.model.bsl.Ballot;
import org.teamfour.model.db.Vote;
import org.teamfour.system.data.Metadata;
import org.teamfour.system.enums.Status;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingSystemImpl extends LogBase implements VotingSystem {

    private Status status;
    public VotingSystemImpl(String logIdentifier) {

        super(VotingSystemImpl.class.getName());
    }
    public void startVoteWindow() {
        status = Status.IN_PROCESS;
        VotingService service = new VotingService();
        service.setVotesCounted(false);
    }

    @Override
    public SystemResponse handleRequest(SystemRequest request) {
        switch (request.getType()) {
            case OPERATION -> {
            }
            case VOTE_FINALIZE -> {
            }
            case CAST_VOTE -> {

            }
        }
        return null;
    }



    private void handleConfiguration() {

        try (BufferedReader br = new BufferedReader(new FileReader(getClass().getResource("ballot_file.json").getPath()))) {
            Ballot ballot = new Gson().fromJson(br, Ballot.class);
            // TODO: service.saveBallot(ballot);
            VotingService service = new VotingService();
            service.getBallot();
            Metadata current = getSystemMetadata();
            // TODO: NULL CHECK DATA
            current.setElectionId(String.valueOf(new org.teamfour.model.db.Ballot().getId()));
            String metadata = new Gson().toJson(current);
            System.out.println(ballot);
        }  catch (IOException ignored) {}
        status = Status.PRE_ELECTION;
    }

    private Metadata getSystemMetadata() {
        try (BufferedReader br = new BufferedReader(new FileReader(SystemFiles.META))) {
            VotingService service = new VotingService();
            Metadata data = new Gson().fromJson(br, Metadata.class);
            // TODO: service.saveBallot(ballot);
            return data;
        } catch (Exception e) {
            log.error("Error getting system metadata", e);
        }
        return null;
    }
    public void endVoteProcess() {
        status = Status.POST_ELECTION;
        VotingService service = new VotingService();
        List<Vote> votes = service.getVotes();
        Map<String, Integer> candidateVotes = new HashMap<>();
        int totalVotes = 0;
        for (Vote vote : votes) {
            String candidate = vote.getCandidateId();
            candidateVotes.put(candidate, candidateVotes.getOrDefault(candidate, 0) + 1);
            totalVotes++;
        }
        // Save vote results to storage
        service.saveVoteResults(candidateVotes, totalVotes);
        // Mark all users as expired
        service.markAllUsersExpired();
    }


    public void countVotes() {

        status = Status.VOTE_COUNTING;
        VotingService service = new VotingService();
        List<Vote> votes = service.getVotes();
        // temporary to save votes
        Map<String, Integer> voteCounts = new HashMap<>();
        for (Vote vote : votes) {
            String candidateId = vote.getCandidateId();
            voteCounts.put(candidateId, voteCounts.getOrDefault(candidateId, 0) + 1);
        }

        // TODO: Save vote counts to storage
    }


    @Override
    public Status getStatus() {
        return null;
    }

    @Override
    public org.teamfour.model.db.Ballot getBallot() {
        return null;
    }

    @Override
    public void setStatus(Status status) {

    }



}