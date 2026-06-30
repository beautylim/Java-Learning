package org.example.common;

public enum MessageEnum {

    USER_EXIST("USER0001"),
    USER_NOT_EXIST("USER0002"),
    PASSWORD_ERROR("USER0003"),
    REGISTER_FAILED("USER0004");

    private final String messageId;

    MessageEnum(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}
