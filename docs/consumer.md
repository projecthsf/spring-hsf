# Consumer

Let says you have 2 API providers:

- Customer API -> provider for CustomerApi and run on host http:// customer.provider.host

```java
package com.spring.api;

import com.spring.api.dto.CustomerDTO;

import java.util.List;
import java.util.Map;

public interface CustomerApi {
    CustomerDTO getCustomer(int id);
    List<CustomerDTO> getList(int groupId);
    Map<String, CustomerDTO> getMap(int groupId);
    int getCount(int groupId);
}
```
- Product API -> provider for ProductApi and run on host http://product.provider.host
```java
package com.spring.api;

import com.spring.api.dto.ProductDTO;

import java.util.List;

public interface ProductApi {
    ProductDTO getProduct(int id);
    List<ProductDTO> getProducts(int categoryId);
    int getCount(int categoryId);
}
```

## Step 1: Update your application.properties:
```properties
spring.hsf.customer.host=http://default-provider.host
spring.hsf.customer.hosts.com.spring.api.CustomerApi=http:// customer.provider.host
spring.hsf.customer.hosts.com.spring.api.ProductApi=http://product.provider.host
```

Note: 
if spring.hsf.customer.hosts.com.spring.api.CustomerApi doesnt exist, **spring.hsf.customer.host** will be use as default

## Step 2: Create bean

You can create file HsfConsumerBean or just add to existed file has annotation @Configuration
```java
package com.spring.consumer.config;

import com.spring.api.CustomerApi;
import com.spring.api.ProductApi;
import io.github.projecthsf.annotation.HsfConsumer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HsfConsumerConfig {
    @HsfConsumer
    private CustomerApi customerApi;
    
    @HsfConsumer
    private ProductApi api;
}
```

Note: 

- @Configuration is required for @HsfConsumer
- You can specific provider version by add version as param, ie: @HsfConsumer(version="2.0.0"), by default version = 1.0.0

## Step 3: Autowired

Now you can autowired into service/component/controller/.... and make a call
```java
package com.spring.consumer;

import com.spring.api.CustomerApi;
import com.spring.api.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    CustomerApi customerApi;
    
    public CustomerDTO getCustomer() {
        return customerApi.getCustomer(1);
    }
}
```
