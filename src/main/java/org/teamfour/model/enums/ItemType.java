package org.teamfour.model.enums;

public enum ItemType {
    RANKED("ranked"),
    CONTEST("contest"),
    APPROVAL("approval"),
    PROPOSITION("proposition");

    private final String value;

    ItemType(String s) {
        this.value = s;
    }

    public final String value() {
        return value;
    }
}