package io.github.projecthsf.autoconfig;

import io.github.easyannotation.annotation.Processor;
import io.github.easyannotation.binding.AnnotationFieldProperty;
import io.github.easyannotation.processor.AbstractAnnotationProcessor;
import io.github.projecthsf.annotation.HsfConsumer;
import io.github.projecthsf.binding.cusomer.HsfConsumerProxy;
import io.github.projecthsf.binding.provider.HsfProviderProperty;
import io.github.projecthsf.util.HsfUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;

@Processor
@Slf4j
public class HsfAnnotationProcessor extends AbstractAnnotationProcessor {
    private HsfProviderProperty hsfProviderProperty = new HsfProviderProperty();

    @Override
    public <A extends Annotation> void processClass(BeanDefinition beanDefinition, Class<?> clazz, A annotation) {
        String beanName = HsfUtil.getHsfProviderBeanName(clazz.getInterfaces()[0], annotation);
        registry.registerBeanDefinition(beanName, beanDefinition);
        hsfProviderProperty.getApis().add(beanName);
    }

    @Override
    public <A extends Annotation> void processField(Field field, A annotation) {
        String beanName = HsfUtil.getHsfCustomerBeanName(field.getType(), annotation);
        Object proxy = Proxy.newProxyInstance(field.getType().getClassLoader(), new Class<?>[] {field.getType()}, new HsfConsumerProxy(applicationContext, (HsfConsumer) annotation));
        registry.registerSingleton(beanName, proxy);
    }

    @Override
    public void afterAllProcesses() {
        registry.registerSingleton(HsfProviderProperty.class.getName(), hsfProviderProperty);
    }

    @Override
    public void processorInit() {
        hsfProviderProperty = new HsfProviderProperty();
        hsfProviderProperty.setApis(new ArrayList<>());
    }

    @Override
    public AnnotationFieldProperty getAnnotationFieldProperty() {
        AnnotationFieldProperty property = new AnnotationFieldProperty();
        property.setField(HsfConsumer.class);
        property.setDependOn(Collections.singletonList(Configuration.class));

        return property;
    }
}
