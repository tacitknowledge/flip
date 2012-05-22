/*
 * Copyright 2012 Tacit Knowledge.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tacitknowledge.flip.spring.config;

import java.util.Map;
import com.tacitknowledge.flip.spring.FlipSpringAspect;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.*;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class InterceptHandlerParserTest {
    
    private XmlBeanFactory context;
    
    @Before
    public void setUp() {
        context = new XmlBeanFactory(new ClassPathResource("test-intercept-handlers-context.xml"));
    }
    
    @Test
    public void testCreateFlipHandlerAspect() {
        assertTrue(context.containsBeanDefinition(FlipSpringAspect.ASPECT_BEAN_NAME));
        BeanDefinition flipHandlerAspectBeanDefinition = context.getBeanDefinition(FlipSpringAspect.ASPECT_BEAN_NAME);
        MutablePropertyValues properties = flipHandlerAspectBeanDefinition.getPropertyValues();
        assertEquals(FlipSpringAspect.class.getName(), flipHandlerAspectBeanDefinition.getBeanClassName());
        
        FlipSpringAspect aspect = context.getBean(FlipSpringAspect.ASPECT_BEAN_NAME, FlipSpringAspect.class);
        assertNotNull(aspect.getFeatureService());
        assertNotNull(aspect.getDefaultValue());
        assertEquals("test", aspect.getDefaultValue());
        
    }
    
}
