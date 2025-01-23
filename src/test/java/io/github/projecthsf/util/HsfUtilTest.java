package io.github.projecthsf.util;

import io.github.projecthsf.annotation.HsfConsumer;
import io.github.projecthsf.annotation.HsfProvider;
import io.github.projecthsf.binding.HsfRequestBody;
import io.github.projecthsf.service.HsfConsumerService;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;

class HsfUtilTest {

    @Test
    void getHsfProviderBeanName() {
        HsfRequestBody body = new HsfRequestBody();
        body.setClassInterface("InterfaceName");
        body.setVersion("1.0.0");
        String providerName = HsfUtil.getHsfProviderBeanName(body);

        assertEquals("InterfaceNameProvider:1.0.0", providerName);
    }

    @Test
    void testGetHsfProviderBeanName() {

        String providerName = HsfUtil.getHsfProviderBeanName(HsfConsumerService.class, new HsfProvider() {
            @Override
            public String version() {
                return "1.0.0";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });

        assertEquals("io.github.projecthsf.service.HsfConsumerServiceProvider:1.0.0", providerName);
    }

    @Test
    void getHsfCustomerBeanName() {
        String consumerName = HsfUtil.getHsfCustomerBeanName(HsfConsumerService.class, new HsfConsumer() {
            @Override
            public String name() {
                return "consumerName";
            }

            @Override
            public String version() {
                return "1.0.0";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });

        assertEquals("consumerName", consumerName);
    }

    @Test
    void getHsfCustomerBeanName_WithDefaultBeanName() {
        String consumerName = HsfUtil.getHsfCustomerBeanName(HsfConsumerService.class, new HsfConsumer() {
            @Override
            public String name() {
                return "";
            }

            @Override
            public String version() {
                return "1.0.0";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });

        assertEquals("io.github.projecthsf.service.HsfConsumerServiceConsumer:1.0.0", consumerName);
    }
}