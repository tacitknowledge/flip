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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import com.tacitknowledge.flip.context.ContextManager;

/**
 * The feature rule model class. The object of this class holds the state to be 
 * returned when the conditions match. To see more info how the rules are processed 
 * see {@link #process(com.tacitknowledge.flip.context.ContextManager)}.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FeatureRule implements FeatureProcessor
{

    @XmlAttribute(required = true)
    private FeatureState state;

    @XmlElements(value=@XmlElement(name = "condition", namespace=FeatureDescriptors.NAMESPACE))
    private FeatureCondition[] conditions;

    /**
     * Returns the conditions of this rule. 
     * 
     * @return the array of conditions.
     */
    public FeatureCondition[] getConditions() {
        return conditions;
    }

    /**
     * Set the array of nested conditions. 
     * 
     * @param conditions conditions.
     */
    public void setConditions(FeatureCondition[] conditions) {
        this.conditions = conditions;
    }

    /**
     * Returns the rule state when conditions match. 
     * 
     * @return the state to return if conditions match.
     */
    public FeatureState getState() {
        return state;
    }

    /**
     * Sets the state of the rule when the conditions match. 
     * 
     * @param state the state to return if conditions match.
     */
    public void setState(FeatureState state) {
        this.state = state;
    }

    /**
     * Processes the rule accordingly to the context. If there are conditions 
     * nested then the conditions are processed and if all of conditions returns 
     * {@link FeatureState#ENABLED} then the value of state property is returned. 
     * Otherwise will be returned {@code null}. 
     * 
     * @param contextManager the context manager used to process the value. 
     * @return the state of the rule if this rule matches.
     */
    @Override
    public FeatureState process(final ContextManager contextManager)
    {
        if (conditions != null)
        {
            for (final FeatureCondition condition : conditions)
            {
                if (condition.process(contextManager) != FeatureState.ENABLED)
                {
                    return null;
                }
            }
        }
        return state;
    }

}
