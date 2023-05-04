package org.teamfour.service;

import org.teamfour.dao.VotingDao;
import org.teamfour.model.db.Ballot;
import org.teamfour.model.db.Option;
import org.teamfour.model.db.Vote;
import org.teamfour.registry.RequestHandler;
import org.teamfour.registry.client.RegistryFacade;
import org.teamfour.registry.data.Registry;
import org.teamfour.registry.data.RegistryMessage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VotingServiceImpl implements VotingService{
    VotingDao votingDao = new VotingDao();
    RegistryFacade registryFacade;
    List<Ballot> ballots;
    public VotingServiceImpl(RegistryFacade registryFacade){
        this.registryFacade = registryFacade;
    }
    public Ballot saveBallot(org.teamfour.model.bsl.Ballot ballot){
        Ballot newBallot = votingDao.saveBallot(ballot);
        this.ballots.add(newBallot);
        return newBallot;
    }
    public boolean voterLogin(String voterAccessCode){
        RegistryMessage.Builder builder = new RegistryMessage.Builder();
        builder.setType(Registry.MessageType.GET_VOTER_STATUS);
        builder.setVoterAccessCode(voterAccessCode);
        RegistryMessage message = new RegistryMessage(builder);
        RegistryMessage response = registryFacade.handleRequest(message);
        switch (response.getType()){
            case VOTER_STATUS:
                switch (response.getVoterStatus()){
                    case "VOTED":
                        return false;
                    case "NOT_VOTED":
                        return true;
                }
                break;
            case ERROR:
                return false;
        }
        return false;
    }

    public boolean recordVotes(List<Vote> votes, String voterAccessCode){
        String sql;
        int count = 0;
        for(Vote vote : votes){
            sql = String.format(
                    """
                    UPDATE OPTION
                    SET count = count + 1
                    WHERE id = %d;
                    """, vote.getId());
            count += votingDao.update(sql);
        }
        RegistryMessage.Builder builder = new RegistryMessage.Builder();
        builder.setType(Registry.MessageType.MARK_VOTE_COUNTED);
        builder.setVoterAccessCode(voterAccessCode);
        RegistryMessage message = new RegistryMessage(builder);
        registryFacade.handleRequest(message);
        if (count==votes.size()){
            return true;
        } else {
            return false;
        }

    }
    public  Ballot findBallot(Integer id){
        for (Ballot ballot: ballots) {
            if (Objects.equals(ballot.getId(), id)){
                return ballot;
            }
        }
        return null;
    }
    public  void countVotes(Integer id){

    }
    public Map<Option, Integer> getTabulation(Integer id){

        return null;
    }
}
