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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Lists;
import com.tacitknowledge.flip.context.FlipContext;
import com.tacitknowledge.flip.properties.FlipProperty;
import com.tacitknowledge.flip.properties.PropertyReader;
import com.tacitknowledge.flip.spi.ServiceProvider;

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
public class FeatureServiceReflectionFactory extends AbstractFeatureServiceFactory
{
    private static final FeatureServiceReflectionFactory instance = new FeatureServiceReflectionFactory();

    /**
     * Instance method for {@link FeatureServiceReflectionFactory}
     * @return instance of {@link FeatureServiceReflectionFactory}
     */
    public static FeatureServiceReflectionFactory getInstance()
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
        return createFeatureService(null, (String[]) null);
    }

    /**
     * Factory method that creates a {@link FeatureService} entity
     * with provided <code>properties</code>.
    
     * @param properties environment specific {@link Properties}
     * @return initialized entity of {@link FeatureService}
     */
    public FeatureService createFeatureService(final Properties properties)
    {
        return createFeatureService(properties, (String[]) null);
    }

    /**
     * Factory method that creates a {@link FeatureService} entity
     * and performs its internal initialization based on the provided list
     * of the packages, that are used for reflection lookup process.
     * 
     * @param packagesToSearch list of package names to take part in the 
     *      {@link ServiceProvider} search process with the purpose of
     *      initialization of the classes declared as {@link PropertyReader}s and
     *      Context Providers
     * @return
     */
    public FeatureService createFeatureService(final String... packagesToSearch)
    {
        return createFeatureService(null, packagesToSearch);
    }

    /**
     * 
     * @param properties environment specific {@link Properties} 
     * @param packagesToSearch list of package names to take part in the 
     *      {@link ServiceProvider} search process with the purpose of
     *      initialization of the classes declared as {@link PropertyReader}s and
     *      Context Providers
     * @return
     */
    public FeatureService createFeatureService(final Properties properties, final String... packagesToSearch)
    {
        final Environment environment = getEnvironment(properties, packagesToSearch);

        initializeEnvironment(environment);

        return new FeatureServiceImpl(environment);
    }

    /**
     * This method is used to create and initialize {@link Environment}
     * entity based on the configuration provided by <code>properties</code>
     * and <code>packagesToSearch</code>.
     * Note that "com.tacitknowledge.flip" is included by default into the
     * list of packages to perform reflection lookup operation.
     * 
     * @param properties environment specific {@link Properties}
     * @param packagesToSearch list of package names to take part in the 
     *      {@link ServiceProvider} search process with the purpose of
     *      initialization of the classes declared as {@link PropertyReader}s and
     *      Context Providers
     * @return an initialized {@link Environment} entity based on the provided
     *  <code>properties</code> and <code>packagesToSearch</code>
     */
    protected Environment getEnvironment(final Properties properties, String... packagesToSearch)
    {
        final Environment environment = new Environment();

        if (properties != null)
        {
            environment.setProperties(properties);
        }

        packagesToSearch = ArrayUtils.add(packagesToSearch, getClass().getPackage().getName());
        final ServiceProvider serviceProvider = new ServiceProvider(packagesToSearch);

        final List<PropertyReader> propertyReaders = Lists.newArrayList(serviceProvider.find(FlipProperty.class,
                PropertyReader.class));
        Collections.sort(propertyReaders, new FlipPropertiesPriorityComparator());
        environment.setPropertyReaders(propertyReaders);

        final List<Object> contextProviders = Lists.newArrayList(serviceProvider.find(FlipContext.class));
        Collections.sort(contextProviders, new FlipContextsPriorityComparator());
        environment.setContextProviders(contextProviders);

        return environment;
    }

    /**
     * Comparator for entities that are annotated with {@link FlipContext}.
     * Comparison is made based on the  {@link FlipContext#priority()} value of
     * {@link FlipContext} annotation. Higher priority value means precedence. 
     */
    private static class FlipContextsPriorityComparator implements Comparator<Object>
    {

        @Override
        public int compare(final Object o1, final Object o2)
        {
            if (!o1.getClass().isAnnotationPresent(FlipContext.class)
                    || !o2.getClass().isAnnotationPresent(FlipContext.class))
            {
                return -1;
            }

            final int priority1 = o1.getClass().getAnnotation(FlipContext.class).priority();
            final int priority2 = o2.getClass().getAnnotation(FlipContext.class).priority();
            return priority2 - priority1;
        }

    }

    /**
     * Comparator for entities that are annotated with {@link FlipProperty}.
     * Comparison is made based on the  {@link FlipProperty#priority()} value of
     * {@link FlipProperty} annotation. Higher priority value means precedence. 
     */
    private static class FlipPropertiesPriorityComparator implements Comparator<Object>
    {

        @Override
        public int compare(final Object o1, final Object o2)
        {
            if (!o1.getClass().isAnnotationPresent(FlipProperty.class)
                    || !o2.getClass().isAnnotationPresent(FlipProperty.class))
            {
                return -1;
            }

            final int priority1 = o1.getClass().getAnnotation(FlipProperty.class).priority();
            final int priority2 = o2.getClass().getAnnotation(FlipProperty.class).priority();
            return priority2 - priority1;
        }

    }
}
