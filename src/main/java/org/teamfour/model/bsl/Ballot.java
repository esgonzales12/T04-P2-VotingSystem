package org.teamfour.model.bsl;

import java.util.List;

public class Ballot {
    private final String location;
    private final String date;
    private final String name;
    private final String instructions;
    private final List<BallotSection> sections;

    public Ballot(String location, String date, String name, String instructions, List<BallotSection> sections) {
        this.location = location;
        this.date = date;
        this.name = name;
        this.instructions = instructions;
        this.sections = sections;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }

    public List<BallotSection> getSections() {
        return sections;
    }

    @Override
    public String toString() {
        return "Ballot{" +
                "location='" + location + '\'' +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", instructions='" + instructions + '\'' +
                ", sections=" + sections +
                '}';
    }

}
