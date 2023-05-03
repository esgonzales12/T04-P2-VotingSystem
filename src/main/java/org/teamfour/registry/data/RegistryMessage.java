package org.teamfour.registry.data;

public class RegistryMessage {
    private final Registry.MessageType type;
    private final String voterAccessCode;
    private final String message;

    public RegistryMessage(Builder builder) {
        this.type = builder.type;
        this.voterAccessCode = builder.voterAccessCode;
        this.message = builder.message;
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

    public static class Builder {
               private Registry.MessageType type;
            private String voterAccessCode;
            private String message;

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

            public RegistryMessage build() {
                return new RegistryMessage(this);
            }
    }
}
