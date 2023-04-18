package org.teamfour.display.util;

import org.teamfour.system.enums.Operation;

import java.util.Map;

public class OperationDescription {

    public static final Map<Operation, String> DESCRIPTIONS = Map.ofEntries(
            Map.entry(Operation.INITIATE_VOTING, "description"),
            Map.entry(Operation.BALLOT_CONFIGURATION, "description"),
            Map.entry(Operation.VOTER_RESET, "description"));
}
