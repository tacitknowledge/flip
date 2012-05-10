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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.FeatureServiceDirectFactory;
import com.tacitknowledge.flip.context.SystemPropertiesContextProvider;
import com.tacitknowledge.flip.properties.XmlPropertyReader;

/**
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class FeatureServiceHandlerParserTest {
    
    private XmlBeanFactory context;
    
    
    @Before
    public void setUp() {
        setupContext("test-feature-service-context.xml");
    }
    
    @Test
    public void testCreateFeatureServiceFactory() {
        assertNotNull(context.containsBeanDefinition("featureServiceFactory"));
        assertEquals(FeatureServiceDirectFactory.class.getName(), context.getBeanDefinition("featureServiceFactory").getBeanClassName());
    }
    
    @Test
    public void testFeatureServiceFactoryProperties() {
        BeanDefinition beanDefinition = context.getBeanDefinition("featureServiceFactory");

        // Test ContextProviders setup
        PropertyValue contextProvidersProperty = beanDefinition.getPropertyValues().getPropertyValue("contextProviders");
        assertNotNull(contextProvidersProperty);
        assertTrue(contextProvidersProperty.getValue() instanceof List);
        List contextProvidersList = ((List)contextProvidersProperty.getValue());
        assertEquals(2, contextProvidersList.size());
        
        assertTrue(contextProvidersList.get(0) instanceof BeanDefinitionHolder);
        assertEquals(SystemPropertiesContextProvider.class.getName(), ((BeanDefinitionHolder)contextProvidersList.get(0)).getBeanDefinition().getBeanClassName());
        
        assertTrue(contextProvidersList.get(1) instanceof BeanReference);
        assertEquals("contextProviderBean", ((BeanReference)contextProvidersList.get(1)).getBeanName());

        // Test PropertyReaders setup
        PropertyValue propertyReadersProperty = beanDefinition.getPropertyValues().getPropertyValue("propertyReaders");
        assertNotNull(propertyReadersProperty);
        assertTrue(propertyReadersProperty.getValue() instanceof List);
        List propertyReadersList = ((List)propertyReadersProperty.getValue());
        assertEquals(2, propertyReadersList.size());
        
        assertTrue(propertyReadersList.get(0) instanceof BeanDefinitionHolder);
        assertEquals(XmlPropertyReader.class.getName(), ((BeanDefinitionHolder)propertyReadersList.get(0)).getBeanDefinition().getBeanClassName());
        
        assertTrue(propertyReadersList.get(1) instanceof BeanReference);
        assertEquals("propertyReaderBean", ((BeanReference)propertyReadersList.get(1)).getBeanName());
        
        // Test properties setup
        PropertyValue propertiesProperty = beanDefinition.getPropertyValues().getPropertyValue("properties");
        assertNotNull(propertiesProperty);
        assertTrue(propertiesProperty.getValue() instanceof Properties);
        Properties properties = ((Properties)propertiesProperty.getValue());
        assertEquals(1, properties.size());
        
        for(Map.Entry entry : properties.entrySet()) {
            assertEquals("aa", ((TypedStringValue)entry.getKey()).getValue());
            assertEquals("bb", ((TypedStringValue)entry.getValue()).getValue());
        }
    }
    
    @Test
    public void testFeatureServiceFactoryPropertiesMising() {
        setupContext("test-feature-service-context-empty.xml");
        BeanDefinition beanDefinition = context.getBeanDefinition("featureServiceFactory");

        // Test ContextProviders setup
        PropertyValue contextProvidersProperty = beanDefinition.getPropertyValues().getPropertyValue("contextProviders");
        assertNull(contextProvidersProperty);

        // Test PropertyReaders setup
        PropertyValue propertyReadersProperty = beanDefinition.getPropertyValues().getPropertyValue("propertyReaders");
        assertNull(propertyReadersProperty);
        
        // Test properties setup
        PropertyValue propertiesProperty = beanDefinition.getPropertyValues().getPropertyValue("properties");
        assertNull(propertiesProperty);
    }
    
    @Test
    public void testFeatureServiceFactoryPropertiesWithPropChildren() {
        setupContext("test-feature-service-context-no-prop-children.xml");
        BeanDefinition beanDefinition = context.getBeanDefinition("featureServiceFactory");

        // Test ContextProviders setup
        PropertyValue contextProvidersProperty = beanDefinition.getPropertyValues().getPropertyValue("contextProviders");
        assertNull(contextProvidersProperty);

        // Test PropertyReaders setup
        PropertyValue propertyReadersProperty = beanDefinition.getPropertyValues().getPropertyValue("propertyReaders");
        assertNull(propertyReadersProperty);
        
        // Test properties setup
        PropertyValue propertiesProperty = beanDefinition.getPropertyValues().getPropertyValue("properties");
        assertNull(propertiesProperty);
    }
    
    @Test
    public void testFeatureServiceFactoryPropertiesWithEnvironment() {
        setupContext("test-feature-service-context-with-env.xml");
        BeanDefinition beanDefinition = context.getBeanDefinition("featureServiceFactory");

        // Test ContextProviders setup
        PropertyValue environmentProperty = beanDefinition.getPropertyValues().getPropertyValue("environment");
        assertNotNull(environmentProperty);
        
        assertTrue(environmentProperty.getValue() instanceof BeanReference);
        assertEquals("environmentBean", ((BeanReference)environmentProperty.getValue()).getBeanName());
    }
    
    
    @Test
    public void testCreateFeatureServiceBean() {
        assertNotNull(context.containsBeanDefinition("featureService"));
        BeanDefinition beanDefinition = context.getBeanDefinition("featureService");
        assertEquals("featureServiceFactory", beanDefinition.getFactoryBeanName());
        assertEquals("createFeatureService", beanDefinition.getFactoryMethodName());
        
        FeatureService featureService = context.getBean("featureService", FeatureService.class);
        assertNotNull(featureService);
    }
    
    
    private void setupContext(String contextLocation) {
        context = new XmlBeanFactory(new ClassPathResource(contextLocation));
    }
}
