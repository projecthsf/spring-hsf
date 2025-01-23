package io.github.projecthsf.util;

public class ClassUtil {
    public static final String DEFAULT_VERSION = "1.0.0";
    public static Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            return null;
        }
    }
}
