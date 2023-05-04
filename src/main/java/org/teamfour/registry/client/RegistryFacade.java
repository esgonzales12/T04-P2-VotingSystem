package org.teamfour.registry.client;

import org.teamfour.registry.data.RegistryMessage;

public interface RegistryFacade {
    RegistryMessage handleRequest(RegistryMessage request);
}
