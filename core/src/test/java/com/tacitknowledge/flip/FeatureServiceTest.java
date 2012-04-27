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

import com.tacitknowledge.flip.exceptions.UnknownContextPropertyException;
import com.tacitknowledge.flip.fixtures.TestContextProvider;
import com.tacitknowledge.flip.model.FeatureDescriptor;
import com.tacitknowledge.flip.model.FeatureState;
import com.tacitknowledge.flip.properties.PropertyReader;
import com.tacitknowledge.flip.context.ContextManager;
import java.util.Collections;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class FeatureServiceTest {
    
    private FeatureService featureService;
    private PropertyReader propertyReader;
    private Object contextProvider;
    
    @Before
    public void setUp() {
        Environment environment = new Environment();
        
        propertyReader = mock(PropertyReader.class);
        contextProvider = new TestContextProvider();
        environment.setPropertyReaders(Collections.singletonList(propertyReader));
        environment.setContextProviders(Collections.singletonList(contextProvider));
        
        featureService = new FeatureServiceImpl(environment);
    }
    
    @Test
    public void testInvokeFeature() {
        FeatureDescriptor featureDescriptor = mock(FeatureDescriptor.class);
        when(featureDescriptor.process(any(ContextManager.class))).thenReturn(FeatureState.ENABLED);
        when(propertyReader.getFeatureDescriptor(eq("test"))).thenReturn(featureDescriptor);
        
        assertEquals(FeatureState.ENABLED, featureService.getFeatureState("test"));
        
        verify(propertyReader).getFeatureDescriptor(eq("test"));
    }
    
    @Test
    public void testInvokeFeatureDisabled() {
        FeatureDescriptor featureDescriptor = mock(FeatureDescriptor.class);
        when(featureDescriptor.process(any(ContextManager.class))).thenReturn(FeatureState.DISABLED);
        when(propertyReader.getFeatureDescriptor(eq("test"))).thenReturn(featureDescriptor);
        
        assertEquals(FeatureState.DISABLED, featureService.getFeatureState("test"));
        
        verify(propertyReader).getFeatureDescriptor(eq("test"));
    }
    
    @Test
    public void testInvokeFeatureUnknownProperty() {
        FeatureDescriptor featureDescriptor = mock(FeatureDescriptor.class);
        when(featureDescriptor.process(any(ContextManager.class))).thenThrow(new UnknownContextPropertyException());
        when(propertyReader.getFeatureDescriptor(eq("test"))).thenReturn(featureDescriptor);
        
        assertEquals(FeatureState.DISABLED, featureService.getFeatureState("test"));
    }
    
    @Test
    public void testHieraclicalFeaturesChildFailed() {
        FeatureDescriptor item = mock(FeatureDescriptor.class);
        when(item.process(any(ContextManager.class))).thenThrow(new UnknownContextPropertyException());
        when(propertyReader.getFeatureDescriptor(eq("test.item"))).thenReturn(item);

        FeatureDescriptor parent = mock(FeatureDescriptor.class);
        when(parent.process(any(ContextManager.class))).thenReturn(FeatureState.ENABLED);
        when(propertyReader.getFeatureDescriptor(eq("test"))).thenReturn(parent);
        
        assertEquals(FeatureState.DISABLED, featureService.getFeatureState("test.item"));
    }
    
    @Test
    public void testHieraclicalFeaturesChildAndParentHaveTheSameStatus() {
        FeatureDescriptor item = mock(FeatureDescriptor.class);
        when(item.process(any(ContextManager.class))).thenReturn(FeatureState.ENABLED);
        when(propertyReader.getFeatureDescriptor(eq("test.item"))).thenReturn(item);

        FeatureDescriptor parent = mock(FeatureDescriptor.class);
        when(parent.process(any(ContextManager.class))).thenReturn(FeatureState.ENABLED);
        when(propertyReader.getFeatureDescriptor(eq("test"))).thenReturn(parent);
        
        assertEquals(FeatureState.ENABLED, featureService.getFeatureState("test.item"));
    }
    
    @Test
    public void testHieraclicalFeaturesChildAndParentHaveDifferentStatuses() {
        FeatureDescriptor item = mock(FeatureDescriptor.class);
        when(item.process(any(ContextManager.class))).thenReturn(FeatureState.ENABLED);
        when(propertyReader.getFeatureDescriptor(eq("test.item"))).thenReturn(item);

        FeatureDescriptor parent = mock(FeatureDescriptor.class);
        when(parent.process(any(ContextManager.class))).thenReturn(FeatureState.DISABLED);
        when(propertyReader.getFeatureDescriptor(eq("test"))).thenReturn(parent);
        
        assertEquals(FeatureState.DISABLED, featureService.getFeatureState("test.item"));
    }
    
    @Test
    public void testHieraclicalFeaturesChildAndParentHaveDifferentStatuses2() {
        FeatureDescriptor item = mock(FeatureDescriptor.class);
        when(item.process(any(ContextManager.class))).thenReturn(FeatureState.DISABLED);
        when(propertyReader.getFeatureDescriptor(eq("test.item"))).thenReturn(item);

        FeatureDescriptor parent = mock(FeatureDescriptor.class);
        when(parent.process(any(ContextManager.class))).thenReturn(FeatureState.ENABLED);
        when(propertyReader.getFeatureDescriptor(eq("test"))).thenReturn(parent);
        
        assertEquals(FeatureState.DISABLED, featureService.getFeatureState("test.item"));
    }

}
