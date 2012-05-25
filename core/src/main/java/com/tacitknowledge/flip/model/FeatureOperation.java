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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * The operations enum used to process the rule conditions. 
 * 
 * @deprecated Use instead {@link FeatureCondition#context} to write conditions.
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
@XmlEnum
@Deprecated
public enum FeatureOperation {
    
    /**
     * Operation used to test for equality. 
     */
    @XmlEnumValue("eq")
    EQUALS(new CommonConditionBuilder("{left} eq {right}")),
    
    /**
     * Operation used to test to test if the left value is less than the right one. 
     */
    @XmlEnumValue("le")
    LESS_EQUALS(new CommonConditionBuilder("{left} le {right}")),
    
    /**
     * Operation used to test if the left value is greater than the right one. 
     */
    @XmlEnumValue("ge")
    GREATER_EQUALS(new CommonConditionBuilder("{left} ge {right}")),
    
    /**
     * Operation used to test if the left value is less or equal to the right one. 
     */
    @XmlEnumValue("lt")
    LESS(new CommonConditionBuilder("{left} lt {right}")),
    
    /**
     * Operation used to test if the left value is greater or equal to the right one. 
     */
    @XmlEnumValue("gt")
    GREATER(new CommonConditionBuilder("{left} gt {right}")),
    
    /**
     * Operation used to test is the values are unequal. 
     */
    @XmlEnumValue("ne")
    NOT_EQUALS(new CommonConditionBuilder("{left} ne {right}")),
    
    /**
     * Operation used to test if the left value matches the regular expression passed as right value. 
     */
    @XmlEnumValue("matches")
    MATCHES(new CommonConditionBuilder("{left} =~ {right}")),
    
    /**
     * Operation used to test if the left value do not matches the regular expression passed as right value. 
     */
    @XmlEnumValue("not-matches")
    NOT_MATCHES(new CommonConditionBuilder("{left} !~ {right}"));

    private ConditionBuilder builder;

    private FeatureOperation(final ConditionBuilder conditionBuilder)
    {
        this.builder = conditionBuilder;
    }
    
    /**
     * Method used to build an EL expression using this operation. The leftParam 
     * is considered always as a property name, but the right value is a constant. 
     * If in the rightParam is passed a string {@code "true"},{@code "false"}, 
     * {@code "null"} or a number then in the resulting expression it will not be quoted. 
     * 
     * @param leftParam the left parameter.
     * @param rightParam the right parameter. 
     * @return the EL expression of this operation.
     */
    public String buildCondition(String leftParam, String rightParam) {
        if (rightParam == null) {
            rightParam = "null";
        }
        if (!rightParam.matches("(true|false|\\d+|null)"))
        {
            rightParam = "\"" + rightParam + "\"";
        }
        if (leftParam == null)
        {
            leftParam = "null";
        }
        return builder.buildCondition(leftParam, rightParam);
    }

}
