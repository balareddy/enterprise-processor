package com.enterprise.message.model;

public enum MessageType {

    BLUE("blue"),
    RED("red"),
    YELLOW("yellow");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
