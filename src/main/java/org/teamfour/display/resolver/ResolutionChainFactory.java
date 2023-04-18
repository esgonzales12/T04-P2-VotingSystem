package org.teamfour.display.resolver;

import org.teamfour.display.components.AdminSignIn;
import org.teamfour.display.enums.ResolutionPolicy;
import org.teamfour.display.enums.TransitionPolicy;
import org.teamfour.display.manager.DisplayManager;
import org.teamfour.display.resolver.data.Step;
import org.teamfour.system.enums.Operation;

public class ResolutionChainFactory {

    public ResolutionChain build(Operation operation, DisplayManager manager) {
        ResolutionChain.Builder builder = new ResolutionChain.Builder(manager);
        switch (operation) {
            case INITIATE_VOTING -> {
                builder.withStep(new Step.Builder()
                                        .withTransition(TransitionPolicy.CONFIRM)
                                        .withResolutionPolicy(ResolutionPolicy.DESTROY)
                                        .withForm(new AdminSignIn())
                                        .build())
                        .withStep(new Step.Builder()
                                        .withTransition(TransitionPolicy.CONFIRM)
                                        .withResolutionPolicy(ResolutionPolicy.DESTROY)
                                        .withForm(new AdminSignIn())
                                        .build())
                        .build();
            }
            case VOTER_RESET -> {
            }
            case BALLOT_CONFIGURATION -> {

            }
        }
        return builder.build();
    }

}
