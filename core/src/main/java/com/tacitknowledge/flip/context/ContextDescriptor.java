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
package com.tacitknowledge.flip.context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class that holds the information about context provider. This class do not 
 * holds the key and values stored in the contexts, but only information
 * which will help on runtime to retrieve the information from context providers.<br />
 * Each context provider is a free form class which could have any number of methods 
 * and named in free form. The main rule is that each method in context provider
 * should not have parameters and should have a return type.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class ContextDescriptor
{
    private String name;

    private Object context;

    private Map<String, Method> properties = new HashMap<String, Method>();

    private List<Method> anonymousProperties = new ArrayList<Method>();

    /**
     * Returns the name of context. This name if taken from {@link FlipContext#name() }
     * field of the class annotation.
     * 
     * @return the context name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of context.
     * @param name 
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Returns the list methods marked with annotation {@link FlipContextProperty} with
     * empty {@link FlipContextProperty#value() }. These methods returns the
     * properties direct in the context. Here are stored only methods which return 
     * {@link java.util.Map}. So the key of the map is direct accessible from context
     * as it is a property of it.
     * 
     * @return the list of anonymous property methods of context.
     */
    public List<Method> getAnonymousProperties()
    {
        return anonymousProperties;
    }

    /**
     * Sets the list of anonymous property methods. For more info see {@link #getAnonymousProperties()}.
     * 
     * @param anonymousProperties the list of anonymous property methods.
     */
    public void setAnonymousProperties(final List<Method> anonymousProperties)
    {
        this.anonymousProperties = anonymousProperties;
    }

    /**
     * Returns the named property methods. This getter returns the methods from
     * context which are marked with annotation {@link FlipContextProperty} with 
     * value set or without this annotation and it reflects the context method signature.
     * As key of this map acts the value of {@link FlipContextProperty} or the
     * method name normalized. The method name normalization is made by the following
     * rule: if method is getter then the property name acts the part of method
     * name without {@code "get"} or {@code "is"} and staring from the lower case,
     * otherwise the method name itself is returned. Note that if method name starts
     * with {@code "is"} the method should return {@code boolean} value.
     * As value acts the method itself. The key of this property acts as a context 
     * property, but the result of invoking the method from map value is the value 
     * of this context property.
     * 
     * @return the map of property names and methods associated with it.
     */
    public Map<String, Method> getProperties()
    {
        return properties;
    }

    /**
     * Sets the list of property methods marked by context property name.
     * For more details see {@link #getProperties() }.
     * 
     * @param properties list of map or context properties.
     */
    public void setProperties(final Map<String, Method> properties)
    {
        this.properties = properties;
    }

    /**
     * Returns the instantiated object of the context class. This object will be 
     * used to invoke methods listed in {@link #getAnonymousProperties()} and
     * {@link #getProperties()}.
     * 
     * @return the context object.
     */
    public Object getContext()
    {
        return context;
    }

    /**
     * Sets the context object. For more details see {@link #getContext() }.
     * 
     * @param context the context object.
     */
    public void setContext(final Object context)
    {
        this.context = context;
    }

}
