package org.teamfour.registry.data;

import java.io.Serializable;

public class RegistryMessage implements Serializable {
    private final Registry.MessageType type;
    private final String voterAccessCode;
    private final String message;
    private final String voterStatus;

    public RegistryMessage(Builder builder) {
        this.type = builder.type;
        this.voterAccessCode = builder.voterAccessCode;
        this.message = builder.message;
        this.voterStatus = builder.voterStatus;
    }

    public String getVoterAccessCode() {
        return voterAccessCode;
    }

    public Registry.MessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getVoterStatus() {
        return voterStatus;
    }

    @Override
    public String toString() {
        return "RegistryMessage{" +
                "type=" + type +
                ", voterAccessCode='" + voterAccessCode + '\'' +
                ", message='" + message + '\'' +
                ", voterStatus='" + voterStatus + '\'' +
                '}';
    }

    public static class Builder {
        private Registry.MessageType type;
        private String voterAccessCode;
        private String message;
        private String voterStatus;

        public Builder setType(Registry.MessageType type) {
                this.type = type;
                return this;
            }

            public Builder setVoterAccessCode(String voterAccessCode) {
                this.voterAccessCode = voterAccessCode;
                return this;
            }

            public Builder setMessage(String message) {
                this.message = message;
                return this;
            }

            public Builder setVoterStatus(String status) {
                this.voterStatus = status;
                return this;
            }

            public RegistryMessage build() {
                    return new RegistryMessage(this);
                }
    }
}
