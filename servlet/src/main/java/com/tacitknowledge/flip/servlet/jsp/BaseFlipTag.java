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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.exceptions.FlipException;
import com.tacitknowledge.flip.model.FeatureState;

/**
 * Base tag for <code>flip:enabled</code> and <code>flip:disabled</code> tags implementations.
 * In constructor the implementors should pass it state so when the feature state 
 * is evaluated it is compared with it and if they are equal the content of this tag is 
 * displayed, otherwise is ignored.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public abstract class BaseFlipTag extends TagSupport
{
    private String feature;

    private final FeatureState myState;

    /**
     * Creates a base flip tag with its state passed as constructor parameter.
     * 
     * @param myState the tag state.
     */
    public BaseFlipTag(final FeatureState myState)
    {
        this.myState = myState;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int doStartTag() throws JspException
    {
        FeatureService service = getFeatureService();
        if (service == null)
        {
            throw new FlipException("The FeatureService has not been instantiated.");
        }
        return service.getFeatureState(feature) == myState ? Tag.EVAL_BODY_INCLUDE : Tag.SKIP_BODY;
    }

    /**
     * Gets the feature service. This method should be implemented in descendants
     * to obtain the feature service to evaluate the feature name obtained from 
     * {@link #getFeature() }.
     * 
     * @return <code>featureService</code>
     */
    protected abstract FeatureService getFeatureService();

    /**
     * Returns the feature name which should be evaluated to display the contents of this tag.
     * 
     * @return the name of the feature to evaluate.
     */
    public String getFeature()
    {
        return feature;
    }

    /**
     * Sets the feature name to evaluate to display the contents of this tag or not.
     * 
     * @param feature the feature name.
     */
    public void setFeature(final String feature)
    {
        this.feature = feature;
    }
}
