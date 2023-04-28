package org.teamfour.model.enums;

public enum OptionType {

    OPTION("option"),
    CANDIDATE("candidate");
    private final String value;

    OptionType(String s) {
        this.value = s;
    }

    public final String value() {
        return value;
    }
}
