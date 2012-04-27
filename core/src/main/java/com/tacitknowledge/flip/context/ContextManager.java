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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tacitknowledge.flip.Environment;
import com.tacitknowledge.flip.exceptions.UnknownContextException;

/**
 * The central point to access all context providers.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class ContextManager
{

    private final Map<String, ContextDescriptor> contexts = new HashMap<String, ContextDescriptor>();

    private final List<ContextDescriptor> contextList = new ArrayList<ContextDescriptor>();

    /**
     * Constructs the context manager and adds the context providers set in {@link Environment#getContextProviders() }.
     * 
     * @param environment the environment used to construct this context manager.
     */
    public ContextManager(final Environment environment)
    {
        final ContextDescriptorFactory contextDescriptorFactory = new ContextDescriptorFactory();

        for (final Object contextProvider : environment.getContextProviders())
        {
            if (contextProvider == null)
            {
                continue;
            }

            final ContextDescriptor contextDescriptor = contextDescriptorFactory
                    .createContextDescriptor(contextProvider);
            contexts.put(contextDescriptor.getName(), contextDescriptor);
            contextList.add(contextDescriptor);
        }
    }

    /**
     * Returns the map view used to access the context properties. If the context
     * name is {@code "_all"} then it is created a map view this all properties 
     * available from all contexts. In the resulting map if two contexts
     * has the property with the same name it is used that which was set
     * in {@link Environment#getContextProviders()} closer to the start of the list.
     * If the context name is the name of one existent context (there is a context
     * provider whose annotation value {@link FlipContext#name()} equals to the 
     * contextName parameter) then that context map is returned.
     * 
     * @param contextName the context
     * @return the map view of properties of that context.
     * @throws UnknownContextException throws where no context with such name was found.
     */
    public Map<String, Object> getContext(final String contextName)
    {
        if (ContextMap.GLOBAL.equals(contextName))
        {
            return new ContextMap(contextList);
        }
        else
        {
            final ContextDescriptor descriptor = contexts.get(contextName);
            if (descriptor == null)
            {
                throw new UnknownContextException(String.format("Cannot find context with name [%s].", contextName));
            }
            return new ContextMap(Collections.singletonList(descriptor));
        }
    }
}
