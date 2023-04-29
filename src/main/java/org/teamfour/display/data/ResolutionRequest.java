package org.teamfour.display.data;

import org.teamfour.display.enums.RequestType;
import org.teamfour.model.db.Vote;

import java.util.List;

public class ResolutionRequest {
    private final RequestType type;
    private final List<Vote> votes;

    public ResolutionRequest(Builder builder) {
        this.type = builder.type;
        this.votes = builder.votes;
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

        public Builder withType(RequestType requestType) {
            this.type = requestType;
            return this;
        }

        public Builder withVotes(List<Vote> votes) {
            this.votes = votes;
            return this;
        }

        public ResolutionRequest build() {
            return new ResolutionRequest(this);
        }
    }
}
