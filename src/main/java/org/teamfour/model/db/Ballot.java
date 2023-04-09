package org.teamfour.model.db;

import org.teamfour.model.bsl.BallotSection;

import java.util.List;

public class Ballot extends org.teamfour.model.bsl.Ballot {

    public Ballot(String location, String date, String name, String instructions, List<BallotSection> sections) {
        super(location, date, name, instructions, sections);
    }
}
