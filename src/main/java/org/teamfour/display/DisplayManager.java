package org.teamfour.display;

import org.teamfour.display.data.ResolutionRequest;
import org.teamfour.display.data.ResolutionResponse;
import org.teamfour.display.enums.Notification;
import org.teamfour.system.enums.Operation;

public interface DisplayManager {
    ResolutionResponse resolve(ResolutionRequest request);

    void dispatchOperation(Operation operation);

    void handleNotification(Notification notification);

}
