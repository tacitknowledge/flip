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
package com.tacitknowledge.flip.servlet.properties;

import com.tacitknowledge.flip.model.FeatureDescriptor;
import com.tacitknowledge.flip.properties.AbstractPropertyReader;
import com.tacitknowledge.flip.properties.FlipProperty;
import com.tacitknowledge.flip.servlet.FlipFilter;
import com.tacitknowledge.flip.servlet.FlipWebContext;

/**
 * The property provider used to retrieve the feature descriptors from session
 * applied there by {@link FlipFilter} from request parameters. This property 
 * provider by default has a bigger priority than {@link com.tacitknowledge.flip.properties.XmlPropertyReader}.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
@FlipProperty(priority = 101)
public class SessionPropertyReader extends AbstractPropertyReader
{
    /**
     * {@inheritDoc }
     */
    public FeatureDescriptor getFeatureDescriptor(final String name)
    {
        return FlipWebContext.getFeatureDescriptors().get(name);
    }
}
