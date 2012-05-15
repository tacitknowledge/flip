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
package com.tacitknowledge.flip.spring;

import com.tacitknowledge.flip.aspectj.ValueExpressionEvaluator;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 *
 * @author ssoloviov
 */
class SpElValueExpressionEvaluator implements ValueExpressionEvaluator {

    public Object evaluate(Object context, String expression) {
        final ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(expression).getValue(context);
    }
    
}
