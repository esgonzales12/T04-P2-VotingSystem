package org.teamfour.system;

import org.teamfour.system.enums.Operation;

public class SystemRequest {
    private final Operation operation;
    private final String adminUsername;
    private final String adminPassword;

    public SystemRequest(Builder builder) {
        this.operation = builder.operation;
        this.adminUsername = builder.adminUsername;
        this.adminPassword = builder.adminPassword;
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

    public static class Builder {
        private Operation operation;
        private String adminUsername;
        private String adminPassword;

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

        public SystemRequest build() {
            return new SystemRequest(this);
        }
    }
}