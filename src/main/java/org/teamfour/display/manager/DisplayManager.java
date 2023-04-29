package org.teamfour.display.manager;

import org.teamfour.display.data.ResolutionRequest;
import org.teamfour.display.data.ResolutionResponse;
import org.teamfour.system.enums.Operation;

public interface DisplayManager {
    ResolutionResponse resolve(ResolutionRequest request);

    void handleChainExit();

    void dispatchOperation(Operation operation);

    void handleNotification();
}
