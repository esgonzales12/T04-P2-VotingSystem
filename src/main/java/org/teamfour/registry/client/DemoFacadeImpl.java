package org.teamfour.registry.client;

import org.teamfour.registry.RegistryFacade;
import org.teamfour.registry.data.Registry;
import org.teamfour.registry.data.RegistryMessage;

import java.util.HashMap;
import java.util.Map;

public class DemoFacadeImpl implements RegistryFacade {
    private final Map<String, String> voters;

    public DemoFacadeImpl() {
        voters = new HashMap<>();
    }

    @Override
    public RegistryMessage handleRequest(RegistryMessage request) {
        switch (request.getType()) {
            case GET_VOTER_STATUS -> {
                if (!voters.containsKey(request.getVoterAccessCode())) {
                    voters.put(request.getVoterAccessCode(), Registry.VoteStatus.NOT_VOTED);
                    return new RegistryMessage.Builder()
                            .setType(Registry.MessageType.VOTER_STATUS)
                            .setVoterStatus(Registry.VoteStatus.NOT_VOTED)
                            .build();
                } else {
                    String status = voters.get(request.getVoterAccessCode());
                    return new RegistryMessage.Builder()
                            .setType(Registry.MessageType.VOTER_STATUS)
                            .setVoterStatus(status)
                            .build();
                }
            }
            case MARK_VOTE_COUNTED -> {
                if (!voters.containsKey(request.getVoterAccessCode())) {
                    return new RegistryMessage.Builder()
                            .setType(Registry.MessageType.ERROR)
                            .setMessage("unable to locate voter")
                            .build();
                } else {
                    voters.put(request.getVoterAccessCode(), Registry.VoteStatus.VOTED);
                    return new RegistryMessage.Builder()
                            .setType(Registry.MessageType.VOTER_STATUS_UPDATED)
                            .build();
                }

            }
        }
        return new RegistryMessage.Builder()
                .setType(Registry.MessageType.ERROR)
                .setMessage("unable to handle request")
                .build();
    }
}
