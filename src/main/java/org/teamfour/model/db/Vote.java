package org.teamfour.model.db;

import org.teamfour.dao.annotations.Column;
import org.teamfour.dao.annotations.PrimaryKey;
import org.teamfour.dao.annotations.Table;

@Table(name = "vote")
public class Vote {
    @Column(name = "id")
    @PrimaryKey(columnIdentifier = "id")
    private Integer id;

    @Column(name = "itemId")
    private Integer itemId;

    @Column(name = "selectionId")
    private Integer optionId;

    @Column(name = "value")
    private String value;

    @Column(name = "date")
    private String date;

    @Column(name = "finalized")
    private Integer finalized;

    public Vote(Integer id, Integer itemId, Integer optionId, String value, String date, Integer finalized) {
        this.id = id;
        this.itemId = itemId;
        this.optionId = optionId;
        this.value = value;
        this.date = date;
        this.finalized = finalized;
    }

    public Vote() {}

    public Integer getId() {
        return id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public String getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }

    public Integer getFinalized() {
        return finalized;
    }
}
