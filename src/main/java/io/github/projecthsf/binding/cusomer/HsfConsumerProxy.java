package io.github.projecthsf.binding.cusomer;

import io.github.projecthsf.annotation.HsfConsumer;
import io.github.projecthsf.service.HsfConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
@Slf4j
public class HsfConsumerProxy implements InvocationHandler {
    private ApplicationContext applicationContext;
    private HsfConsumer annotation;
    public HsfConsumerProxy(ApplicationContext applicationContext, HsfConsumer annotation) {
        this.applicationContext = applicationContext;
        this.annotation = annotation;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("equals".equals(method.getName())) {
            return false;
        }

        HsfConsumerService service = applicationContext.getBean(HsfConsumerService.class);
        return service.invokeProvider(proxy, method, args, annotation);
    }
}
