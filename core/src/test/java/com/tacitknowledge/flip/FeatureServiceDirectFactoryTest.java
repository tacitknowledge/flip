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

import com.tacitknowledge.flip.properties.PropertyReader;
import java.util.Properties;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class FeatureServiceDirectFactoryTest {

    private FeatureServiceDirectFactory factory;
    
    @Before
    public void setUp() {
        factory = new FeatureServiceDirectFactory();
    }
    

    @Test
    public void testCreateFeatureServiceWithDefault() {
        FeatureService featureService = factory.createFeatureService();
        assertNotNull(featureService);
    }
    
    @Test
    public void testInitPropertyReaders() {
        factory = spy(factory);
        Environment environment = new Environment();
        Properties properties = mock(Properties.class);
        environment.setProperties(properties);
        PropertyReader propertyReader = mock(PropertyReader.class);
        environment.getPropertyReaders().add(propertyReader);
        
        factory.setEnvironment(environment);
                
        factory.createFeatureService();
        
        verify(propertyReader).initialize(eq(properties));
    }
}
