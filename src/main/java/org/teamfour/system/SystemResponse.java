package org.teamfour.system;

import lombok.Builder;
import org.teamfour.display.enums.ResponseType;

@Builder
public class SystemResponse {
    private final ResponseType type;
    public SystemResponse(ResponseType responseType) {
        this.type = responseType;
    }
    public ResponseType getResponseType() {
        return type;
    }
    @Override
    public String toString() {
        return "SystemResponse{" +
                "responseType=" + type +
                '}';
    }
}
