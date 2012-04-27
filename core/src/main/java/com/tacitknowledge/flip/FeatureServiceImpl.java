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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.tacitknowledge.flip.context.ContextManager;
import com.tacitknowledge.flip.exceptions.FlipException;
import com.tacitknowledge.flip.model.FeatureDescriptor;
import com.tacitknowledge.flip.model.FeatureState;
import com.tacitknowledge.flip.properties.PropertyManager;

/**
 * Implementation of {@link FeatureService} contract
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class FeatureServiceImpl implements FeatureService
{
    private static final String FEATURE_HIERARCHY_SEPARATOR = ".";

    private static final Logger logger = Logger.getLogger(FeatureServiceImpl.class.getName());

    private final ContextManager contextManager;

    private final PropertyManager propertyManager;

    /**
     * Constructor that in addition to entity instantiation
     * fetches required information from provided {@link Environment}.
     *  
     * @param environment {@link Environment} entity that contains required information
     *      for {@link ContextManager} and {@link PropertyManager} creation.
     */
    public FeatureServiceImpl(final Environment environment)
    {
        contextManager = new ContextManager(environment);
        propertyManager = new PropertyManager(environment);
    }

    @Override
    public FeatureState getFeatureState(final String name)
    {
        FeatureState result;
        try
        {
            final FeatureDescriptor featureDescriptor = propertyManager.getFeatureDescriptor(name);
            result = featureDescriptor.process(contextManager);
        }
        catch (final FlipException ex)
        {
            logger.log(
                    Level.WARNING,
                    String.format(
                            "An exception has occured during Feature State extraction for feature named [%s]. Using [%s] value.",
                            name, FeatureState.DISABLED.toString()), ex);

            result = FeatureState.DISABLED;
        }

        if (isFeatureHasParent(name))
        {
            final FeatureState parentResult = getFeatureState(getParentFeatureName(name));
            result = FeatureState.getByState(parentResult.state() && result.state());
        }

        return result;
    }

    /**
     * Identifies whether provided feature name has parent feature.
     * 
     * @param name feature name
     * @return <code>true</code> in case provided feature name has parent feature,
     *      <code>false</code> - otherwise.
     */
    private boolean isFeatureHasParent(final String name)
    {
        return name.contains(FEATURE_HIERARCHY_SEPARATOR);
    }

    private String getParentFeatureName(final String name)
    {
        final int i = name.lastIndexOf(FEATURE_HIERARCHY_SEPARATOR);
        return name.substring(0, i);
    }
}
