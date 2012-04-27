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
import java.util.Map;

/**
 * The factory used to create {@link ContextDescriptor}.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class ContextDescriptorFactory
{

    /**
     * The factory method used to create {@link ContextDescriptor}. This method
     * parses using java reflection API and extracts all methods which have no 
     * parameters and have a return type. After that it generates {@link ContextDescriptor}
     * setting the required information in it.
     * 
     * @param contextProvider the context provider object used to obtain such information.
     * @return the {@link ContextDescriptor} object which reflects this context provider.
     */
    public ContextDescriptor createContextDescriptor(final Object contextProvider)
    {
        final FlipContext contextAnnotation = contextProvider.getClass().getAnnotation(FlipContext.class);
        if (contextAnnotation == null)
        {
            return null;
        }

        final ContextDescriptor contextDescriptor = new ContextDescriptor();
        contextDescriptor.setName(contextAnnotation.name());
        contextDescriptor.setContext(contextProvider);
        addContextProperties(contextDescriptor);

        return contextDescriptor;
    }

    /**
     * Adds the property descriptors to the {@link ContextDescriptor}. This method
     * obtains the information based on the methods from the {@link ContextDescriptor#getContext()}
     * object set before.
     * 
     * @param contextDescriptor the context descriptor where to add the property information. 
     */
    private void addContextProperties(final ContextDescriptor contextDescriptor)
    {
        for (final Method method : contextDescriptor.getContext().getClass().getMethods())
        {
            if (method.getParameterTypes().length != 0 || method.getReturnType() == Void.TYPE)
            {
                continue;
            }

            final String propertyName = getPropertyName(method);
            if (propertyName == null || propertyName.isEmpty())
            {
                if (Map.class.isAssignableFrom(method.getReturnType()))
                {
                    contextDescriptor.getAnonymousProperties().add(method);
                }
            }
            else
            {
                contextDescriptor.getProperties().put(propertyName, method);
            }
        }
    }

    /**
     * Retrieves the name of the property based in the methods of context provider.
     * It looks for {@link FlipContextProperty} if the method is annotated.
     * If the this annotation has empty value set then this method returns an 
     * empty string otherwise the method name is evaluated by 
     * {@link #getPropertyNameByMethodName(java.lang.reflect.Method)},
     * 
     * @param method the method of context provider.
     * @return the name of property associated to the method.
     */
    private String getPropertyName(final Method method)
    {
        final FlipContextProperty contextPropertyAnnotation = method.getAnnotation(FlipContextProperty.class);
        if (contextPropertyAnnotation != null)
        {
            return contextPropertyAnnotation.value();
        }
        else
        {
            return getPropertyNameByMethodName(method);
        }
    }
    /**
    * Evaluates the name of method by its name. If the method starts with {@code "get"}
    * it returns the rest of name and starting with a letter in lower case. If the 
    * method starts with {@code "is"} and returns {@code boolean} then the
    * name is the name of method without {@code "is"} and started with a letter 
    * in lower case. Otherwise the method name itself is returned.
    * 
    * @param method the method whose name should be evaluated.
    * @return the name of the property associated with this method.
    */
    private String getPropertyNameByMethodName(final Method method)
    {
        final String name = method.getName();
        if (name.startsWith("get"))
        {
            return name.substring(3, 4).toLowerCase() + name.substring(4);
        }
        else if (name.startsWith("is")
                && (Boolean.TYPE == method.getReturnType() || Boolean.class == method.getReturnType()))
        {
            return name.substring(2, 3).toLowerCase() + name.substring(3);
        }
        else
        {
            return name;
        }
    }

}
