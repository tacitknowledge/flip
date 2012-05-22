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

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.FeatureServiceDirectFactory;
import com.tacitknowledge.flip.spring.FlipSpringAspect;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.Assert.*;
/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class AnnotatedFlipSpringConfigTest {
    
    private AnnotationConfigApplicationContext context;
    
    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext(TestCofig.class);
    }
    
    @Test
    public void testCreateFlipHandlerAspect() {
        assertTrue(context.containsBeanDefinition(FlipSpringAspect.ASPECT_BEAN_NAME));
        
        FlipSpringAspect aspect = context.getBean(FlipSpringAspect.ASPECT_BEAN_NAME, FlipSpringAspect.class);
        assertNotNull(aspect.getFeatureService());
        assertNotNull(aspect.getDefaultValue());
        assertEquals("test", aspect.getDefaultValue());
    }

    @Test
    public void testCreateFeatureService() {
        assertTrue(context.containsBeanDefinition(FlipSpringAspect.FEATURE_SERVICE_BEAN_NAME));
        assertNotNull(context.getBean(FlipSpringAspect.FEATURE_SERVICE_BEAN_NAME, FeatureService.class));
    }
    
    @Configuration
    @Import(AnnotatedFlipSpringConfig.class)
    public static class TestCofig {

        @Autowired
        public FeatureService featureService;
        
        @Bean
        public FlipSpringAspect flipSpringAspect() {
            return AnnotatedFlipSpringConfig.createFlipSpringAspect(featureService, "test");
        }
        
        @Bean
        public FeatureServiceDirectFactory getFeatureServiceFactory() {
            return new FeatureServiceDirectFactory();
        }
        
    }
    
}
