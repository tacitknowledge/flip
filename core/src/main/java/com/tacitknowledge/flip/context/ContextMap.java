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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tacitknowledge.flip.exceptions.UnknownContextPropertyException;

/**
 * The map view of context used to access the context properties. This map does
 * not holds any information retrieved from context provider. It acts as a view 
 * so it evaluates the value of the property exactly in that moment when it was 
 * asked using {@link #get(java.lang.Object) } method. Almost of the methods 
 * throws {@link UnsupportedOperationException}, exceptions of this rule are:
 * {@link #isEmpty() } which always returns {@code "true"}, {@link #size()} which
 * always returns {@code "1"} and {@link #containsKey(java.lang.Object)} which returns
 * the real existence of the property.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class ContextMap implements Map<String, Object>
{

    public static final String GLOBAL = "_all";

    private final List<ContextDescriptor> contexts;

    /**
     * Constructs the map view based on the list of context descriptors. The 
     * order of the context descriptors is important because if two contexts
     * has the same property will be returned the value of that which is first 
     * in this list.
     * 
     * @param contexts the list of context descriptors.
     */
    public ContextMap(final List<ContextDescriptor> contexts)
    {
        this.contexts = contexts;
    }

    @Override
    public int size()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEmpty()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsKey(final Object key)
    {
        try
        {
            getValue(String.valueOf(key));
            return true;
        }
        catch (final MissingValueException ex)
        {
            return false;
        }
    }

    @Override
    public boolean containsValue(final Object value)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object get(final Object key)
    {
        try
        {
            return getValue(String.valueOf(key));
        }
        catch (final MissingValueException ex)
        {
            throw new UnknownContextPropertyException(String.format("Cannot find property named [%s].", key));
        }
    }

    @Override
    public Object put(final String key, final Object value)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object remove(final Object key)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putAll(final Map<? extends String, ? extends Object> m)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> keySet()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Object> values()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Entry<String, Object>> entrySet()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns the value of context property. It loops by the context descriptors
     * passed to constructor and when it finds the property returns its value.
     * If there is no such property in all contexts will be raised 
     * {@link MissingValueException} exception.
     * 
     * @param name the name of property whose value to return.
     * @return the value of the property
     * @throws MissingValueException when there is no such property found
     */
    private Object getValue(final String name) throws MissingValueException
    {
        for (final ContextDescriptor context : contexts)
        {
            try
            {
                return getValueFromContext(context, name);
            }
            catch (final MissingValueException ex)
            {}
        }
        throw new MissingValueException();
    }

    /**
     * Looks up for a property in a specific context.
     * 
     * @param contextDescriptor the context descriptor where to find for the property.
     * @param name the name of property to find.
     * @return the value of context property found.
     * @throws MissingValueException when no such property was found in this context.
     */
    private Object getValueFromContext(final ContextDescriptor contextDescriptor, final String name)
        throws MissingValueException
    {
        try
        {
            return getNamedValue(contextDescriptor, name);
        }
        catch (final MissingValueException ex)
        {
            return getAnonymousValue(contextDescriptor, name);
        }
    }

    /**
     * Looks for a property in named properties of the context descriptor.
     * For more details see {@link ContextDescriptor#getProperties() }.
     * 
     * @param contextDescriptor the context descriptor where to find for the property.
     * @param name the name of the property.
     * @return the value of the property found.
     * @throws MissingValueException when there is no such property found.
     */
    private Object getNamedValue(final ContextDescriptor contextDescriptor, final String name)
        throws MissingValueException
    {
        try
        {
            final Method methodToInvoke = contextDescriptor.getProperties().get(name);
            if (methodToInvoke == null)
            {
                throw new MissingValueException();
            }
            return methodToInvoke.invoke(contextDescriptor.getContext());
        }
        catch (final IllegalAccessException ex)
        {
            Logger.getLogger(ContextManager.class.getName()).log(Level.WARNING, null, ex);
        }
        catch (final IllegalArgumentException ex)
        {
            Logger.getLogger(ContextManager.class.getName()).log(Level.WARNING, null, ex);
        }
        catch (final InvocationTargetException ex)
        {
            Logger.getLogger(ContextManager.class.getName()).log(Level.WARNING, null, ex);
        }
        throw new MissingValueException();
    }

    /**
     * Looks for a property in anonymous properties of the context descriptor.
     * For more details see {@link ContextDescriptor#getAnonymousProperties() }.
     * 
     * @param contextDescriptor the context descriptor where to find for the property.
     * @param name the name of the property.
     * @return the value of the property found.
     * @throws MissingValueException when there is no such property found.
     */
    private Object getAnonymousValue(final ContextDescriptor contextDescriptor, final String name)
        throws MissingValueException
    {
        for (final Method method : contextDescriptor.getAnonymousProperties())
        {
            try
            {
                final Map<?, ?> map = (Map<?, ?>) method.invoke(contextDescriptor.getContext());
                if (!map.containsKey(name))
                {
                    throw new MissingValueException();
                }
                return map.get(name);
            }
            catch (final IllegalAccessException ex)
            {}
            catch (final IllegalArgumentException ex)
            {}
            catch (final InvocationTargetException ex)
            {}
        }
        throw new MissingValueException();
    }

}
