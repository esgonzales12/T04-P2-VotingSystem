package org.teamfour.display.util;

import org.teamfour.system.enums.Operation;

import java.util.Map;

import static org.teamfour.system.enums.Operation.*;

public class OperationPrompts {
    public static final Map<Operation, String> COMPLETION_PROMPTS = Map.ofEntries(
            Map.entry(BEGIN_VOTING_WINDOW, "Voting window has been started"),
            Map.entry(CONFIGURATION, "Configuration complete, select continue to review ballot format"),
            Map.entry(VOTE_COUNT_EXPORT, "Vote tabulation results have been exported"),
            Map.entry(SYSTEM_LOG_EXPORT, "System logs have been exported."),
            Map.entry(BEGIN_VOTE_COUNTING, "Vote tabulation complete, the vote window has been ended"),
            Map.entry(END_VOTE_PROCESS, "The voting process has been ended. No ballot is currently configured."));
}
