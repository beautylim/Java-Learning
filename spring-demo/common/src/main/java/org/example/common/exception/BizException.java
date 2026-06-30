package org.example.common.exception;

import org.example.common.MessageEnum;

public class BizException extends RuntimeException {

    private MessageEnum messageEnum;

    public BizException(MessageEnum messageEnum) {
        this.messageEnum = messageEnum;
    }

    public MessageEnum getMessageEnum() {
        return messageEnum;
    }
}
