package org.teamfour.system.data;

import org.teamfour.model.db.Ballot;
import org.teamfour.model.db.Vote;
import org.teamfour.system.VotingSystem;
import org.teamfour.system.enums.Operation;
import org.teamfour.system.enums.RequestType;
import org.teamfour.service.VotingService;

import java.util.List;

public class SystemRequest {
    private final RequestType type;

    private final Operation operation;
    private final String adminUsername;
    private final String adminPassword;
    public SystemRequest(Builder builder) {
        this.operation = builder.operation;
        this.adminUsername = builder.adminUsername;
        this.adminPassword = builder.adminPassword;
        this.type = builder.type;
    }

    public RequestType getType() {
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

    public List<Vote> getVotes() {

        return null;
    }



    public static class Builder {
        private Operation operation;
        private String adminUsername;
        private String adminPassword;
        private RequestType type;

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

        public Builder withType(RequestType type) {
            this.type = type;
            return this;
        }

        public SystemRequest build() {
            return new SystemRequest(this);
        }
    }
}