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
package com.tacitknowledge.flip.model;

import com.tacitknowledge.flip.context.ContextManager;

/**
 * Interface for model classes used to process the feature state. 
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public interface FeatureProcessor {
    
    /**
     * The method used to process the feature state. Here will be processed the 
     * model by its own rules and the result will returned back. If the model 
     * cannot determine its state this method should return {@code null}. 
     * 
     * @param contextManager the context used to process the model object. 
     * @return the state of this model object after processing.
     */
    FeatureState process(ContextManager contextManager);
}
