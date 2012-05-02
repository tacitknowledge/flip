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

import com.tacitknowledge.flip.model.FeatureDescriptors;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.tacitknowledge.flip.model.FeatureDescriptor;

/**
 * The class which reads the feature descriptors from XML file. It caches them and 
 * periodically refreshes the cache. <br />
 * The XML file should be of the following structure:<br />
 * <pre>
 * &lt;features&gt;
 * &nbsp;&nbsp;&lt;feature name="[name]" state="[overriding-state]"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;rule state="[state-to-apply]"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;condition context="[context]" name="[property]" operation="[operation]" value="[value-to-compare]" /&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/rule&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;...
 * &nbsp;&nbsp;&lt;/feature&gt;
 * &nbsp;&nbsp;...
 * &lt;/features&gt;
 * </pre>
 * 
 * <p>
 * where:
 * <ul>
 * <li>[name] - is the feature name.</li>
 * <li>[overriding-state] - the state which is used. The rules are not processed.</li>
 * <li>[state-to-apply] - this state is applied if the conditions in the rule match.</li>
 * <li>[context] - the context where to find the [property]. If it is set to "_all" then 
 * the property will be searched in all contexts available.</li>
 * 
 * <li>[property] - the name of property from the context(s). The property could be a dotted expression
 * which will return the values of nested objects.</li>
 * <li>[operation] - the operation to apply to the value of property with value. Here could be the following operations:<br />
 * <dl>
 * <dt>eq</dt> <dd>- checks for equality</dd>
 * <dt>ne</dt> <dd>- checks for inequality</dd>
 * <dt>lt</dt> <dd>- checks that property is less than value</dd>
 * <dt>le</dt> <dd>- checks that property is less or equals to value</dd>
 * <dt>gt</dt> <dd>- checks that property is greater than value</dd>
 * <dt>ge</dt> <dd>- checks that property is greater or equals to value</dd>
 * <dt>matches</dt> <dd>- checks that the regular expression passed as value matches the property value</dd>
 * <dt>not-matches</dt> <dd>- checks that the regular expression passed as value do not matches the property value</dd>
 * </dl>
 * </li>
 * <li>[value] - the value used to compare the property value using operator.</li>
 * </ul>
 * </p>
 * <p>
 * The features, rules and conditions could be as many as you want.
 * The rules are applied sequentially as they are declared. The last rule should be without conditions
 * as a default rule. If none of conditions will match the last one will return the result.
 * </p>
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
@FlipProperty(priority = 100)
public class XmlPropertyReader extends RefreshablePropertyReader
{

    /**
     * The name of property whose value points to the XML file with configuration.
     */
    public static final String CONFIG_PROPERTY = "flip.properties.xml.path";

    /**
     * The property whose value points to XML file with configuration. 
     */
    public static final String CONFIG_FILE_NAME = "flip.properties.xml";

    /**
     * Returns the stream with XML configuration file content. Firstly this 
     * method looks up for a property {@link #CONFIG_PROPERTY} in properties passed 
     * if there is no such property it looks up {@link #CONFIG_PROPERTY} property 
     * in system properties and if here too the property was not found it looks 
     * for the XML file in classpath by name {@code "flip.properties.xml"}. 
     * 
     * @param props the properties with configuration 
     * @return the stream with XML file content.
     */
    protected InputStream getConfigurationStream(final Properties props)
    {
        InputStream result = getConfigStreamFromProperties(props);
        if (result != null)
        {
            return result;
        }

        result = getConfigStreamFromProperties(System.getProperties());
        if (result != null)
        {
            return result;
        }

        result = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        if (result != null)
        {
            return result;
        }

        return null;
    }

    /**
     * Looks up in the properties for {@link #CONFIG_PROPERTY} and returns the 
     * stream of the XML file configuration. If the properties does not contains 
     * such a property or the property points to an inexistent file returns {@code null}. 
     * 
     * @param props the properties where to look up for {@link #CONFIG_PROPERTY}. 
     * @return the stream with XML file content.
     */
    protected InputStream getConfigStreamFromProperties(final Properties props)
    {
        if (props == null)
        {
            return null;
        }

        try
        {
            final String path = props.getProperty(CONFIG_PROPERTY);
            if (path == null)
            {
                return null;
            }

            final File configFile = new File(path);
            if (!configFile.exists())
            {
                return null;
            }

            return new FileInputStream(configFile);
        }
        catch (final IOException ex)
        {
            Logger.getLogger(XmlPropertyReader.class.getName()).log(Level.WARNING, null, ex);
            return null;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void readDescriptors()
    {
        try
        {
            final JAXBContext context = JAXBContext.newInstance(FeatureDescriptors.class);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final FeatureDescriptors descriptors = (FeatureDescriptors) unmarshaller
                    .unmarshal(getConfigurationStream(getConfig()));

            cache.clear();
            if (descriptors != null && descriptors.getFeatures() != null)
            {
                for (final FeatureDescriptor descriptor : descriptors.getFeatures())
                {
                    cache.put(descriptor.getName(), descriptor);
                }
            }
        }
        catch (final JAXBException ex)
        {
            Logger.getLogger(XmlPropertyReader.class.getName()).log(Level.WARNING, null, ex);
        }
    }

}
