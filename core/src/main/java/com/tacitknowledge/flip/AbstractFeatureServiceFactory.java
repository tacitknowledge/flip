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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.InstanceofPredicate;

import com.tacitknowledge.flip.properties.PropertyReader;

/**
 * Base abstract {@link FeatureService} factory implementation.
 * Provides some basic utility methods.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public abstract class AbstractFeatureServiceFactory
{

    /**
     * Performs initialization of the provided environment.
     * 
     * @param env {@link Environment} entity
     */
    protected void initializeEnvironment(final Environment env)
    {
        for (final PropertyReader propertyReader : env.getPropertyReaders())
        {
            propertyReader.initialize(env.getProperties());
        }
    }

    /**
     * Determines whether provided <code>list</code> contains item 
     * of a given type <code>klass</code>
     * 
     * @param list {@link List} to process
     * @param klass item type to look for
     * @return <code>true</code> in case there is an existent item of a given type within
     *  provided list, <code>false</code> - otherwise
     */
    protected boolean isListHasItemOfType(final List<?> list, final Class<?> klass)
    {
        return CollectionUtils.exists(list, InstanceofPredicate.getInstance(klass));
    }

}
