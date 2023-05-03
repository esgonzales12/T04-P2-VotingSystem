package org.teamfour.system;

import com.google.gson.Gson;
import org.teamfour.logging.LogBase;
import org.teamfour.model.bsl.Ballot;
import org.teamfour.system.data.Metadata;
import org.teamfour.system.enums.Status;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class VotingSystemImpl extends LogBase implements VotingSystem {

    private Status status;
    public VotingSystemImpl(String logIdentifier) {
        super(VotingSystemImpl.class.getName());
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
            Metadata data = new Gson().fromJson(br, Metadata.class);
            // TODO: service.saveBallot(ballot);
            return data;
        } catch (Exception e) {
            log.error("something went wrong");
        }
        return null;
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
