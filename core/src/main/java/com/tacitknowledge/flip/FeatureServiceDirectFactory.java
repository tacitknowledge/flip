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
import java.util.Properties;

import com.tacitknowledge.flip.properties.PropertyReader;

/**
 * Feature Service factory that creates {@link FeatureService} leveraging
 * reflection capabilities. This factory should be used when there is no DI
 * that is used within the system, and we want to leave responsibility 
 * of finding and instantiating {@link PropertyReader}s and Context Providers
 * to the reflection engine. 
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class FeatureServiceDirectFactory extends AbstractFeatureServiceFactory
{
    private static final FeatureServiceDirectFactory instance = new FeatureServiceDirectFactory();

    private Environment environment = new Environment();

    /**
     * Instance method for {@link FeatureServiceDirectFactory}
     * @return instance of {@link FeatureServiceDirectFactory}
     */
    public static FeatureServiceDirectFactory getInstance()
    {
        return instance;
    }

    /**
     * Factory method that creates a {@link FeatureService} entity
     * based on the internal {@link Environment} data.
     * 
     * @return initialized entity of {@link FeatureService}
     */
    public FeatureService createFeatureService()
    {
        initializeEnvironment(environment);
        return new FeatureServiceImpl(environment);
    }

    public void setPropertyReaders(final List<PropertyReader> propertyReaders)
    {
        environment.setPropertyReaders(propertyReaders);
    }

    public void setContextProviders(final List<Object> contextProviders)
    {
        environment.setContextProviders(contextProviders);
    }

    public void setProperties(final Properties properties)
    {
        environment.setProperties(properties);
    }

    public void setEnvironment(final Environment environment)
    {
        this.environment = environment;
    }
}
