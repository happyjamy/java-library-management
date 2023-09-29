package com.programmers.exception;

import com.programmers.util.MessageProperties;

public enum ErrorCode {
    INVALID_MODE_NUMBER("invalid.mode"),
    INVALID_INPUT("invalid.input"),
    INVALID_EXIT("invalid.exit"),
    INVALID_MENU_NUMBER("invalid.menu"),
    ;

    ErrorCode(String key) {
        this.key = key;
    }
    private final String key;

    public String getMessage() {
        return MessageProperties.getError(key);
    }

}
