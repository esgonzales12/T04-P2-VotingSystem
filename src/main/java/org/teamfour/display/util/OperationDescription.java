package org.teamfour.display.util;

import org.teamfour.system.enums.Operation;

import java.util.Map;

public class OperationDescription {

    public static final Map<Operation, String> DESCRIPTIONS = Map.ofEntries(
            Map.entry(Operation.BEGIN_VOTING_WINDOW, "description"),
            Map.entry(Operation.CONFIGURATION, "description"),
            Map.entry(Operation.BEGIN_VOTE_COUNTING, "description"),
            Map.entry(Operation.END_VOTE_PROCESS, "description"),
            Map.entry(Operation.SYSTEM_LOG_EXPORT, "description"),
            Map.entry(Operation.VOTE_COUNT_EXPORT, "description"));
}
