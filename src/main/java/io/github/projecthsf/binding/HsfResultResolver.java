package io.github.projecthsf.binding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.github.projecthsf.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InvalidClassException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HsfResultResolver {
    private final String responseJson;
    private final Type responseType;

    private static final Map<String, Class<?>> PRIMITIVE_MAP = new HashMap<>(10);
    static {
        PRIMITIVE_MAP.put("int", Integer.class);
        PRIMITIVE_MAP.put("byte", Byte.class);
        PRIMITIVE_MAP.put("char", Character.class);
        PRIMITIVE_MAP.put("boolean", Boolean.class);
        PRIMITIVE_MAP.put("double", Double.class);
        PRIMITIVE_MAP.put("float", Float.class);
        PRIMITIVE_MAP.put("long", Long.class);
        PRIMITIVE_MAP.put("short", Short.class);
        PRIMITIVE_MAP.put("void", Void.class);
    }
    public HsfResultResolver(String responseJson, Type responseType) {
        this.responseJson = responseJson;
        this.responseType = responseType;
    }

    public Object getResponse() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TypeFactory factory = TypeFactory.defaultInstance();

        if (responseType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) responseType;
            Class<?> rawType = getTypeClass(type.getRawType());
            if (Collection.class.isAssignableFrom(rawType)) {
                return getCollectionResponse(mapper, factory, type.getActualTypeArguments());
            }

            if (Map.class.isAssignableFrom(rawType)) {
                return getMapResponse(mapper, factory, type.getActualTypeArguments());
            }

            throw new InvalidClassException(type.getRawType().getTypeName(), "Class type supported");
        }

        Class<?> clazz = getTypeClass(responseType);
        return mapper.readValue(responseJson, clazz);
    }

    private Object getCollectionResponse(ObjectMapper mapper, TypeFactory factory, Type[] types) throws JsonProcessingException {
        Class<?> clazz = getTypeClass(types[0]);
        CollectionType listType = factory.constructCollectionType(List.class, clazz);
        return mapper.readValue(responseJson, listType);
    }

    private Object getMapResponse(ObjectMapper mapper, TypeFactory factory, Type[] types) throws JsonProcessingException {
        Class<?> keyClazz = getTypeClass(types[0]);
        Class<?> valueClazz = getTypeClass(types[1]);
        MapType mapType    = factory.constructMapType(Map.class, keyClazz, valueClazz);
        return mapper.readValue(responseJson, mapType);
    }

    private Class<?> getTypeClass(Type type) {
        if (PRIMITIVE_MAP.containsKey(type.getTypeName())) {
            return PRIMITIVE_MAP.get(type.getTypeName());
        }

        return ClassUtil.getClass(type.getTypeName());
    }
}
