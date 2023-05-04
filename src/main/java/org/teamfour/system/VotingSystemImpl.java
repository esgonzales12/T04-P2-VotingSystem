package org.teamfour.system;

import com.google.gson.Gson;
import org.teamfour.display.enums.ResponseType;
import org.teamfour.logging.LogBase;
import org.teamfour.model.bsl.Ballot;
import org.teamfour.model.db.Option;
import org.teamfour.model.db.Vote;
import org.teamfour.service.VotingService;
import org.teamfour.system.data.Metadata;
import org.teamfour.system.data.SystemFiles;
import org.teamfour.system.data.SystemRequest;
import org.teamfour.system.data.SystemResponse;
import org.teamfour.system.enums.Status;
import org.teamfour.system.enums.SystemResponseType;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingSystemImpl extends LogBase implements VotingSystem {

    private Status status;
    private final Metadata systemMetadata;
    private final VotingService votingService;
    public VotingSystemImpl(String logIdentifier) {
        super(VotingSystemImpl.class.getName());
        this.systemMetadata = fetchSystemData();
        this.votingService = null;
    }
    public void startVoteWindow() {
        status = Status.IN_PROCESS;
    }

    @Override
    public SystemResponse handleRequest(SystemRequest request) {
        switch (request.getType()) {
            case OPERATION -> {
                return SystemResponse
                        .builder()
                        .type(SystemResponseType.SUCCESS)
                        .build();
            }
            case VOTE_FINALIZE -> {
                votingService.countVotes(getBallot().getId());
                // TODO: CALL VVPAT
                return SystemResponse.builder()
                        .type(SystemResponseType.SUCCESS)
                        .build();
            }
            case CAST_VOTE -> {
                boolean recorded = votingService.recordVotes(request.getVotes(), getBallot().getId());
            }




            case VOTER_LOGIN -> {
                if (votingService.voterLogin(request.getVoterAccessCode())) {
                    return SystemResponse.builder()
                            .type(SystemResponseType.SUCCESS)
                            .build();
                } else {
                    return SystemResponse.builder()
                            .type(SystemResponseType.FAILURE)
                            .build();
                }
            }

        }
        return null;
    }



    private void handleConfiguration() {

        try (BufferedReader br = new BufferedReader(new FileReader(""))) {
            org.teamfour.model.bsl.Ballot ballot = new Gson().fromJson(br, Ballot.class);
            org.teamfour.model.db.Ballot dbBallot = votingService.saveBallot(ballot);
            systemMetadata.setElectionId(dbBallot.getId());
            updateSystemData();
        }  catch (IOException ignored) {
            log.error("");
        }
        status = Status.PRE_ELECTION;
    }

    @Override
    public Metadata getSystemMetadata() {
        return systemMetadata;
    }
    public void endVoteProcess() {
        systemMetadata.setStatus(Status.POST_ELECTION);
        systemMetadata.setAutoTestComplete(false);
        systemMetadata.setManualTestComplete(false);
        systemMetadata.setTabulationComplete(false);
        systemMetadata.setElectionId(null);
        updateSystemData();
//        List<Vote> votes = service.getVotes();
//        Map<String, Integer> candidateVotes = new HashMap<>();
//        int totalVotes = 0;
//        for (Vote vote : votes) {
//            String candidate = vote.getCandidateId();
//            candidateVotes.put(candidate, candidateVotes.getOrDefault(candidate, 0) + 1);
//            totalVotes++;
//        }
//        // Save vote results to storage
//        service.saveVoteResults(candidateVotes, totalVotes);
//        // Mark all users as expired
//        service.markAllUsersExpired();
    }


    public void countVotes() {

        status = Status.VOTE_COUNTING;
        votingService.countVotes(systemMetadata.getElectionId());
        Map<Option, Integer> votes = votingService.getTabulation(systemMetadata.getElectionId());
        // temporary to save votes
        Map<String, Integer> voteCounts = new HashMap<>();
//        for (Vote vote : votes) {
//            String candidateId = vote.getCandidateId();
//            voteCounts.put(candidateId, voteCounts.getOrDefault(candidateId, 0) + 1);
//        }

        // TODO: Save vote counts to storage
    }


    @Override
    public org.teamfour.model.db.Ballot getBallot() {
        if (systemMetadata.getElectionId() != null) {
            // TODO: votingService.getBallot(systemMetadata.getElectionId();

        }
        return null;
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