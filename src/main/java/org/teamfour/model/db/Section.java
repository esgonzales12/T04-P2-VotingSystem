package org.teamfour.model.db;

import org.teamfour.dao.annotations.Column;
import org.teamfour.dao.annotations.PrimaryKey;
import org.teamfour.dao.annotations.Table;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Table(name = "section")
public class Section {

    @Column(name = "id")
    @PrimaryKey(columnIdentifier = "id")
    private Integer id;

    @Column(name = "ballotId")
    private Integer ballotId;

    @Column(name = "name")
    private String name;
    private final List<Item> items;

    public Section() {
        items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBallotId() {
        return ballotId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBallotId(Integer ballotId) {
        this.ballotId = ballotId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BallotSection{" +
                "name='" + name + '\'' +
                ", numItems=" + (isNull(items) ? null : items.size()) +
                "items='" + items +
                '}';
    }

}

