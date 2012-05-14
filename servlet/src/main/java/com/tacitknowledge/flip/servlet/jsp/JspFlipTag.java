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

import com.tacitknowledge.flip.FlipContext;
import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.model.FeatureState;
import com.tacitknowledge.flip.servlet.FlipWebContext;

/**
 * Base abstract tag for simple JSP Flip tags. It implements the logic to obtain 
 * the feature service from JSP page context.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class JspFlipTag extends BaseFlipTag
{
    /**
     * Constant used to identify the request attribute which holds the {@link FeatureService}.
     */
    public static final String FEATURE_SERVICE_ATTRIBUTE = "featureService";

    /**
     * Constructs the JSP tag and passes its state. Depending of its state
     * the tag will display or not the content.
     * 
     * @param myState the state of the tag
     */
    public JspFlipTag(final FeatureState myState)
    {
        super(myState);
    }

    private FeatureService service;

    /**
     * Returns the {@link FeatureService} passed as attribute to the tag.
     * 
     * @return {@link FeatureService} passed as tag attribute.
     */
    public FeatureService getService()
    {
        return service;
    }

    /**
     * Sets the {@link FeatureService}. By default it is used to set feature service 
     * by tag attribute.
     * 
     * @param service the {@link FeatureService}.
     */
    public void setService(final FeatureService service)
    {
        this.service = service;
    }

    /**
     * Returns the {@link FeatureService}. obtained by the following rules:
     * firstly it looks if feature service was set by tag attribute, then it looks 
     * by the request attribute named {@code "featureService"} and then it retrieves 
     * it from {@link FlipWebContext#getFeatureService() }.
     */
    @Override
    protected FeatureService getFeatureService()
    {
        return FlipContext.chooseFeatureService(
                service, 
                getFeatureServiceFromRequest());
    }
    
    private FeatureService getFeatureServiceFromRequest() {
        Object featureServiceObject = pageContext.getRequest().getAttribute(FEATURE_SERVICE_ATTRIBUTE);
        if (featureServiceObject instanceof FeatureService) {
            return (FeatureService) featureServiceObject;
        } else {
            return null;
        }
    }
}
