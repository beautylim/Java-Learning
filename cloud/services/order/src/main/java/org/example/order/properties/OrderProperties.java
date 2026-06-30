package org.example.order.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "order")
public class OrderProperties {

    private Long timeout;

    private int autoConfirmDays;
}
