# Overview
Simple way to implement api workflow: consumer, provider and http request call


# Diagram

![Alt text](https://github.com/projecthsf/spring-hsf/blob/main/docs/images/hsf-diagram.png "HSF Diagram")


Example: 
```
public interface CustomerApi {
    CustomerDTO getCustomer(int id);
}
```

And when you implement:
```
@HsfProvider
public class CustomerApiImplement CustomerApi {
  // code implements here
}
```
Its mean you register CustomerApi.provider to a /hsf


And when you call
```
CustomerDTO dto = customerApi.getCustomer(1)
```

The ConsumerProxy will convert customerApi.getCustomer(1)  to http request body
```
{"classInterface":"com.spring.api.CustomerApi","method":"getCustomer","paramTypes":["int"],"params":[1],"version":"1.0.0"}
```

and send to HSF controller and get json response, so it same with 
```
String json = http.request.POST http://provider.host/hsf -d {"classInterface":"com.spring.api.CustomerApi","method":"getCustomer","paramTypes":["int"],"params":[1],"version":"1.0.0"}
```

and then ConsumerProxy will parse json to dto object
```
CustomerDTO dto = JSONParser.parse(json);
```

