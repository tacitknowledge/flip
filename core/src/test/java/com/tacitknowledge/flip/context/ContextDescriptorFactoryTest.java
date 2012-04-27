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
package com.tacitknowledge.flip.context;

import com.tacitknowledge.flip.context.ContextDescriptorFactory;
import com.tacitknowledge.flip.context.ContextDescriptor;
import com.tacitknowledge.flip.fixtures.TestContextProvider;
import java.lang.reflect.Method;
import org.hamcrest.Matchers;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class ContextDescriptorFactoryTest {

    private ContextDescriptorFactory contextDescriptorFactory;
    
    @Before
    public void setUp() {
        contextDescriptorFactory = new ContextDescriptorFactory();
    }
    
    @Test
    public void testGetDescriptorForAnnotatedClass() {
        TestContextProvider contextProvider = new TestContextProvider();
        ContextDescriptor contextDescriptor = contextDescriptorFactory.createContextDescriptor(contextProvider);
        
        assertEquals("test", contextDescriptor.getName());
        assertEquals(contextProvider, contextDescriptor.getContext());
    }
    
    @Test
    public void testFailWhenContextProviderHasNotAnnotation() {
        assertNull(contextDescriptorFactory.createContextDescriptor(new Integer("1")));
    }

    @Test
    public void testGetSimpleMethodAsProperty() {
        TestContextProvider contextProvider = new TestContextProvider();
        ContextDescriptor contextDescriptor = contextDescriptorFactory.createContextDescriptor(contextProvider);
        
        assertNotNull(contextDescriptor.getProperties().get("hello"));
        assertEquals("hello", contextDescriptor.getProperties().get("hello").getName());
    }
    
    @Test
    public void testObtainNullForVoidMethod() {
        TestContextProvider contextProvider = new TestContextProvider();
        ContextDescriptor contextDescriptor = contextDescriptorFactory.createContextDescriptor(contextProvider);
        
        assertNull(contextDescriptor.getProperties().get("ignoreVoidMethod"));
    }
    
    @Test
    public void testObtainNullForMethodWithParams() {
        TestContextProvider contextProvider = new TestContextProvider();
        ContextDescriptor contextDescriptor = contextDescriptorFactory.createContextDescriptor(contextProvider);
        
        assertNull(contextDescriptor.getProperties().get("methodWithParams"));
    }
    
    @Test
    public void testGetMethodThatIsAnnotatedAsContextProperty() {
        TestContextProvider contextProvider = new TestContextProvider();
        ContextDescriptor contextDescriptor = contextDescriptorFactory.createContextDescriptor(contextProvider);
        
        assertNotNull(contextDescriptor.getProperties().get("intKey"));
        assertEquals("getIntegerValue", contextDescriptor.getProperties().get("intKey").getName());
    }

    @Test
    public void testGetMethodThatIsGetter() {
        TestContextProvider contextProvider = new TestContextProvider();
        ContextDescriptor contextDescriptor = contextDescriptorFactory.createContextDescriptor(contextProvider);
        
        assertNotNull(contextDescriptor.getProperties().get("value"));
        assertEquals("getValue", contextDescriptor.getProperties().get("value").getName());
    }
    
    @Test
    public void testGetMethodThatIsBooleanGetter() {
        TestContextProvider contextProvider = new TestContextProvider();
        ContextDescriptor contextDescriptor = contextDescriptorFactory.createContextDescriptor(contextProvider);
        
        assertNotNull(contextDescriptor.getProperties().get("booleanValue"));
        assertEquals("isBooleanValue", contextDescriptor.getProperties().get("booleanValue").getName());
        assertEquals(Boolean.TYPE, contextDescriptor.getProperties().get("booleanValue").getReturnType());
    }
    
    @Test
    public void testGetMethodThatIsBooleanObjectGetter() {
        TestContextProvider contextProvider = new TestContextProvider();
        ContextDescriptor contextDescriptor = contextDescriptorFactory.createContextDescriptor(contextProvider);
        
        assertNotNull(contextDescriptor.getProperties().get("booleanObjectValue"));
        assertEquals("isBooleanObjectValue", contextDescriptor.getProperties().get("booleanObjectValue").getName());
        assertEquals(Boolean.class, contextDescriptor.getProperties().get("booleanObjectValue").getReturnType());
    }
    
    @Test
    public void testGetIsMethodThatIsNotBooleanGetter() {
        TestContextProvider contextProvider = new TestContextProvider();
        ContextDescriptor contextDescriptor = contextDescriptorFactory.createContextDescriptor(contextProvider);
        
        assertNull(contextDescriptor.getProperties().get("objectValueNonBoolean"));
    }
    
    @Test
    public void testGetAllValuesAsMapMethodThatIsAnonymousAnnotatedWithContextProperty() throws Exception {
        TestContextProvider contextProvider = new TestContextProvider();
        ContextDescriptor contextDescriptor = contextDescriptorFactory.createContextDescriptor(contextProvider);
        
        Method method = contextProvider.getClass().getMethod("getAllValuesAsMap");
        assertThat(contextDescriptor.getAnonymousProperties(), Matchers.hasItem(method));
    }
    
    @Test
    public void testAnonymousAnnotatedMethodShouldBeMap() throws Exception {
        TestContextProvider contextProvider = new TestContextProvider();
        ContextDescriptor contextDescriptor = contextDescriptorFactory.createContextDescriptor(contextProvider);
        
        Method method = contextProvider.getClass().getMethod("getAnonMethod");
        assertThat(contextDescriptor.getAnonymousProperties(), Matchers.not(Matchers.hasItem(method)));
        assertThat(contextDescriptor.getProperties().values(), Matchers.not(Matchers.hasItem(method)));
    }
}
