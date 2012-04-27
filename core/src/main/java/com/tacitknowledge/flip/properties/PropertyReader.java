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

import java.util.Properties;

import com.tacitknowledge.flip.model.FeatureDescriptor;

/**
 * Interface for property readers. Property reader is an entity which reads the 
 * feature descriptors from an arbitrary source. 
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public interface PropertyReader
{
    /**
     * Initializes the property reader using the properties passed. There are no 
     * standard configuration properties so each implementor defines its own 
     * properties. 
     * 
     * @param config the properties with property reader configuration.
     */
    void initialize(Properties config);

    /**
     * Returns the {@link FeatureDescriptor} object with the specific name. 
     * @param name the name of feature descriptor to return. 
     * @return the {@link FeatureDescriptor} for the specified name. 
     *      Returns null if there is no feature with such name.
     */
    FeatureDescriptor getFeatureDescriptor(String name);
}
