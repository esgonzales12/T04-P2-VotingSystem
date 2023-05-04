package org.teamfour.registry;

import org.teamfour.registry.data.RegistryMessage;

public interface RegistryFacade {
    RegistryMessage handleRequest(RegistryMessage request);
}
