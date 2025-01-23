package io.github.projecthsf.autoconfig;

import io.github.projecthsf.contorller.HsfProviderController;
import io.github.projecthsf.service.HsfConsumerService;
import io.github.projecthsf.service.HsfProviderService;
import lombok.extern.slf4j.Slf4j;
import io.github.projecthsf.property.HsfConsumerProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(HsfConsumerProperty.class)
@Slf4j
@Import({
        HsfConsumerService.class,
        HsfAnnotationBeanPostProcessor.class,
        HsfProviderService.class,
        HsfProviderController.class,
})
public class HsfAutoConfigure {
}
