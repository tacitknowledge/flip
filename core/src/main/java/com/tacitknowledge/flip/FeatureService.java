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
package com.tacitknowledge.flip;

import com.tacitknowledge.flip.model.FeatureState;

/**
 * Contract for Feature Services, capable of determining state of a given feature.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public interface FeatureService
{
    /**
     * This method is used to obtain {@link FeatureState} value for the provided
     * feature name.
     * 
     * @param name this is the name of the feature to obtain state for.
     * @return one of values of {@link FeatureState}. The general rule is 
     *  if feature is enabled, returned value is {@link FeatureState#ENABLED}, 
     *  otherwise - {@link FeatureState#DISABLED}
     */
    FeatureState getFeatureState(String name);
}
