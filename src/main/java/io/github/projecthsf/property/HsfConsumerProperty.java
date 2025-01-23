package io.github.projecthsf.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
@ConfigurationProperties(prefix = "spring.hsf")
@Setter
@Getter
public class HsfConsumerProperty {
    private Customer customer;

    @Getter @Setter
    public static class Customer {
        private String rootPath;
        private String host;
        private Map<String, String> hosts;
    }
}
