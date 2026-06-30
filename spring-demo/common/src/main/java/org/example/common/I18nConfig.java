package org.example.common;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

@Configuration
public class I18nConfig {

    @Bean
    public MessageSource getMessageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        // 对应上面 i18n/biz-messages
        source.setBasename("i18n/messages");
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        source.setFallbackToSystemLocale(false);
        return source;
    }
}
