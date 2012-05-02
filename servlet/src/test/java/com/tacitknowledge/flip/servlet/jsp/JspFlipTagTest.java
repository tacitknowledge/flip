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

package com.tacitknowledge.flip.servlet.jsp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.junit.Before;
import org.junit.Test;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.model.FeatureState;
import com.tacitknowledge.flip.servlet.FlipWebContext;
import com.tacitknowledge.flip.servlet.jsp.JspFlipTag;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class JspFlipTagTest
{

    private JspFlipTag tag;

    private FeatureService featureService;

    private HttpServletRequest request;

    @Before
    public void setUp()
    {
        tag = new JspFlipTag(FeatureState.ENABLED);
        featureService = mock(FeatureService.class);

        final PageContext pageContext = mock(PageContext.class);
        tag.setPageContext(pageContext);
        request = mock(HttpServletRequest.class);
        when(pageContext.getRequest()).thenReturn(request);

        FlipWebContext.setFeatureService(null);
    }

    @Test
    public void testGetFeatureServiceByTagParameter()
    {
        tag.setService(featureService);
        assertEquals(featureService, tag.getFeatureService());
    }

    @Test
    public void testGetFeatureServiceByRequestAttribute()
    {
        when(request.getAttribute(eq(JspFlipTag.FEATURE_SERVICE_ATTRIBUTE))).thenReturn(featureService);

        assertEquals(featureService, tag.getFeatureService());
    }

    @Test
    public void testGetFeatureServiceWhenNoRequestAttribute()
    {
        when(request.getAttribute(eq(JspFlipTag.FEATURE_SERVICE_ATTRIBUTE))).thenReturn(null);

        assertNull(tag.getFeatureService());
    }

    @Test
    public void testGetFeatureServiceWhenInRequestInvalidObject()
    {
        when(request.getAttribute(eq(JspFlipTag.FEATURE_SERVICE_ATTRIBUTE))).thenReturn(new Integer("10"));

        assertNull(tag.getFeatureService());
    }

    @Test
    public void testGetFeatureServiceByWebContext()
    {
        FlipWebContext.setFeatureService(featureService);

        assertEquals(featureService, tag.getFeatureService());
    }

    @Test
    public void testGetFeatureServiceByPriorityBetweenAttributeAndRequestAttribute()
    {
        when(request.getAttribute(eq(JspFlipTag.FEATURE_SERVICE_ATTRIBUTE))).thenReturn(featureService);

        final FeatureService paramService = mock(FeatureService.class);
        tag.setService(paramService);

        assertEquals(paramService, tag.getFeatureService());
    }

    @Test
    public void testGetFeatureServiceByPriorityBetweenREquestAttributeAndWebContext()
    {
        when(request.getAttribute(eq(JspFlipTag.FEATURE_SERVICE_ATTRIBUTE))).thenReturn(featureService);

        final FeatureService paramService = mock(FeatureService.class);
        FlipWebContext.setFeatureService(paramService);

        assertEquals(featureService, tag.getFeatureService());
    }

}
