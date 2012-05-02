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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.tacitknowledge.flip.model.FeatureDescriptor;
import com.tacitknowledge.flip.properties.FeatureDescriptorsMap;
import com.tacitknowledge.flip.servlet.FlipFilter;
import com.tacitknowledge.flip.servlet.FlipWebContext;

/**
 * The JSP tag used to display the list of feature descriptors set by {@link FlipFilter}
 * from request parameters and stored in the session.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class SessionFeaturesTag extends TagSupport
{
    /**
     * Displays the list of feature descriptors from session set by {@link FlipFilter}
     * from request attributes. It outputs them in the form:
     * <pre>
     * [feature-name] = [feature-state]&lt;br /&gt;
     * ...
     * </pre>
     * If no feature descriptors was found nothing displays.
     * 
     * {@inheritDoc }
     * 
     */
    @Override
    public int doStartTag() throws JspException
    {
        PrintWriter writer = null;
        try
        {
            final FeatureDescriptorsMap featureDescriptorsMap = FlipWebContext.getFeatureDescriptors();
            writer = pageContext.getResponse().getWriter();
            for (final FeatureDescriptor featureDescriptor : featureDescriptorsMap.values())
            {
                writer.write(featureDescriptor.getName());
                writer.write(" = ");
                writer.write(featureDescriptor.getState().name().toLowerCase());
                writer.write("<br />");
            }
        }
        catch (final IOException ex)
        {
            Logger.getLogger(SessionFeaturesTag.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            writer.close();
        }

        return SKIP_BODY;
    }
}
