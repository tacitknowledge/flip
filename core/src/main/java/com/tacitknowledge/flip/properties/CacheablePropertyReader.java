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

import com.tacitknowledge.flip.model.FeatureDescriptor;
import java.util.Properties;

/**
 * Abstract Property Reader which holds feature descriptors in a cache. 
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public abstract class CacheablePropertyReader extends AbstractPropertyReader {
    
    /**
     * The cache with feature descriptors. 
     */
    protected final FeatureDescriptorsMap cache = new FeatureDescriptorsMap();

    /**
     * {@inheritDoc }
     */
    @Override
    public void initialize(Properties config) {
        super.initialize(config);
        readDescriptors();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public FeatureDescriptor getFeatureDescriptor(String name) {
        return cache.get(name);
    }
    
    /**
     * Reads the descriptors and holds them in the cache. Implementors should 
     * extend this method to build the cache. 
     */
    protected abstract void readDescriptors();
    
}
