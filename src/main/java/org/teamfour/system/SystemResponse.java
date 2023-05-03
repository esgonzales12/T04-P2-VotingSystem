package org.teamfour.system;

import org.teamfour.display.enums.RequestType;

public class SystemResponse {
    private final RequestType requestType;
    public SystemResponse(RequestType responseType) {
        this.requestType = responseType;
    }
    public RequestType getResponseType() {
        return requestType;
    }

    @Override
    public String toString() {
        return "SystemResponse{" +
                "responseType=" + requestType +
                '}';
    }
}
