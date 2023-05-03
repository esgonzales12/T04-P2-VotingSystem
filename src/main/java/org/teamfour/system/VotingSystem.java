package org.teamfour.system;

import org.teamfour.model.db.Ballot;
import org.teamfour.system.enums.Status;

public interface VotingSystem {
    Status getStatus();

    Ballot getBallot();

    void setStatus(Status status);

    SystemResponse handleRequest(SystemRequest request);


}
