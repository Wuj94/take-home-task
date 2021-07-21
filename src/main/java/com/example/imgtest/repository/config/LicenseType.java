package com.example.imgtest.repository.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LicenseType {
    TOURNAMENT("tournament", 0), MATCH("match", 1);

    private String value;
    private int id;

    LicenseType(String value, int id) {
        this.value = value;
        this.id = id;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static LicenseType fromValue(String text) {
        for (LicenseType b : LicenseType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

}
