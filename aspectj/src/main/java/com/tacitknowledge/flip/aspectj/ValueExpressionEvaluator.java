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

/**
 * The interface adapter used to evaluate the value expressions.
 * 
 * @author ssoloviov
 */
public interface ValueExpressionEvaluator {
    
    /**
     * Evaluates the value expressions. The implementors could use his own
     * expression evaluators. By default is used {@link JexlValueExpressionEvaluator}.
     * 
     * @param context the context used to evaluate the expression.
     * @param expression the expression to evaluate.
     * @return the object obtained after evaluating the expression.
     */
    public Object evaluate(Object context, String expression);
    
}
