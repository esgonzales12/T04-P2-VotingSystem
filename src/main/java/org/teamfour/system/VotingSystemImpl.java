package org.teamfour.system;

import com.google.gson.Gson;
import org.teamfour.display.enums.ResponseType;
import org.teamfour.logging.LogBase;
import org.teamfour.model.bsl.Ballot;
import org.teamfour.model.db.Vote;
import org.teamfour.system.data.Metadata;
import org.teamfour.system.enums.Status;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingSystemImpl extends LogBase implements VotingSystem {

    private Status status;
    private final Metadata systemMetadata;
    public VotingSystemImpl(String logIdentifier) {
        super(VotingSystemImpl.class.getName());
        this.systemMetadata = fetchSystemData();
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
                return SystemResponse
                        .builder()
                        .type(ResponseType.SUCCESS)
                        .build();
            }
            case VOTE_FINALIZE -> {
            }
            case CAST_VOTE -> {

            }
            case VOTER_LOGIN -> {
            }
            case ADMIN_LOGIN -> {
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

    @Override
    public Metadata getSystemMetadata() {
        return systemMetadata;
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

    private Metadata fetchSystemData() {
        try (BufferedReader br = new BufferedReader(new FileReader(SystemFiles.META))) {
            // TODO: service.saveBallot(ballot);
            return new Gson().fromJson(br, Metadata.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("UNABLE TO READ SYSTEM METADATA");
        }
        return null;
    }

    private void updateSystemData() {
        try {
            FileOutputStream outputStream = new FileOutputStream(SystemFiles.META, false);
            outputStream.write(new Gson().toJson(systemMetadata).getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }



}