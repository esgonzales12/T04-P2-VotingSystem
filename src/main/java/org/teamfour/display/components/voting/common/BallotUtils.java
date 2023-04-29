package org.teamfour.display.components.voting.common;

import org.teamfour.display.components.voting.CandidateCard;
import org.teamfour.model.db.Option;

public class BallotUtils {

    public static CandidateCard getCandidateCard(Option option, boolean proposition) {
        String primaryText = proposition ? option.getChoice() : option.getName();
        String secondaryText = proposition ? "" : option.getParty();
        return new CandidateCard(primaryText, secondaryText, option.getId());
    }
}
