package org.teamfour.display.data;

import org.teamfour.display.enums.ResponseType;

public class ResolutionResponse {
    private final ResponseType responseType;

    public ResolutionResponse(ResponseType responseType) {
        this.responseType = responseType;
    }

    public ResponseType getResponseType() {
        return responseType;
    }
}
