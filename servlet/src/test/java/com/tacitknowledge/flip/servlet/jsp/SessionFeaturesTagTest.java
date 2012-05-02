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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.junit.Before;
import org.junit.Test;

import com.tacitknowledge.flip.model.FeatureDescriptor;
import com.tacitknowledge.flip.model.FeatureState;
import com.tacitknowledge.flip.properties.FeatureDescriptorsMap;
import com.tacitknowledge.flip.servlet.FlipWebContext;
import com.tacitknowledge.flip.servlet.jsp.SessionFeaturesTag;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class SessionFeaturesTagTest {
    
    private SessionFeaturesTag tag;
    private PageContext pageContext;
    private StringWriter writer;
    private FeatureDescriptorsMap featureDescriptors;
    
    @Before
    public void setUp() throws IOException {
        tag = new SessionFeaturesTag();
        pageContext = mock(PageContext.class);
        tag.setPageContext(pageContext);
        
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(pageContext.getResponse()).thenReturn(response);
        
        writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
        
        featureDescriptors = new FeatureDescriptorsMap();
        FlipWebContext.setFeatureDescriptors(featureDescriptors);
    }
    
    @Test
    public void testListFeatures() throws JspException {
        featureDescriptors.put("featureName", new FeatureDescriptor(){{
            setName("featureName");
            setState(FeatureState.ENABLED);
        }});
        
        tag.doStartTag();
        
        assertEquals("featureName = enabled<br />", writer.toString());
    }
    
    @Test
    public void testEmptyListFeatures() throws JspException {
        tag.doStartTag();
        
        assertEquals("", writer.toString());
    }
    
}
