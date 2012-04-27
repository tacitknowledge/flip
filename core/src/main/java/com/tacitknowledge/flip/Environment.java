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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.tacitknowledge.flip.properties.PropertyReader;

/**
 * This class represents a place holder for all of the environment related information
 * that is required by the Flip system, such as {@link Properties}, a list of
 * {@link PropertyReader}s and context providers.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class Environment
{
    private Properties properties = new Properties();

    private List<PropertyReader> propertyReaders = new ArrayList<PropertyReader>();

    private List<Object> contextProviders = new ArrayList<Object>();

    public List<PropertyReader> getPropertyReaders()
    {
        return propertyReaders;
    }

    public void setPropertyReaders(final List<PropertyReader> propertyReaders)
    {
        this.propertyReaders = propertyReaders;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void setProperties(final Properties properties)
    {
        this.properties = properties;
    }

    public List<Object> getContextProviders()
    {
        return contextProviders;
    }

    public void setContextProviders(final List<Object> contextProviders)
    {
        this.contextProviders = contextProviders;
    }
}
