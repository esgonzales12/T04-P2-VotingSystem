package org.teamfour.dao;


import org.teamfour.model.bsl.BallotItem;
import org.teamfour.model.bsl.BallotSection;
import org.teamfour.model.bsl.Candidate;
import org.teamfour.model.db.Ballot;
import org.teamfour.model.db.Item;
import org.teamfour.model.db.Option;
import org.teamfour.model.db.Section;
import org.teamfour.model.enums.ItemType;

import java.util.List;

public class VotingDao extends DaoBase {
    private static final String BALLOT_COLS = "(name, instructions, location, date)";
    private static final String SECTION_COLS = "(ballotId, name)";
    private static final String ITEM_COLS = "(sectionId, description, type, name, allowedSelections)";
    private static final String BALLOT = "ballot";
    private static final String SECTION = "section";
    private static final String ITEM = "item";
    private static final String OPTION = "option";
    private static final String VOTE = "vote";


    public Ballot saveBallot(org.teamfour.model.bsl.Ballot ballot) {
        String ballotValues = String.format("('%s', '%s', '%s', '%s')",
                ballot.getName(), ballot.getInstructions(), ballot.getLocation(), ballot.getDate());
        Ballot dbBallot = create(Ballot.class, new QueryBuilder()
                                                    .insertInto(BALLOT)
                                                    .values(BALLOT_COLS, ballotValues)
                                                    .build());
        for (BallotSection ballotSection: ballot.getSections()) {

            String sectionValues = String.format("('%d', '%s')",
                    dbBallot.getId(), ballotSection.getName());

            Section section = create(Section.class, new QueryBuilder()
                    .insertInto(SECTION)
                    .values(SECTION_COLS, sectionValues)
                    .build());

            dbBallot.getSections().add(section);

            for (BallotItem ballotItem: ballotSection.getItems()) {

                String itemValues = String.format("('%d', '%s', '%s', '%s', '%d')",
                        section.getId(), ballotItem.getDescription(), ballotItem.getType(),
                        ballotItem.getName(), ballotItem.getAllowedSelections());

                Item item = create(Item.class, new QueryBuilder()
                        .insertInto(ITEM)
                        .values(ITEM_COLS, itemValues)
                        .build());
                section.getItems().add(item);

                saveItemOptions(item, ballotItem);
            }
        }

        return dbBallot;

    }

    private void saveItemOptions(Item item, BallotItem ballotItem) {

        if (ballotItem.getType().equals(ItemType.PROPOSITION.value())) {
            for (String optionText: ballotItem.getOptions()) {
                String optionValues = String.format("('%d', '%s')", item.getId(), optionText);

                Option option = create(Option.class,
                        new QueryBuilder()
                                .insertInto(OPTION)
                                .values("(itemId, choice)", optionValues)
                                .build());

                item.getOptions().add(option);
            }
            return;
        }

        for (Candidate candidate: ballotItem.getCandidates()) {
            String optionValues = String.format("('%d', '%s', '%s')", item.getId(), candidate.getName(), candidate.getParty());

            Option option = create(Option.class,
                    new QueryBuilder()
                            .insertInto(OPTION)
                            .values("(itemId, name, party)", optionValues)
                            .build());

            item.getOptions().add(option);
        }

    }
}
