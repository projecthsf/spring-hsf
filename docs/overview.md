# Overview
Simple way to implement api workflow: consumer, provider and http request call

Example: CustomerApi is a interface

And when you implement:
```
@HsfProvider
public class CustomerApiImplement CustomerApi {
  // code implements here
}
```
Its mean you register CustomerApi to a /hsf provider


And from consumer side, when u call
```
CustomerDTO dto = customerApi.getCustomer()
```

it is same with:
```
String json = http.request.POST http://provider.host/hsf -d {"classInterface":"com.spring.api.CustomerApi","method":"getCustomer","paramTypes":["int"],"params":[1],"version":"1.0.0"}
CustomerDTO dto = JSONParser.parse(json);
```

# Diagram

![Alt text](https://github.com/projecthsf/spring-hsf/blob/main/docs/images/hsf-diagram.png "HSF Diagram")
