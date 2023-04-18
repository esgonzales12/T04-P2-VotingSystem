package org.teamfour.display.resolver;

import org.teamfour.display.resolver.data.ResolutionRequest;
import org.teamfour.display.resolver.data.ResolutionResponse;

public interface Resolver {
    ResolutionResponse handle(ResolutionRequest request);
}
