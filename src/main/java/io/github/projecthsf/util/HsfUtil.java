package io.github.projecthsf.util;

import io.github.projecthsf.annotation.HsfConsumer;
import io.github.projecthsf.annotation.HsfProvider;
import io.github.projecthsf.binding.HsfRequestBody;

import java.lang.annotation.Annotation;

public class HsfUtil {
    public static final String DEFAULT_VERSION = "1.0.0";
    public static String getHsfProviderBeanName(HsfRequestBody request) {
        return request.getClassInterface() + "Provider:" + request.getVersion();
    }

    public static String getHsfProviderBeanName(Class<?> clazz, Annotation annotation) {
        return clazz.getName() + "Provider:" + ((HsfProvider) annotation).version();
    }

    public static String getHsfCustomerBeanName(Class<?> clazz, Annotation hsfConsumer) {
        HsfConsumer annotation = (HsfConsumer) hsfConsumer;
        return annotation.name().isEmpty() ? clazz.getName() + "Consumer:" + annotation.version() : annotation.name();
    }
}
