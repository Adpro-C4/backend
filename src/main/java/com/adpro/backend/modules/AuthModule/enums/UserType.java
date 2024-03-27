package com.adpro.backend.modules.authmodule.enums;

public enum UserType {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER");

    private final String userType;

    UserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public static boolean contains(String value) {
        for (UserType userType : values()) {
            if (userType.getUserType().equals(value)) {
                return true;
            }
        }
        return false;
    }
}

