package org.teamfour.service;

import org.teamfour.dao.VotingDao;
import org.teamfour.model.db.Ballot;
import org.teamfour.model.db.Option;
import org.teamfour.model.db.Vote;
import org.teamfour.registry.data.Registry;
import org.teamfour.registry.data.RegistryMessage;

import java.util.List;
import java.util.Map;

public class VotingServiceImpl implements VotingService{
    VotingDao votingDao = new VotingDao();
    List<Ballot> ballots;
    public VotingServiceImpl(){

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
        return false;
    }
    public boolean recordVotes(List<Vote> votes, Integer ballotId){
        return false;
    }
    public  Ballot findBallot(Integer id){
        return null;
    }
    public  void countVotes(Integer id){

    }
    public Map<Option, Integer> getTabulation(Integer id){
        return null;
    }
}
