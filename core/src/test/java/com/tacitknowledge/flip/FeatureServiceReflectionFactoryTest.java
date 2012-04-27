/* Copyright 2012 Tacit Knowledge
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.tacitknowledge.flip;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tacitknowledge.flip.context.SystemPropertiesContextProvider;
import com.tacitknowledge.flip.fixtures.TestContextProvider;
import com.tacitknowledge.flip.fixtures.TestPropertyReader;
import com.tacitknowledge.flip.otherfixtures.Test1ContextProvider;
import com.tacitknowledge.flip.properties.PropertyReader;
import com.tacitknowledge.flip.properties.XmlPropertyReader;
import java.util.List;
import java.util.Properties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class FeatureServiceReflectionFactoryTest {
    
    private FeatureServiceReflectionFactory factory;
    
    @Before 
    public void setUp() {
        factory = new FeatureServiceReflectionFactory();
    }
    
    @Test
    public void testCreateFeatureServiceWithDefault() {
        FeatureService featureService = factory.createFeatureService();
        assertNotNull(featureService);
    }
    
    @Test
    public void testGenEnvironmentServiceFilledDefault() {
        Environment environment = factory.getEnvironment(null, (String[]) null);
        
        assertNotNull(environment);
        
        Assert.assertTrue(environment.getProperties().isEmpty());
        assertTrue(isListHasItemOfType(environment.getPropertyReaders(), TestPropertyReader.class));
        assertTrue(isListHasItemOfType(environment.getPropertyReaders(), XmlPropertyReader.class));
        
        assertTrue(isListHasItemOfType(environment.getContextProviders(), TestContextProvider.class));
        assertTrue(isListHasItemOfType(environment.getContextProviders(), SystemPropertiesContextProvider.class));
        
        assertFalse(environment.getContextProviders().isEmpty());
    }
    
    @Test
    public void testGenEnvironmentServiceFilledWithPackages() {
        Environment environment = factory.getEnvironment(null, "com.tacitknowledge.flip.fixtures");
        
        assertNotNull(environment);
        
        Assert.assertTrue(environment.getProperties().isEmpty());
        assertTrue(isListHasItemOfType(environment.getPropertyReaders(), TestPropertyReader.class));
        assertTrue(isListHasItemOfType(environment.getContextProviders(), TestContextProvider.class));
        assertTrue(isListHasItemOfType(environment.getContextProviders(), Test1ContextProvider.class));
    }
    
    @Test
    public void testGenEnvironmentServiceFilledWithMultiplePackages() {
        Environment environment = factory.getEnvironment(null, "com.tacitknowledge.flip.fixtures", "com.tacitknowledge.flip.otherfixtures");
        
        assertNotNull(environment);
        
        Assert.assertTrue(environment.getProperties().isEmpty());
        assertTrue(isListHasItemOfType(environment.getPropertyReaders(), TestPropertyReader.class));
        assertTrue(isListHasItemOfType(environment.getContextProviders(), TestContextProvider.class));
        assertTrue(isListHasItemOfType(environment.getContextProviders(), Test1ContextProvider.class));
    }
    
    @Test
    public void testGenEnvironmentServiceFilledWithProperties() {
        Properties properties = new Properties();
        properties.setProperty("key", "value");
        Environment environment = factory.getEnvironment(properties, (String[]) null);
        
        assertNotNull(environment);
        assertTrue(environment.getProperties().containsKey("key"));
        assertEquals("value", environment.getProperties().getProperty("key"));
    }
    
    @Test
    public void testCreateFeatureCallsGetEnvironment() {
        factory = spy(factory);
        when(factory.getEnvironment(any(Properties.class), any(String[].class))).thenReturn(new Environment());
        Properties properties = new Properties();
        factory.createFeatureService(properties, "package1", "package2");
        
        verify(factory).getEnvironment(eq(properties), eq("package1"), eq("package2"));
    }
    
    @Test
    public void testInitPropertyReaders() {
        factory = spy(factory);
        Environment environment = new Environment();
        Properties properties = mock(Properties.class);
        environment.setProperties(properties);
        PropertyReader propertyReader = mock(PropertyReader.class);
        environment.getPropertyReaders().add(propertyReader);
        
        when(factory.getEnvironment(any(Properties.class), any(String[].class))).thenReturn(environment);
        factory.createFeatureService();
        
        verify(propertyReader).initialize(eq(properties));
    }
    
    private boolean isListHasItemOfType(List list, Class klass) {
        for(Object item : list) {
            if (klass.isAssignableFrom(item.getClass())) {
                return true;
            }
        }
        return false;
    }
    
    @Test
    public void testGenEnvironmentServiceFilledDefaultAccordingToPriority() {
        Environment environment = factory.getEnvironment(null, (String[]) null);

        final Function<Object, Class> transformerFunction = new Function<Object, Class>() {

              @Override
              public Class apply(Object input) {
                  return input.getClass();
              }
          
          };
        
        List<Class> propertyReadersAsClass = Lists.transform(environment.getPropertyReaders(), transformerFunction);
        List<Class> contextProvidersAsClass = Lists.transform(environment.getContextProviders(), transformerFunction);
                
        assertTrue(propertyReadersAsClass.indexOf(TestPropertyReader.class) < propertyReadersAsClass.indexOf(XmlPropertyReader.class));
        
        assertTrue(contextProvidersAsClass.indexOf(TestContextProvider.class) < contextProvidersAsClass.indexOf(SystemPropertiesContextProvider.class));
        assertTrue(contextProvidersAsClass.indexOf(SystemPropertiesContextProvider.class) < contextProvidersAsClass.indexOf(Test1ContextProvider.class));
    }
}
