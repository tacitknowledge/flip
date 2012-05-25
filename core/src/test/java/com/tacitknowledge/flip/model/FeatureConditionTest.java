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
package com.tacitknowledge.flip.model;

import com.tacitknowledge.flip.context.ContextMap;
import com.tacitknowledge.flip.exceptions.UnknownContextPropertyException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class FeatureConditionTest extends AbstractFeatureProcessorTest {
    
    private FeatureCondition condition;
    
    @Before
    public void setUp() {
        condition = new FeatureCondition();
        condition.setContext("main");
    }
    
    @Test
    public void testTrue() {
        condition.setName("param");
        condition.setOperation(FeatureOperation.EQUALS);
        condition.setValue("PARAM VALUE");
        
        assertEquals(FeatureState.ENABLED, condition.process(contextManager));
    }
    
    @Test
    public void testFalse() {
        condition.setName("param");
        condition.setOperation(FeatureOperation.EQUALS);
        condition.setValue("PARAM VALUE1");
        
        assertEquals(FeatureState.DISABLED, condition.process(contextManager));
    }
    
    @Test
    public void testAllContexts() {
        condition.setContext(ContextMap.GLOBAL);
        condition.setName("param");
        condition.setOperation(FeatureOperation.EQUALS);
        condition.setValue("PARAM VALUE");
        
        assertEquals(FeatureState.ENABLED, condition.process(contextManager));
    }
    
    @Test
    public void testLessOperation() {
        condition.setContext(ContextMap.GLOBAL);
        condition.setName("count");
        condition.setOperation(FeatureOperation.LESS);
        condition.setValue("11");
        
        assertEquals(FeatureState.ENABLED, condition.process(contextManager));
    }
    
    @Test(expected=UnknownContextPropertyException.class)
    public void testUnknownProperty() {
        condition.setContext(ContextMap.GLOBAL);
        condition.setName("count1");
        condition.setOperation(FeatureOperation.LESS);
        condition.setValue("11");
        
        when(contextMap.get(eq("count1"))).thenThrow(new UnknownContextPropertyException());
        
        condition.process(contextManager);

    }
    
    @Test
    public void testNestedProperty() {
        condition.setContext(ContextMap.GLOBAL);
        condition.setName("someObject.nestedValue");
        condition.setOperation(FeatureOperation.EQUALS);
        condition.setValue("NestedValue");
        
        when(contextMap.get(eq("someObject"))).thenReturn(new SomeObject());
        
        assertEquals(FeatureState.ENABLED, condition.process(contextManager));
    }
    
    @Test
    public void testJexl() {
        condition.setExpression("count == 10");
        assertEquals(FeatureState.ENABLED, condition.process(contextManager));
    }
    
    @Test
    public void testJexlFailed() {
        condition.setExpression("count == 11");
        assertEquals(FeatureState.DISABLED, condition.process(contextManager));
    }
    
    @Test
    public void testJexlUsingContext() {
        condition.setContext("main");
        condition.setExpression("count == 10");
        assertEquals(FeatureState.ENABLED, condition.process(contextManager));
    }
    
    @Test
    public void testJexlUsingInvalidContext() {
        condition.setContext("up");
        condition.setExpression("count == 10");
        assertEquals(FeatureState.DISABLED, condition.process(contextManager));
    }
    
    @Test
    public void testJexlPriorityOverOldStyle() {
        condition.setContext(ContextMap.GLOBAL);
        condition.setName("param");
        condition.setOperation(FeatureOperation.EQUALS);
        condition.setValue("PARAM VALUE");
        condition.setExpression("count != 10");
        assertEquals(FeatureState.DISABLED, condition.process(contextManager));
    }
    
    public static class SomeObject {
        
        public String getNestedValue() {
            return "NestedValue";
        }
    }
    
}
