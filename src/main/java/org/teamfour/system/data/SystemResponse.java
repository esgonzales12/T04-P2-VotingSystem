package org.teamfour.system.data;

import lombok.Builder;
import org.teamfour.system.enums.SystemResponseType;

@Builder
public class SystemResponse {
    private final SystemResponseType type;
    public SystemResponseType getResponseType() {
        return type;
    }
    @Override
    public String toString() {
        return "SystemResponse{" +
                "responseType=" + type +
                '}';
    }
}
