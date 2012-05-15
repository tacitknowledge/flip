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
package com.tacitknowledge.flip.aspectj;

import java.util.HashMap;
import java.util.Map;

import com.tacitknowledge.flip.aspectj.converters.BooleanConverter;
import com.tacitknowledge.flip.aspectj.converters.ByteConverter;
import com.tacitknowledge.flip.aspectj.converters.CharacterConverter;
import com.tacitknowledge.flip.aspectj.converters.Converter;
import com.tacitknowledge.flip.aspectj.converters.DoubleConverter;
import com.tacitknowledge.flip.aspectj.converters.FloatConverter;
import com.tacitknowledge.flip.aspectj.converters.IntegerConverter;
import com.tacitknowledge.flip.aspectj.converters.LongConverter;
import com.tacitknowledge.flip.aspectj.converters.ShortConverter;

/**
 *
 * @author ssoloviov
 */
public class FlipAopContext {
    
    private static final Map<Class, Converter> converters = new HashMap<Class, Converter>();
    private static ValueExpressionEvaluator valueExpressionEvaluator;
    
    static {
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
        valueExpressionEvaluator = new JexlValueExpressionEvaluator();
    }
    
    public static synchronized void addConverter(Converter converter) {
        for(Class klass : converter.getManagedClasses()) {
            converters.put(klass, converter);
        }
    }
    
    public static synchronized void addConverters(Converter[] converters) {
        for(Converter converter : converters) {
            addConverter(converter);
        }
    }
    
    public static Converter getConverter(Class klass) {
        return converters.get(klass);
    }

    public static ValueExpressionEvaluator getValueExpressionEvaluator() {
        return valueExpressionEvaluator;
    }

    public static synchronized void setValueExpressionEvaluator(ValueExpressionEvaluator valueExpressionEvaluator) {
        FlipAopContext.valueExpressionEvaluator = valueExpressionEvaluator;
    }
    
}
