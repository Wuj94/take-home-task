package com.example.imgtest.repository.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Summary {
    AvB("AvB", 0), AvBTime("AvBTime", 1);

    private final String value;
    private final int indentifier;

    Summary(String value, int identifier) {
        this.value = value;
        this.indentifier = identifier;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Summary fromValue(String text) {
        for (Summary b : Summary.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

}
