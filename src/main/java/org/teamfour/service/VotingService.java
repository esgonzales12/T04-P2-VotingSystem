package org.teamfour.service;

import org.teamfour.model.db.Ballot;
import org.teamfour.model.db.Option;
import org.teamfour.model.db.Vote;

import java.util.List;
import java.util.Map;

public interface VotingService {

    Ballot saveBallot(org.teamfour.model.bsl.Ballot ballot);
    boolean voterLogin(String voterAccessCode);
    boolean recordVotes(List<Vote> votes, String voterAccessCode);
    Ballot findBallot(Integer id);
    void countVotes(Integer id);
    Map<Option, Integer> getTabulation(Integer id);

}
