package io.github.projecthsf.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.projecthsf.binding.HsfResultResolver;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;

@Slf4j
public class HsfJsonUtil {
    public static String serialize(Object data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            log.error("Serialize failed, data: {} error: {}", data, e.getMessage(), e);
        }

        return null;
    }

    public static Object deserialize(String json, Type type) throws Exception {
        HsfResultResolver resolver = new HsfResultResolver(json, type);
        return resolver.getResponse();
    }
}
