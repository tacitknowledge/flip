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

package com.tacitknowledge.flip.servlet.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import com.tacitknowledge.flip.model.FeatureDescriptor;
import com.tacitknowledge.flip.model.FeatureState;
import com.tacitknowledge.flip.properties.FeatureDescriptorsMap;
import com.tacitknowledge.flip.servlet.FlipFilter;
import com.tacitknowledge.flip.servlet.FlipWebContext;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class SessionPropertyReaderTest {
    
    private SessionPropertyReader reader;
    private HttpSession session;
    private FeatureDescriptorsMap sessionFeaturesMap;
    
    @Before
    public void setUp() {
        reader = new SessionPropertyReader();
        session = mock(HttpSession.class);
        sessionFeaturesMap = new FeatureDescriptorsMap();
        when(session.getAttribute(eq(FlipFilter.SESSION_FEATURES_KEY))).thenReturn(sessionFeaturesMap);
        
        FlipWebContext.setFeatureDescriptors(sessionFeaturesMap);
    }
    
    @Test
    public void testGetFeatureState() {
        
        sessionFeaturesMap.put("featureName", new FeatureDescriptor() {{
            setName("fetureName");
            setState(FeatureState.DISABLED);
        }});
        
        FeatureDescriptor featureDescriptor = reader.getFeatureDescriptor("featureName");
        assertEquals(FeatureState.DISABLED, featureDescriptor.getState());
    }
    
    @Test
    public void testGetMissingFeatureState() {        
        assertNull(reader.getFeatureDescriptor("featureName"));
    }

    @Test
    public void testGetFeatureStateWhenFeatureMapIsMissing() {
        when(session.getAttribute(eq(FlipFilter.SESSION_FEATURES_KEY))).thenReturn(null);
        assertNull(reader.getFeatureDescriptor("featureName"));
    }

    @Test
    public void testGetFeatureStateWhenSessionContainsNonMapEntity() {
        when(session.getAttribute(eq(FlipFilter.SESSION_FEATURES_KEY))).thenReturn(new Double(3.14));
        assertNull(reader.getFeatureDescriptor("featureName"));
    }
    
}
