package org.teamfour.system;

import org.teamfour.model.db.Ballot;
import org.teamfour.model.db.Vote;
import org.teamfour.system.data.Metadata;
import org.teamfour.system.enums.Status;

import java.util.List;

public interface VotingSystem {
    Status getStatus();

    Ballot getBallot();

    void setStatus(Status status);

    SystemResponse handleRequest(SystemRequest request);



}
