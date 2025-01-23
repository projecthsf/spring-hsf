package io.github.projecthsf.annotation;

import io.github.projecthsf.util.HsfUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HsfConsumer {
    String name() default "";
    String version() default HsfUtil.DEFAULT_VERSION;
}
