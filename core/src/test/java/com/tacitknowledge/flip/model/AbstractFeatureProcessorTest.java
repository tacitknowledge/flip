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
import com.tacitknowledge.flip.context.ContextManager;
import java.util.Map;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
@Ignore
public abstract class AbstractFeatureProcessorTest {
       
    protected ContextManager contextManager;
    protected Map contextMap;

    @Before
    public void baseSetup() {
        Matcher<String> paramMatcher = new BaseMatcher<String>() {

            @Override
            public boolean matches(Object item) {
                return !"option".equals(item) 
                        && !"count".equals(item)
                        && !"param".equals(item);
            }

            @Override
            public void describeTo(Description description) {
                
            }
        };

        Matcher<String> contextMatcher = new BaseMatcher<String>() {

            @Override
            public boolean matches(Object item) {
                return "main".equals(item)
                        || ContextMap.GLOBAL.equals(item);
            }

            @Override
            public void describeTo(Description description) {
                
            }
        };
        
        contextMap = mock(Map.class);
        when(contextMap.get(eq("option"))).thenReturn(true);
        when(contextMap.get(eq("count"))).thenReturn(10);
        when(contextMap.get(eq("param"))).thenReturn("PARAM VALUE");
        when(contextMap.get(argThat(paramMatcher))).thenReturn(null);
        
        contextManager = mock(ContextManager.class);
        when(contextManager.getContext(argThat(contextMatcher))).thenReturn(contextMap);
    }
    
}
