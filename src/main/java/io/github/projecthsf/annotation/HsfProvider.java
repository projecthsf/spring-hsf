package io.github.projecthsf.annotation;

import io.github.easyannotation.annotation.AnnotationScan;
import io.github.projecthsf.autoconfig.HsfAnnotationProcessor;
import io.github.projecthsf.util.HsfUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AnnotationScan(processor = HsfAnnotationProcessor.class)
public @interface HsfProvider {
    String version() default HsfUtil.DEFAULT_VERSION;
}
