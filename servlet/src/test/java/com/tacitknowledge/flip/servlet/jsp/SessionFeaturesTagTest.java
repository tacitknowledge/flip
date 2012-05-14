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
import javax.servlet.jsp.JspWriter;
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
        JspWriter printWriter = new JspWriter(1024, true) {

            @Override
            public void newLine() throws IOException {
                writer.write("\n");
            }

            @Override
            public void print(boolean b) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void print(char c) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void print(int i) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void print(long l) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void print(float f) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void print(double d) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void print(char[] s) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void print(String s) throws IOException {
                writer.write(s);
            }

            @Override
            public void print(Object obj) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void println() throws IOException {
                newLine();
            }

            @Override
            public void println(boolean x) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void println(char x) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void println(int x) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void println(long x) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void println(float x) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void println(double x) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void println(char[] x) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void println(String x) throws IOException {
                writer.write(x+"\n");
            }

            @Override
            public void println(Object x) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void clear() throws IOException {
                
            }

            @Override
            public void clearBuffer() throws IOException {
                
            }

            @Override
            public void flush() throws IOException {
                writer.flush();
            }

            @Override
            public void close() throws IOException {
                writer.close();
            }

            @Override
            public int getRemaining() {
                return 0;
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                writer.write(cbuf, off, len);
            }
        };
        when(pageContext.getOut()).thenReturn(printWriter);
        
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
