/*
 * Copyright 2012 Tacit Knowledge.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tacitknowledge.flip.aspectj.converters;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the converters. This class holds the list of converters. It allows retrieving 
 * the converter by class to convert to. By default there are registered converterd for
 * primitive types and its wrappers.
 * 
 * @author ssoloviov
 */
public class ConvertersHandler {
    
    private final Map<Class, Converter> converters = new HashMap<Class, Converter>();

    /**
     * Constructs new converter handler.
     */
    public ConvertersHandler() {
        addDefaultConverters();
    }
    
    /**
     * Registers new converter. If two converters manages the same class the last 
     * registered will manage it.
     * 
     * @param converter the converter to register.
     */
    public synchronized void addConverter(Converter converter) {
        for(Class klass : converter.getManagedClasses()) {
            converters.put(klass, converter);
        }
    }
    
    /**
     * Register an array of converters.
     * 
     * @param converters the array of converters to register.
     */
    public synchronized void addConverters(Converter[] converters) {
        for(Converter converter : converters) {
            addConverter(converter);
        }
    }
    
    /**
     * Returns the converter that manages the required class. If there is no
     * converter associated with the class null will be returned.
     * 
     * @param klass the associated class with converter.
     * @return the converter if found or null.
     */
    public Converter getConverter(Class klass) {
        return converters.get(klass);
    }
    
    /**
     * Register the default converters. It registers converters for primitive types
     * and its wrappers.
     */
    private synchronized void addDefaultConverters() {
        addConverters(new Converter[]{
            new BooleanConverter(),
            new ByteConverter(),
            new CharacterConverter(),
            new DoubleConverter(),
            new FloatConverter(),
            new IntegerConverter(),
            new LongConverter(),
            new ShortConverter()
        });
    }
    
}
