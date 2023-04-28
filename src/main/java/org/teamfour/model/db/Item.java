package org.teamfour.model.db;

import org.teamfour.dao.annotations.Column;
import org.teamfour.dao.annotations.PrimaryKey;
import org.teamfour.dao.annotations.Table;
import org.teamfour.model.bsl.Candidate;

import java.util.ArrayList;
import java.util.List;

@Table(name = "item")
public class Item {

    @Column(name = "id")
    @PrimaryKey(columnIdentifier = "id")
    private Integer id;

    @Column(name = "sectionId")
    private Integer sectionId;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "allowedSelections")
    private Integer allowedSelections;
    private final List<Option> options;
    private final List<Candidate> candidates;

    public Item () {
        options = new ArrayList<>();
        candidates = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Integer getAllowedSelections() {
        return allowedSelections;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public List<Option> getOptions(){
        return options;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAllowedSelections(Integer allowedSelections) {
        this.allowedSelections = allowedSelections;
    }
}
