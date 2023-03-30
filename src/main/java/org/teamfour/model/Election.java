package org.teamfour.model;

public class Election {
    private final Integer id;
    private final String location;
    private final String startDate;
    private final String endDate;

    public Election(String location, Integer id, String startDate, String endDate) {
        this.location = location;
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public Integer getId() {
        return id;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Election{" +
                "location='" + location + '\'' +
                ", id=" + id +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
