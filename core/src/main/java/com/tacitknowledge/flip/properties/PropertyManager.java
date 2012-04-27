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
package com.tacitknowledge.flip.properties;

import java.util.List;

import com.tacitknowledge.flip.Environment;
import com.tacitknowledge.flip.exceptions.MissingFeatureDescriptorException;
import com.tacitknowledge.flip.model.FeatureDescriptor;

/**
 * The class which manages the property readers. It is a central point to access 
 * all property readers. If there are two ore more property readers which could 
 * return the feature descriptor with the same name the property manager returns 
 * only the first. The order to find the feature descriptor is the same as the 
 * property readers are declared in the environment. 
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class PropertyManager
{
    private final List<PropertyReader> propertyReaders;

    /**
     * Constructs the property manager. All property readers are obtained from environment. 
     * 
     * @param environment the environment used to construct the property manager.
     */
    public PropertyManager(final Environment environment)
    {
        propertyReaders = environment.getPropertyReaders();
    }

    /**
     * Returns the feature descriptor by its name. It returns the first found in 
     * the list obtained from environment. 
     * 
     * @param name the name of feature. 
     * @return {@link FeatureDescriptor} which has the name declared. 
     * @throws MissingFeatureDescriptorException throws when there is no a feature 
     * descriptor with the name passed as a parameter.
     */
    public FeatureDescriptor getFeatureDescriptor(final String name)
    {
        for (final PropertyReader reader : propertyReaders)
        {
            final FeatureDescriptor descriptor = reader.getFeatureDescriptor(name);
            if (descriptor != null)
            {
                return descriptor;
            }
        }

        throw new MissingFeatureDescriptorException(String.format(
                "Couldn't find a feature descriptor for feature named [%s].", name));
    }

}
