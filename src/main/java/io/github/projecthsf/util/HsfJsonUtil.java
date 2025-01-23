package io.github.projecthsf.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.projecthsf.binding.HsfResultResolver;

import java.lang.reflect.Type;

public class HsfJsonUtil {
    public static String serialize(Object data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }

    public static Object deserialize(String json, Type type) throws Exception {
        HsfResultResolver resolver = new HsfResultResolver(json, type);
        return resolver.getResponse();
    }
}
