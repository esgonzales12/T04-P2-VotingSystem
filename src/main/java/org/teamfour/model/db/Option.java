package org.teamfour.model.db;

import org.teamfour.dao.annotations.Column;
import org.teamfour.dao.annotations.PrimaryKey;
import org.teamfour.dao.annotations.Table;

@Table(name = "option")
public class Option {

    @Column(name = "id")
    @PrimaryKey(columnIdentifier = "id")
    private Integer id;

    @Column(name = "itemId")
    private Integer itemId;

    @Column(name = "name")
    private String name;

    @Column(name = "party")
    private String party;

    @Column(name = "choice")
    private String choice;

    @Column(name = "count")
    private Integer count;

    public Option(Integer id, Integer itemId, String firstname, String party, String option) {
        this.id = id;
        this.itemId = itemId;
        this.name = firstname;
        this.party = party;
        this.choice = option;
    }

    public Option() {

    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getChoice() {
        return choice;
    }

    public Integer getId() {
        return id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", name='" + name + '\'' +
                ", party='" + party + '\'' +
                ", option='" + choice + '\'' +
                '}';
    }
}
