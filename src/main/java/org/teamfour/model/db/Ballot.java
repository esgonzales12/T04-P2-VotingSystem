package org.teamfour.model.db;

import org.teamfour.dao.annotations.Column;
import org.teamfour.dao.annotations.PrimaryKey;
import org.teamfour.dao.annotations.Table;

import java.util.ArrayList;
import java.util.List;

@Table(name = "ballot")
public class Ballot {
    @Column(name = "id")
    @PrimaryKey(columnIdentifier = "id")
    private Integer id;

    @Column(name = "location")
    private String location;

    @Column(name = "date")
    private String date;

    @Column(name = "name")
    private String name;

    @Column(name = "instructions")
    private String instructions;

    @Column(name = "hash")
    private String hash;

    private final List<Section> sections;

    public Ballot() {
        sections = new ArrayList<>();
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

    public List<Section> getSections() {
        return sections;
    }

    public Integer getId() {
        return id;
    }

    public String getHash() {
        return hash;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setHash(String hash) {
        this.hash = hash;
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
