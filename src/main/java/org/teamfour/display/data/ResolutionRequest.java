package org.teamfour.display.data;

import org.teamfour.display.enums.RequestType;
import org.teamfour.model.db.Vote;
import org.teamfour.system.enums.Operation;

import java.util.List;

public class ResolutionRequest {
    private final RequestType type;
    private final List<Vote> votes;
    private final Operation operation;
    private final String voterAccessCode;
    private final String adminUsername;
    private final String adminPassword;

    public ResolutionRequest(Builder builder) {
        this.type = builder.type;
        this.votes = builder.votes;
        this.voterAccessCode = builder.voterAccessCode;
        this.adminUsername = builder.adminUsername;
        this.adminPassword = builder.adminPassword;
        this.operation = builder.operation;
    }

    public RequestType getType() {
        return type;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getVoterAccessCode() {
        return voterAccessCode;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    @Override
    public String toString() {
        return "ResolutionRequest{" +
                "type=" + type +
                ", votes=" + votes +
                '}';
    }

    public static class Builder {
        private RequestType type;
        private List<Vote> votes;
        private String voterAccessCode;
        private String adminUsername;
        private String adminPassword;
        private Operation operation;

        public Builder withType(RequestType requestType) {
            this.type = requestType;
            return this;
        }

        public Builder withVotes(List<Vote> votes) {
            this.votes = votes;
            return this;
        }

        public Builder withAccessCode(String code) {
            this.voterAccessCode = code;
            return this;
        }

        public Builder withAdminUsername(String username) {
            this.adminUsername = username;
            return this;
        }

        public Builder withAdminPassword(String password) {
            this.adminPassword = password;
            return this;
        }

        public Builder withOperation(Operation operation) {
            this.operation = operation;
            return this;
        }

        public ResolutionRequest build() {
            return new ResolutionRequest(this);
        }
    }
}
