# Implement provider from interface

A provider is
- MUST implement a interface
- Have annotation @HsfProvider

Example interface CustomerApi:
```
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

Example implement CustomerApiImpl:

```
package com.spring.provider.provider;

import com.spring.api.CustomerApi;
import com.spring.api.dto.CustomerDTO;
import io.github.projecthsf.annotation.HsfProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@HsfProvider
public class CustomerApiImpl implements CustomerApi {
    @Override
    public CustomerDTO getCustomer(int id) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(1);
        dto.setName("Name 1 -> ");
        return dto;
    }

    @Override
    public List<CustomerDTO> getList(int groupId) {
        List<CustomerDTO> dtos = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(i);
            dto.setName("Name " + i);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public int getCount(int groupId) {
        return 50;
    }

    @Override
    public Map<String, CustomerDTO> getMap(int groupId) {
        Map<String, CustomerDTO> map = new HashMap<>();
        for (int i = 1; i < 5; i++) {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(i);
            dto.setName("Name " + i);
            map.put("customer"  + i, dto);
        }
        return map;
    }
}

```

That's it. let says your app run on http://provider.host then you can access 
```
http://provider.host/hsf/list <~ to see the list all the providers have been implemented
```

And 
```
curl --location 'http://provider.host/hsf' \
--header 'Content-Type: application/json' \
--data '{"classInterface":"com.spring.api.CustomerApi","method":"getCustomer","paramTypes":["int"],"params":[1],"version":"1.0.0"}'
```

Note: 
- HsfProvider support versioning, its mean you can implement multi version for same interface (by default version = 1.0.0)

```
@HsfProvider(version="2.0.0")
public class CustomerApiImpl implements CustomerApi {
}
```

