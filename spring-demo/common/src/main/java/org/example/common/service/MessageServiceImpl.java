package org.example.common.service;

import lombok.RequiredArgsConstructor;
import org.example.common.error.ResultError;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final MessageSource messageSource;

    @Override
    public ResultError convert(String messageId) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(messageId+".message", null, locale);
        String remediation = messageSource.getMessage(messageId+".remediation", null, locale);
        return ResultError.builder().message(message).remediation(remediation).timestamp(System.currentTimeMillis()).build();
    }

    @Override
    public int getHttpCode(String messageId) {
        Locale locale = LocaleContextHolder.getLocale();
        String httpCode = messageSource.getMessage(messageId+".code", null, locale);
        return Integer.parseInt(httpCode);
    }
}
