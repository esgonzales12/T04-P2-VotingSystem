package org.teamfour.service;

import org.teamfour.dao.VotingDao;
import org.teamfour.display.components.voting.common.VoteValue;
import org.teamfour.logging.LogBase;
import org.teamfour.model.db.*;
import org.teamfour.registry.client.RegistryFacade;
import org.teamfour.registry.data.Registry;
import org.teamfour.registry.data.RegistryMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VotingServiceImpl extends LogBase implements VotingService {
    VotingDao votingDao;
    RegistryFacade registryFacade;
    List<Ballot> ballots;
    public VotingServiceImpl(RegistryFacade registryFacade){
        super("VotingService");
        this.votingDao = new VotingDao();
        this.registryFacade = registryFacade;
        this.ballots = new ArrayList<>();
    }
    public Ballot saveBallot(org.teamfour.model.bsl.Ballot ballot){
        Ballot newBallot = votingDao.saveBallot(ballot);
        this.ballots.add(newBallot);
        return newBallot;
    }
    public boolean voterLogin(String voterAccessCode){
        RegistryMessage message = new RegistryMessage.Builder()
                .setType(Registry.MessageType.GET_VOTER_STATUS)
                .setVoterAccessCode(voterAccessCode)
                .build();
        log.info("SENDING REGISTRY REQUEST: " + message.toString());
        RegistryMessage response = registryFacade.handleRequest(message);
        log.info("RECEIVED RESPONSE: " + response.toString());
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
            if (vote.getValue() == null) {
                log.error("VOTE VALUE IS NULL FOR VOTE: " + vote);
                continue;
            } else if (vote.getValue().equals(VoteValue.NONE)) {
                continue;
            }
            sql = String.format(
                    """
                    UPDATE OPTION
                    SET count = count + 1
                    WHERE id = %d;
                    """, vote.getOptionId());
            count += votingDao.update(sql);
        }
        RegistryMessage.Builder builder = new RegistryMessage.Builder();
        builder.setType(Registry.MessageType.MARK_VOTE_COUNTED);
        builder.setVoterAccessCode(voterAccessCode);
        RegistryMessage message = new RegistryMessage(builder);
        RegistryMessage response = registryFacade.handleRequest(message);

        if (response.getType() == Registry.MessageType.ERROR) {
            log.info("ERROR UPDATING VOTER STATUS");
            return false;
        }
        return true;
    }
    public  Ballot findBallot(Integer id){
        String ballotSql = String.format(
                """
                SELECT * FROM BALLOT
                WHERE id = %d;
                """, id);
        Ballot ballot = votingDao.selectOne(Ballot.class, ballotSql).get();
        String sectionsql = String.format(
                """
                SELECT * FROM Section
                WHERE section.ballotid = %d;
                """, id);
        ballot.getSections().addAll(votingDao.selectMany(Section.class, sectionsql));
        String itemSql;
        String optionSql;
        for (Section section: ballot.getSections()) {
            itemSql = String.format(
                    """
                    SELECT * FROM item
                    WHERE item.sectionid = %d;
                    """, section.getId());
            section.getItems().addAll(votingDao.selectMany(Item.class, itemSql));
            for (Item item: section.getItems()){
                optionSql = String.format(
                        """
                        SELECT * FROM option
                        WHERE option.itemid = %d;
                        """, item.getId());
                item.getOptions().addAll(votingDao.selectMany(Option.class, optionSql));
            }
        }
        return ballot;
    }
    public  void countVotes(Integer id){

    }
    public Map<Option, Integer> getTabulation(Integer id){

        return null;
    }
}
