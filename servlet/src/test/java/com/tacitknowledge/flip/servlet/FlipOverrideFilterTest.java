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
package com.tacitknowledge.flip.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
import static org.mockito.Matchers.anyBoolean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import com.tacitknowledge.flip.model.FeatureDescriptor;
import com.tacitknowledge.flip.model.FeatureState;
import com.tacitknowledge.flip.properties.FeatureDescriptorsMap;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class FlipOverrideFilterTest
{

    private FlipOverrideFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;
    
    @Before
    public void setUp() throws IOException, ServletException {
        filter = new FlipOverrideFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }
    
    @Test
    public void testDoNotCreateSession() throws IOException, ServletException {
        when(request.getSession(eq(false))).thenReturn(null);
        
        filter.doFilter(request, response, filterChain);
        verify(request, never()).getSession();
        verify(request, never()).getSession(eq(true));
    }
    
    @Test
    public void testInvokeFilerChain() throws IOException, ServletException {
        filter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(eq(request), eq(response));
    }
    
    @Test
    public void testClearContextAfterFilerExecuted() throws IOException, ServletException {
        FlipWebContext.setFeatureDescriptors(new FeatureDescriptorsMap());

        filter.doFilter(request, response, filterChain);
        assertNull(FlipWebContext.getFeatureDescriptors());
    }
    
    @Test
    public void testUsePropertiesFromRequest() throws IOException, ServletException {
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getSession(anyBoolean())).thenReturn(session);

        Map<String, String[]> requestParams = new HashMap<String, String[]>();
        requestParams.put("flip.featureName", new String[]{"enabled"});
        when(request.getParameterMap()).thenReturn(requestParams);
        
        FeatureDescriptorsMap sessionFeatureMap = new FeatureDescriptorsMap();
        when(session.getAttribute(FlipOverrideFilter.SESSION_FEATURES_KEY)).thenReturn(sessionFeatureMap);

        filter.doFilter(request, response, filterChain);
        
        FeatureDescriptor featureDescriptor = sessionFeatureMap.get("featureName");
        assertEquals("featureName", featureDescriptor.getName());
        assertEquals(FeatureState.ENABLED, featureDescriptor.getState());
    }
    
    @Test
    public void testInvalidPropertiesInRequest() throws IOException, ServletException {
        HttpSession session = mock(HttpSession.class);
        FilterConfig filterConfig = mock(FilterConfig.class);

        when(request.getSession()).thenReturn(session);
        when(request.getSession(anyBoolean())).thenReturn(session);

        Map<String, String[]> requestParams = new HashMap<String, String[]>();
        requestParams.put("flip.featureName", new String[]{"blahblahblah"});
        requestParams.put("featureName2", new String[]{"enabled"});
        when(request.getParameterMap()).thenReturn(requestParams);
        
        Map<String, FeatureDescriptor> sessionFeatureMap = new HashMap<String, FeatureDescriptor>();
        when(session.getAttribute(FlipOverrideFilter.SESSION_FEATURES_KEY)).thenReturn(sessionFeatureMap);

        filter.init(filterConfig);
        filter.doFilter(request, response, filterChain);
        
        assertTrue(sessionFeatureMap.isEmpty());
        assertNull(sessionFeatureMap.get("featureName"));
        assertNull(sessionFeatureMap.get("featureName2"));
    }
    
}
