package io.github.projecthsf.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.github.projecthsf.binding.HsfRequestBody;
import io.github.projecthsf.util.HsfUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Service
@RequiredArgsConstructor
@Slf4j
public class HsfProviderService {
    private final ApplicationContext applicationContext;
    public Object processRequest(HsfRequestBody requestBody) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object provider = applicationContext.getBean(HsfUtil.getHsfProviderBeanName(requestBody));
        Method method = provider.getClass().getMethod(requestBody.getMethod(), requestBody.getParamTypes());
        return method.invoke(provider, requestBody.getParams());
    }
}