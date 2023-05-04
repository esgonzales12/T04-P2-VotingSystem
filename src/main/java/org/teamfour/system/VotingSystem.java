package org.teamfour.system;

import org.teamfour.model.db.Ballot;
import org.teamfour.system.data.Metadata;
import org.teamfour.system.data.SystemRequest;
import org.teamfour.system.data.SystemResponse;

public interface VotingSystem {

    Ballot getBallot();

    Metadata getSystemMetadata();

    SystemResponse handleRequest(SystemRequest request);

    boolean validDeviceConnected();

}
