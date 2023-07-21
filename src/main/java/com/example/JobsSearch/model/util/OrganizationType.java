package com.example.JobsSearch.model.util;

public enum OrganizationType {
    B("B"),
    C("C"),
    E("E");

    private final String value;

    OrganizationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Phương thức chuyển đổi từ String sang enum OrganizationType
    public static OrganizationType fromValue(String value) {
        for (OrganizationType type : OrganizationType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid OrganizationType: " + value);
    }
}

