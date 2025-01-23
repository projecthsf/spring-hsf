package io.github.projecthsf.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.projecthsf.util.HsfJsonUtil;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HsfJsonUtilTest {
    @Getter
    @Setter
    private static class Person {
        private int id;
        private String name;
        private String top;
    }
    @Test
    void serialize() throws JsonProcessingException {
        Person person = new Person();
        person.setId(3);
        person.setName("Carrot");

        String result = HsfJsonUtil.serialize(person);
        String expectedJson = "{\"id\":3,\"name\":\"Carrot\",\"top\":null}";
        assertEquals(expectedJson, result);
    }

    @Test
    void deserialize() throws Exception {
        String json = "{\"id\":3,\"name\":\"Carrot\",\"top\":null}";

        Object result = HsfJsonUtil.deserialize(json, Person.class);
        assertEquals(Person.class, result.getClass());
        Person person = (Person) result;
        assertEquals(3, person.getId());
        assertEquals("Carrot", person.getName());
        assertNull(person.getTop());
    }
}