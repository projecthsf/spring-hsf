package io.github.projecthsf.binding;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HsfResultResolverTest {
    private interface ReturnTypeSchema {
        Person getPerson();
        List<Person> getList();
        Map<String, Person> getMap();
    }
    @Getter
    @Setter
    private static class Person {
        private int id;
        private String name;
        private String top;
    }
    @Test
    void getResponse_Primitive() throws Exception {
        HsfResultResolver resolver = new HsfResultResolver("1", int.class);
        Object result = resolver.getResponse();
        assertEquals(1, result);
    }

    @Test
    void getResponse_RegularObject() throws Exception {
        HsfResultResolver resolver = new HsfResultResolver("{\"id\":3,\"name\":\"Carrot\",\"top\":null}", ReturnTypeSchema.class.getMethod("getPerson").getGenericReturnType());
        Object result = resolver.getResponse();
        assertEquals(Person.class, result.getClass());
        Person person = (Person) result;
        assertEquals(3, person.getId());
        assertEquals("Carrot", person.getName());
        assertNull(person.getTop());
    }

    @Test
    void getResponse_CollectiveObject() throws Exception {
        HsfResultResolver resolver = new HsfResultResolver("[{\"id\":2,\"name\":\"Jin\",\"top\":null},{\"id\":3,\"name\":\"Carrot\",\"top\":null}]", ReturnTypeSchema.class.getMethod("getList").getGenericReturnType());
        Object result = resolver.getResponse();
        assertEquals(ArrayList.class, result.getClass());
        List<Person> persons = (List<Person>) result;
        assertEquals(2, persons.size());
        assertEquals(2, persons.get(0).getId());
        assertEquals("Jin", persons.get(0).getName());
        assertEquals(3, persons.get(1).getId());
        assertEquals("Carrot", persons.get(1).getName());
    }

    @Test
    void getResponse_MapObject() throws Exception {
        HsfResultResolver resolver = new HsfResultResolver("{\"person1\":{\"id\":2,\"name\":\"Jin\",\"top\":null},\"person2\":{\"id\":3,\"name\":\"Carrot\",\"top\":null}}", ReturnTypeSchema.class.getMethod("getMap").getGenericReturnType());
        Object result = resolver.getResponse();
        assertEquals(LinkedHashMap.class, result.getClass());
        Map<String, Person> personMap = (Map<String, Person>) result;
        assertEquals(2, personMap.size());
        assertEquals(2, personMap.get("person1").getId());
        assertEquals("Jin", personMap.get("person1").getName());
        assertEquals(3, personMap.get("person2").getId());
        assertEquals("Carrot", personMap.get("person2").getName());
    }
}