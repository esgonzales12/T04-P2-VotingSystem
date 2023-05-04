package org.teamfour.system.data;

import org.teamfour.model.db.Vote;
import org.teamfour.system.enums.Operation;
import org.teamfour.system.enums.SystemRequestType;

import java.util.List;

public class SystemRequest {
    private final SystemRequestType type;

    private final Operation operation;
    private final String adminUsername;
    private final String adminPassword;
    private final String voterAccessCode;
    private final List<Vote> votes;

    public SystemRequest(Builder builder) {
        this.operation = builder.operation;
        this.adminUsername = builder.adminUsername;
        this.adminPassword = builder.adminPassword;
        this.type = builder.type;
        this.votes = builder.votes;
        this.voterAccessCode = builder.accessCode;
    }

    public SystemRequestType getType() {
        return type;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    @Override
    public String toString() {
        return "SystemRequest{" +
                "operation=" + operation +
                '}';
    }

    public String getVoterAccessCode() {
        return voterAccessCode;
    }

    public List<Vote> getVotes() {
        return votes;
    }


    public static class Builder {
        private Operation operation;
        private String adminUsername;
        private String adminPassword;
        private SystemRequestType type;
        private String accessCode;
        private List<Vote> votes;


        public Builder withOperation(Operation operation) {
            this.operation = operation;
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

        public Builder withType(SystemRequestType type) {
            this.type = type;
            return this;
        }

        public Builder withVotes(List<Vote> votes) {
            this.votes = votes;
            return this;
        }

        public Builder withVoterAccessCode(String accessCode) {
            this.accessCode = accessCode;
            return this;
        }

        public SystemRequest build() {
            return new SystemRequest(this);
        }
    }
}