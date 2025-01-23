package io.github.projecthsf.util;

import io.github.projecthsf.service.HsfConsumerService;
import io.github.projecthsf.util.ClassUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassUtilTest {

    @Test
    void testGetClass() {
        Class<?> clazz = ClassUtil.getClass(HsfConsumerService.class.getName());
        assertEquals(clazz, HsfConsumerService.class);
    }

    @Test
    void testGetClass_NonExistedClass() {
        Class<?> clazz = ClassUtil.getClass("non_existed_class");
        assertNull(clazz);
    }
}