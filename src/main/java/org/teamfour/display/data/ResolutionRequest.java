package org.teamfour.display.data;

import org.teamfour.display.enums.RequestType;
import org.teamfour.model.db.Vote;

import java.util.List;

public class ResolutionRequest {
    private final RequestType type;
    private final List<Vote> votes;
    private final String voterAccessCode;

    public ResolutionRequest(Builder builder) {
        this.type = builder.type;
        this.votes = builder.votes;
        this.voterAccessCode = builder.voterAccessCode;
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

        public ResolutionRequest build() {
            return new ResolutionRequest(this);
        }
    }
}
