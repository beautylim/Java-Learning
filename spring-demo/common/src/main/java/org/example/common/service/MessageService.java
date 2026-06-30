package org.example.common.service;

import org.example.common.error.ResultError;

public interface MessageService {

    ResultError convert(String messageId);

    int getHttpCode(String messageId);
}
