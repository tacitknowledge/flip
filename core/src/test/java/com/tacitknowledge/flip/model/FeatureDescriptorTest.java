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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class FeatureDescriptorTest extends AbstractFeatureProcessorTest {
    
    private FeatureDescriptor descriptor;
    
    @Before
    public void setUp() {
        descriptor = new FeatureDescriptor();
        descriptor.setName("test");
    }
    
    @Test
    public void testOverridingState() {
        descriptor.setState(FeatureState.ENABLED);
        
        FeatureState state = descriptor.process(contextManager);
        assertEquals(FeatureState.ENABLED, state);
    }
    
    @Test
    public void testOverridingStateDisabled() {
        descriptor.setState(FeatureState.DISABLED);
        
        FeatureState state = descriptor.process(contextManager);
        assertEquals(FeatureState.DISABLED, state);
    }
    
    @Test
    public void testUndefinedFeatureState() {
        assertNull(descriptor.process(contextManager));
    }
    
    @Test
    public void testDefaultRule() {
        FeatureRule rule = new FeatureRule();
        rule.setState(FeatureState.ENABLED);
        descriptor.setRules(new FeatureRule[]{rule});
        
        FeatureState state = descriptor.process(contextManager);
        
        assertEquals(FeatureState.ENABLED, state);
    }
    
    @Test
    public void testOverrideRules() {
        FeatureRule rule = new FeatureRule();
        rule.setState(FeatureState.ENABLED);
        descriptor.setRules(new FeatureRule[]{rule});
        descriptor.setState(FeatureState.DISABLED);
        
        FeatureState state = descriptor.process(contextManager);
        
        assertEquals(FeatureState.DISABLED, state);
    }
    
    @Test
    public void testTwoRulesAndMatchesFirst() {
        FeatureRule rule = new FeatureRule();
        rule.setState(FeatureState.DISABLED);

        FeatureRule rule1 = new FeatureRule();
        rule1.setState(FeatureState.ENABLED);
        
        descriptor.setRules(new FeatureRule[]{rule, rule1});
        
        FeatureState state = descriptor.process(contextManager);
        
        assertEquals(FeatureState.DISABLED, state);
    }
    
    @Test
    public void testTwoRulesAndMatchesSecond() {
        FeatureRule rule = new FeatureRule();
        rule.setState(FeatureState.DISABLED);
        FeatureCondition condition = new FeatureCondition();
        condition.setContext(ContextMap.GLOBAL);
        condition.setName("param");
        condition.setOperation(FeatureOperation.EQUALS);
        condition.setValue("AAA");
        rule.setConditions(new FeatureCondition[]{condition});

        FeatureRule rule1 = new FeatureRule();
        rule1.setState(FeatureState.ENABLED);
        
        descriptor.setRules(new FeatureRule[]{rule, rule1});
        
        FeatureState state = descriptor.process(contextManager);
        
        assertEquals(FeatureState.ENABLED, state);
    }
    
}
