package io.github.projecthsf.autoconfig;

import io.github.projecthsf.annotation.HsfConsumer;
import io.github.projecthsf.binding.cusomer.HsfConsumerProxy;
import io.github.projecthsf.binding.provider.HsfProviderProperty;
import io.github.projecthsf.util.ClassUtil;
import io.github.projecthsf.util.HsfUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import io.github.projecthsf.annotation.HsfProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationNotAllowedException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class HsfAnnotationBeanPostProcessor implements BeanDefinitionRegistryPostProcessor, BeanFactoryAware, EnvironmentAware, ApplicationContextAware {
    private BeanFactory beanFactory;
    private ApplicationContext applicationContext;
    private DefaultListableBeanFactory registry;
    private Environment environment;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry = (DefaultListableBeanFactory) registry;

        List<String> packageNames = AutoConfigurationPackages.get(beanFactory);
        for (String packageName: packageNames) {
            log.info("* Package {}: start... ", packageName);
            log.info("* Package {} > HsfProvider start ...", packageName);
            scanHsfProvider(packageName);
            log.info("*Package {} > HsfProvider finished ...", packageName);
            log.info("*Package {} > HsfConsumer start ...", packageName);
            scanHsfConsumer(packageName);
            log.info("*Package {} > HsfConsumer finished ...", packageName);
            log.info("*Package {}: scan finished", packageName);
        }
    }

    private void scanHsfProvider(String packageName) {
        Set<BeanDefinition> beans = getBeanDefinitions(packageName, HsfProvider.class);
        HsfProviderProperty property = new HsfProviderProperty();
        property.setApis(new ArrayList<>());
        for (BeanDefinition bean: beans) {
            Class<?> clazz = ClassUtil.getClass(bean.getBeanClassName());
            if (clazz == null) {
                continue;
            }

            if (clazz.getInterfaces().length == 0) {
                throw new BeanCreationNotAllowedException(bean.getBeanClassName(), "MUST implementation of an interface to use @HsfProvider");
            }

            String beanName = HsfUtil.getHsfProviderBeanName(clazz.getInterfaces()[0], clazz.getAnnotation(HsfProvider.class));
            property.getApis().add(beanName);
            log.info("** Package: {} hsf provider registering bean {}", packageName, beanName);
            registry.registerBeanDefinition(beanName, bean);
        }

        registry.registerSingleton(HsfProviderProperty.class.getName(), property);
    }

    private void scanHsfConsumer(String packageName) {
        Set<BeanDefinition> beans = getBeanDefinitions(packageName, Configuration.class);
        // collect all fields has HSF annotation
        for (BeanDefinition bean: beans) {
            Class<?> clazz = ClassUtil.getClass(bean.getBeanClassName());
            if (clazz == null) {
                continue;
            }

            for (Field field: clazz.getDeclaredFields()) {
                HsfConsumer[] annotations = field.getAnnotationsByType(HsfConsumer.class);
                if (annotations.length == 0) {
                    continue;
                }

                // add to hsf consumer fields
                registerField(packageName, field, annotations[0]);
            }
        }
    }

    private void registerField(String packageName, Field field, HsfConsumer annotation) throws BeansException {
        String beanName = HsfUtil.getHsfCustomerBeanName(field.getType(), annotation);
        log.info("** Package: {} hsf consumer registering bean {} -> {}", packageName, beanName, field.getType().getName());

        Object proxy = Proxy.newProxyInstance(field.getType().getClassLoader(), new Class<?>[] {field.getType()}, new HsfConsumerProxy(applicationContext, annotation));
        registry.registerSingleton(beanName, proxy);

    }

    private Set<BeanDefinition> getBeanDefinitions(String packageName, Class<? extends Annotation> annotationType) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(annotationType));
        return provider.findCandidateComponents(packageName);
    }

    @Builder
    @Getter
    private static class HsfConsumerField {
        private Field field;
        private HsfConsumer annotation;
    }
}
