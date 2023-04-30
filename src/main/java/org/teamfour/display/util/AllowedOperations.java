package org.teamfour.display.util;

import org.teamfour.system.enums.Operation;
import org.teamfour.system.enums.Status;

import java.util.List;
import java.util.Map;

public class AllowedOperations {
    public static final Map<Status, List<Operation>> ALLOWED_OPERATIONS = Map.ofEntries(
            Map.entry(Status.IN_PROCESS, List.of(Operation.BEGIN_VOTE_COUNTING)),
            Map.entry(Status.PRE_ELECTION, List.of(Operation.BEGIN_VOTING_WINDOW, Operation.SYSTEM_LOG_EXPORT)),
            Map.entry(Status.POST_ELECTION, List.of(Operation.CONFIGURATION)),
            Map.entry(Status.VOTE_COUNTING, List.of(Operation.VOTE_COUNT_EXPORT, Operation.END_VOTE_PROCESS, Operation.SYSTEM_LOG_EXPORT))
    );
}
