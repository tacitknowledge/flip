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
package com.tacitknowledge.flip.model;

import java.util.regex.Pattern;

/**
 * Common class used to build the EL expression by a string template. This template 
 * should mark the left parameter as {@code "{left}"}. The right parameter should mark 
 * as {@code "{right}"} in the string passed to constructor. 
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class CommonConditionBuilder implements ConditionBuilder
{

    private final String expression;

    /**
     * Constructs the condition builder by the template.
     * 
     * @param expression the template used to build the EL expression.
     */
    public CommonConditionBuilder(String expression) {
        this.expression = expression;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String buildCondition(final String leftParam, final String rightParam)
    {
        final String result = expression.replaceAll(Pattern.quote("{left}"), String.valueOf(leftParam)).replaceAll(
                Pattern.quote("{right}"), String.valueOf(rightParam));
        return result;
    }

}
