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

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.ObjectContext;

/**
 * This is default value expression evaluator. This evaluator uses Jexl to evaluate 
 * the expressions.
 * 
 * @author ssoloviov
 */
public class JexlValueExpressionEvaluator implements ValueExpressionEvaluator {

    private JexlEngine engine = new JexlEngine();
    
    /**
     * {@inheritDoc }
     */
    public Object evaluate(Object context, String expression) {
        Expression jexlExpression = engine.createExpression(expression);
        JexlContext jexlContext = new ObjectContext(engine, context);
        
        return jexlExpression.evaluate(jexlContext);
    }
    
}
