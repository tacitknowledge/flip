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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tacitknowledge.flip.context.ContextManager;

/**
 * The feature descriptor object. It holds the information to process the feature. 
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FeatureDescriptor implements FeatureProcessor
{

    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute()
    private FeatureState state;

    @XmlElements(@XmlElement(name = "rule", namespace=FeatureDescriptors.NAMESPACE))
    private FeatureRule[] rules;

    @XmlAttribute(name="expiration-date")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date expirationDate;
    
    /**
     * Returns the name of the feature. 
     * 
     * @return the name of the feature.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the feature. 
     * 
     * @param name the name of the feature.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the arrays of nested rules. 
     * 
     * @return array of rules.
     */
    public FeatureRule[] getRules() {
        return rules;
    }

    /**
     * Sets the rules of this feature to process. 
     * 
     * @param rules the array of rules.
     */
    public void setRules(FeatureRule[] rules) {
        this.rules = rules;
    }

    /**
     * Returns the overriding state of the feature. 
     * 
     * @return the overriding state.
     */
    public FeatureState getState() {
        return state;
    }

    /**
     * Sets the overriding state of the feature. If this state is set then the 
     * rules will not be processed and this state will be returned on feature 
     * descriptor processing. 
     * 
     * @param state the overriding state.
     */
    public void setState(FeatureState state) {
        this.state = state;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Processes the state of the feature. If this feature descriptor has set 
     * the overriding state then no rules will be processed and this state will 
     * be returned. If there are rules then the rules will be processed and the 
     * result of the first rule that matches will be returned as a state of the 
     * feature. If all of the rules do not matches then null will be returned. 
     * 
     * @param contextManager the context manager used to process the feature state. 
     * @return the state of the feature or null is it is impossible to calculate its state.
     */
    @Override
    public FeatureState process(final ContextManager contextManager)
    {
        if (expirationDate != null) {
            Logger.getLogger(this.getClass().getName()).warning(String.format("The feature [%s] is expired. The expiration date is %t", name, expirationDate));
        }
        
        if (state != null)
        {
            return state;
        }
        else
        {
            if (rules != null)
            {
                for (final FeatureRule rule : rules)
                {
                    final FeatureState ruleResult = rule.process(contextManager);
                    if (ruleResult != null)
                    {
                        return ruleResult;
                    }
                }
            }
        }

        return null;
    }
    
}
