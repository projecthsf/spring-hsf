# Provider

Dont need any config!!!

However there is a class src/main/java/io/github/projecthsf/binding/provider/HsfProviderProperty.java to store all the class that have annotation @HsfProvider, the values will be filled by Hsf processor when application start.

You can check values by http://provider.host/hsf/list


# Consumer

The config will be loaded from src/main/java/io/github/projecthsf/property/HsfConsumerProperty.java
```
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
```

and the values will load from application.properties
```
spring.hsf.consumer.rootPath=/custom-root-path // default = /hsf
spring.hsf.consumer.host=http://default-provider.host // default provider host
spring.hsf.consumer.hosts.CustomerApi=http://customer-provider.host // for specific interface
```

When a interface call:
```
customerApi.getCustomer() -> call to http://customer-provider.host/custom-root-path (b/c spring.hsf.consumer.hosts.CustomerApi has value)
productApi.getProduct() -> call to http://default-provider.host/custom-root-path (b/c spring.hsf.consumer.hosts.ProductApi doesn't exist)
```

