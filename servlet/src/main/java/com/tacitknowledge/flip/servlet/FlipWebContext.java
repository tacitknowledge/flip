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

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.properties.FeatureDescriptorsMap;
import com.tacitknowledge.flip.servlet.jsp.JspFlipTag;

/**
 * The static utility class which holds the feature descriptors generated
 * by the {@link FlipFilter} and the {@link FeatureService} which could be installed
 * there by developer on application start-up time.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class FlipWebContext
{

    private static ThreadLocal<FeatureDescriptorsMap> featureDescriptors = new ThreadLocal<FeatureDescriptorsMap>();

    /**
     * Returns the feature descriptors set here by the {@link FlipFilter}.
     * 
     * @return the {@link FeatureDescriptorsMap} with feature descriptors generated from the request.
     */
    public static FeatureDescriptorsMap getFeatureDescriptors()
    {
        return featureDescriptors.get();
    }

    /**
     * Sets the feature descriptors to override. By default they are set by {@link FlipFilter}
     * generated from the request parameters.
     * 
     * @param featureDescriptors the {@link FeatureDescriptorsMap} with feature descriptors
     * generated from request parameters.
     */
    public static void setFeatureDescriptors(final FeatureDescriptorsMap featureDescriptors)
    {
        FlipWebContext.featureDescriptors.set(featureDescriptors);
    }

    /**
     * Clears the state for this thread. This method should be called when the request 
     * finishes not to appear memory leak.
     */
    public static void clear()
    {
        featureDescriptors.remove();
    }

}
