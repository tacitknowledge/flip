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

import com.tacitknowledge.flip.context.ContextManager;
import com.tacitknowledge.flip.context.ContextMap;
import com.tacitknowledge.flip.Environment;
import com.tacitknowledge.flip.exceptions.UnknownContextException;
import com.tacitknowledge.flip.exceptions.UnknownContextPropertyException;
import com.tacitknowledge.flip.fixtures.TestContextProvider;
import com.tacitknowledge.flip.otherfixtures.Test1ContextProvider;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class ContextManagerTest {
    
    private ContextManager contextManager;
    private Environment environment;
    
    @Before
    public void setUp() {
        environment = new Environment();
        environment.getContextProviders().add(new TestContextProvider());
        contextManager = new ContextManager(environment);
    }
    
    @Test
    public void testGetValueBySimpleMethodName() {
        assertEquals("world", contextManager.getContext("test").get("hello"));
    }
    
    @Test
    public void testGetValueBySimpleMethodNameInGlobalContext() {
        assertEquals("world", contextManager.getContext(ContextMap.GLOBAL).get("hello"));
    }
    
    @Test(expected=UnknownContextException.class)
    public void testGetValueFromInvalidContext() {
        contextManager.getContext("abrakadabra").get("hello");
    }
    
    @Test(expected = UnknownContextPropertyException.class)
    public void testFailGetValueByMissingMethodName() {
        contextManager.getContext("test").get("missingHello");
    }
    
    @Test
    public void testGetValueFromBooleanMethod() {
        assertEquals(false, contextManager.getContext(ContextMap.GLOBAL).get("booleanValue"));
    }
    
    @Test
    public void testGetValueFromAnnotatedMethod() {
        assertEquals(10, contextManager.getContext(ContextMap.GLOBAL).get("intKey"));
    }
    
    @Test
    public void testGetValueAnonymousMap() {
        assertEquals("value", contextManager.getContext(ContextMap.GLOBAL).get("key"));
    }
    
    @Test
    public void testGetValueByPriorityFromGlobalContext() {
        environment.getContextProviders().add(new Test1ContextProvider());
        contextManager = new ContextManager(environment);
        
        assertEquals("someValue", contextManager.getContext(ContextMap.GLOBAL).get("value"));
    }
    
    @Test
    public void testGetValueByPriorityFromGlobalContextAndHasOnlyInSecondContext() {
        environment.getContextProviders().add(new Test1ContextProvider());
        contextManager = new ContextManager(environment);
        
        assertEquals("second-value", contextManager.getContext(ContextMap.GLOBAL).get("secondValue"));
    }
    
    @Test(expected=UnknownContextPropertyException.class)
    public void testGetValueByPriorityFromGlobalContextAndThereIsNoSuchField() {
        environment.getContextProviders().add(new Test1ContextProvider());
        contextManager = new ContextManager(environment);
        
        contextManager.getContext(ContextMap.GLOBAL).get("secondValue1");
    }
    
}
