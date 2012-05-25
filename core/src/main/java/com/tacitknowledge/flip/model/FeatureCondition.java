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

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

import com.tacitknowledge.flip.context.ContextManager;
import com.tacitknowledge.flip.context.ContextMap;

/**
 * The object which holds the rule condition information. 
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FeatureCondition implements FeatureProcessor
{

    @XmlAttribute()
    private String context = ContextMap.GLOBAL;

    @XmlAttribute
    private String name;

    @XmlAttribute()
    private FeatureOperation operation = FeatureOperation.EQUALS;

    @XmlAttribute
    private String value;
    
    @XmlValue
    private String expression;

    /**
     * Returns the context used to process the condition. If the context is equal
     * to {@code "_all"} then the property {@link #getName() name} will be looked up in all context available. 
     * 
     * @return the name of context to use. By default is used {@code "_all"}.
     */
    public String getContext() {
        return context;
    }

    /**
     * Sets the name of context to use on processing the state. 
     * 
     * @param context the context name or {@code "_all"} if to use all of them.
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * Returns the name of property from {@link #getContext() context} to use in the condition. 
     * 
     * @return the name of property to test.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of property to use in the condition and which should be present in the {@link #getContext() context}.
     * 
     * @param name the name of property to test.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the operation used to test the property. 
     * 
     * @return the operation applied to test the property.
     */
    public FeatureOperation getOperation() {
        return operation;
    }

    /**
     * Sets the operation used to test the property. 
     * 
     * @param operation the operation used to perocess the condition.
     */
    public void setOperation(FeatureOperation operation) {
        this.operation = operation;
    }

    /**
     * Returns the value which is used as a right parameter in the operation {@link #getOperation() operation}. 
     * 
     * @return the value used to test the property.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value used to process the condition. 
     * 
     * @param value the value used to test the property.
     */
    public void setValue(String value) {
        this.value = value;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * Processes the condition accordingly with the context manager used. This 
     * method builds the expression from the options passed so that the property 
     * {@link #getName() name} from the {@link #getContext() context} will be 
     * compared with the {@link #getValue() value} using the {@link #getOperation() operation}.
     * 
     * @param contextManager the context manager used to process the condition.
     * @return the {@link FeatureState#ENABLED} if the condition matches and 
     * {@link FeatureState#DISABLED} otherwise.
     */
    @Override
    public FeatureState process(final ContextManager contextManager)
    {
        final JexlEngine jexlEngine = new JexlEngine();
        Expression jexlExpression;
        if (expression != null) {
            jexlExpression = jexlEngine.createExpression(expression);
        } else {
            jexlExpression = jexlEngine.createExpression(operation.buildCondition(name, value));
            
        }
        final Map<String, Object> contextMap = contextManager.getContext(context);
        final JexlContext jexlContext = new MapContext(contextMap);

        return Boolean.TRUE.equals(jexlExpression.evaluate(jexlContext)) ? FeatureState.ENABLED : FeatureState.DISABLED;
    }
}
