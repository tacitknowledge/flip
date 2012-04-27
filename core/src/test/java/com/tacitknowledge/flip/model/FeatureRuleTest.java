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

import com.tacitknowledge.flip.exceptions.UnknownContextPropertyException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class FeatureRuleTest extends AbstractFeatureProcessorTest{
    
    private FeatureRule rule;
    
    @Before
    public void setUp() {
        rule = new FeatureRule();
        rule.setState(FeatureState.ENABLED);
    }
    
    @Test
    public void testDefaultRule() {
        assertEquals(FeatureState.ENABLED, rule.process(contextManager));
    }
    
    @Test
    public void testWithConditions() {
        FeatureCondition condition = new FeatureCondition();
        condition.setContext("main");
        condition.setName("param");
        condition.setOperation(FeatureOperation.EQUALS);
        condition.setValue("PARAM VALUE");
        rule.setConditions(new FeatureCondition[]{condition});
        
        assertEquals(FeatureState.ENABLED, rule.process(contextManager));
    }
    
    @Test(expected=UnknownContextPropertyException.class)
    public void testWithConditionsFailed() {
        FeatureCondition condition = new FeatureCondition();
        condition.setContext("main");
        condition.setName("param1");
        condition.setOperation(FeatureOperation.EQUALS);
        condition.setValue("PARAM VALUE");
        rule.setConditions(new FeatureCondition[]{condition});
        
                
        when(contextMap.get(eq("param1"))).thenThrow(new UnknownContextPropertyException());
        
        rule.process(contextManager);
    }
    
    @Test
    public void testWithTwoConditions() {
        FeatureCondition condition = new FeatureCondition();
        condition.setContext("main");
        condition.setName("param");
        condition.setOperation(FeatureOperation.EQUALS);
        condition.setValue("PARAM VALUE");

        FeatureCondition condition1 = new FeatureCondition();
        condition1.setContext("main");
        condition1.setName("count");
        condition1.setOperation(FeatureOperation.LESS);
        condition1.setValue("11");
        
        rule.setConditions(new FeatureCondition[]{condition, condition1});
        
        assertEquals(FeatureState.ENABLED, rule.process(contextManager));
    }
    
    @Test
    public void testWithTwoConditionsOneFailed() {
        FeatureCondition condition = new FeatureCondition();
        condition.setContext("main");
        condition.setName("param");
        condition.setOperation(FeatureOperation.EQUALS);
        condition.setValue("PARAM VALUE1");

        FeatureCondition condition1 = new FeatureCondition();
        condition1.setContext("main");
        condition1.setName("count");
        condition1.setOperation(FeatureOperation.LESS);
        condition1.setValue("11");
        
        rule.setConditions(new FeatureCondition[]{condition, condition1});
        
        assertNull(rule.process(contextManager));
    }
}
