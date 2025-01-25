# spring-hsf
This framework simplifies API integration by eliminating the need for manual client generation and JSON response parsing.

Dictionary:

- Your application is API Consumer
- The service that provides API data is API Provider

## I. Introduction
**Let says**: To obtain customer data, your application must call an API provided by an external source. The response from this API must following interface:
```
public interface CustomerApi** {
  CustomerDTO getCustomer(int id);
  List<CustomerDTO> getList(int groupId);
  Map<String, CustomerDTO> getMap(int groupId);
  int getCount(int groupId);
}
```


| API Owner |                                                        | RESTFULL | SpringHSF |
|---|--------------------------------------------------------|:--------:|----------:|
| Consumer | Need to know the request url, request method,....      |   YES    |      *NO* |
| Consumer | Make a call to provider                                |   YES    |     *YES* |
| Consumer | Parse JSON response to your desired object             |   YES    |      *NO* |
| Provider | Create a class to implement interface CustomerApi      |   YES    |       YES |
| Provider | Create a controller to handle the new request/response |   YES    |      *NO* |
| Provider | Register new route to handle new request               |   YES    |      *NO* |

### Here is example how Consumer call api to get customer object

#### a. With RESTFULL

``` 
String json = httpClient.get("http://provider/customer/123")
CustomerDTO dto = JSON.parse(json);
```

#### b. With SpringHSF

```
CustomerDTO dto = customerApi.getCustomer(123)
```

### Here example how Provider implement the Customer api

#### a. With RESTFULL

```
public class CustomerApiService implements CustomerApi {
    CustomerDTO getCustomer(int id) { //implement code }
    List<CustomerDTO> getList(int groupId) { //implement code }
    Map<String, CustomerDTO> getMap(int groupId) { //implement code }
    int getCount(int groupId) { //implement code }
}
```

```
@RestController
public class CustomerController {
    @RequestMapping("/customer/{id}")
    CustomerDTO getCustomer(int id) { //implement code }
    
    @RequestMapping("/customer/")
    List<CustomerDTO> getList(int groupId) { //implement code }
    
    @RequestMapping("/customer/{id}/map")
    Map<String, CustomerDTO> getMap(int groupId) { //implement code }
    
    @RequestMapping("/customer/count")
    int getCount(int groupId) { //implement code }
}
```


### 2. With SpringHSF

```
@HsfProvider
public class CustomerApiService implements CustomerApi {
    CustomerDTO getCustomer(int id) { //implement code }
    List<CustomerDTO> getList(int groupId) { //implement code }
    Map<String, CustomerDTO> getMap(int groupId) { //implement code }
    int getCount(int groupId) { //implement code }
}
```

## II. How to use

### 1. Pre condition

Your app MUST run on Spring Boot web
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>3.4.1</version>
</dependency>
```

### 2. Dependency 

Add this dependency to your pom.xml
```
<dependency>
    <groupId>io.github.projecthsf</groupId>
    <artifactId>spring-hsf</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 3. Api interfaces

The API interfaces MUST be accessible  for both Consumer and Provider application

**Hint\*: you can create a separate package to contain only interface and dto support for that interfaces**

Example: your api package contains interface CustomerApi and CustomerDTO
```
public interface CustomerApi {
    CustomerDTO getCustomer(int id);
    List<CustomerDTO> getList(int groupId);
    Map<String, CustomerDTO> getMap(int groupId);
    int getCount(int groupId);
}

@Setter
@Getter
public class CustomerDTO {
    private Integer id;
    private String name;
    private String top;
}
```

### 4. Implement provider

```
@HsfProvider
@Slf4j
public class CustomerApiImpl implements CustomerApi {
    @Override
    public CustomerDTO getCustomer(int id) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(1);
        dto.setName("Name 1");
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
Done for provider, now start your provider app and u can access this HSF like this

```
curl --location 'http://provider.host/hsf' \
--header 'Content-Type: application/json' \
--data '{"classInterface":"com.spring.api.CustomerApi","method":"getCustomer","paramTypes":["int"],"params":[1],"version":"1.0.0"}'
```

And get response:
```
{"id":1,"name":"Name 1","top":null}
```


### Implement provider
1. You need to add interface CustomerApi to bean with @HsfConsumer

```
@Configuration
public class HsfConsumerConfig {
    @HsfConsumer
    private CustomerApi customerApi;
}
```

Note*: currently @HsfConsumer works ONLY under @Configuration

And then you can use it in any your service or controller
Example:
```
@Service
public class CustomerService {
    @Autowired
    private CustomerApi customerApi;
    
    public doSomthing(int customerId) {
        CustomerDTO dto =  customerApi.getCustomer(customerId);
        System.out.println("=== customer name: " + dto.getName());
    }
}
```

And when service CustomerService.doSomething() is called, you will see log customer name print out

That's it.

Now let try demo with all above implements https://github.com/projecthsf/spring-hsf-demo

More documents:

[HSF Overview](https://github.com/projecthsf/spring-hsf/blob/main/docs/overview.md)

[HSF Application Properties](https://github.com/projecthsf/spring-hsf/blob/main/docs/properties.md)

[HSF Consumer](https://github.com/projecthsf/spring-hsf/blob/main/docs/consumer.md)

[HSF Provider](https://github.com/projecthsf/spring-hsf/blob/main/docs/provider.md)

[Report an issue](https://github.com/projecthsf/spring-hsf/issues)

