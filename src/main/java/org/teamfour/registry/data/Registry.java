package org.teamfour.registry.data;

public class Registry {
    public enum MessageType {
        VOTER_STATUS,
        VOTER_STATUS_UPDATED,
        ERROR,
        GET_VOTER_STATUS,
        SET_VOTER_STATUS
    }

    public static class VoteStatus {
        public static final String VOTED = "VOTED";
        public static final String NOT_VOTED = "NOT_VOTED";
    }
}
