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

import com.tacitknowledge.flip.aspectj.converters.ConvertersHandler;

/**
 * The context used in AOP flipping actions. This class contains only static
 * methods which allows retrieving the converters and value expression evaluators
 * used to process disabled values.
 * 
 * @author ssoloviov
 */
public class FlipAopContext {
    
    private static final ConvertersHandler convertersHandler = new ConvertersHandler();
    private static ValueExpressionEvaluator valueExpressionEvaluator;
    
    static {
        valueExpressionEvaluator = new JexlValueExpressionEvaluator();
    }
    
    /**
     * Returns {@link ConvertersHandler} instance used to obtain converter.
     * 
     * @return the {@link ConvertersHandler} instance.
     */
    public static ConvertersHandler getConvertersHandler() {
        return convertersHandler;
    }

    /**
     * Returns the {@link ValueExpressionEvaluator} used to evaluate the 
     * expressions used as disabled values.
     * 
     * @return the {@link ValueExpressionEvaluator}.
     */
    public static ValueExpressionEvaluator getValueExpressionEvaluator() {
        return valueExpressionEvaluator;
    }

    /**
     * Sets the value expression evaluator. When the disabled value will contain
     * the value expression this evaluator will be used.
     * 
     * @param valueExpressionEvaluator {@link ValueExpressionEvaluator} instance.
     */
    public static synchronized void setValueExpressionEvaluator(ValueExpressionEvaluator valueExpressionEvaluator) {
        FlipAopContext.valueExpressionEvaluator = valueExpressionEvaluator;
    }
    
}
