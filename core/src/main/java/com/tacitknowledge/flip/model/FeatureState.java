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
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * The feature state enum. There are only two possible options: {@code ENABLED} and {@code DISABLED}. 
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
@XmlEnum(String.class)
@XmlAccessorType(XmlAccessType.FIELD)
public enum FeatureState {
    
    /**
     * The enabled state of the feature 
     */
    @XmlEnumValue("enabled")
    ENABLED(true),
    
    /**
     * The disabled state of the feature. 
     */
    @XmlEnumValue("disabled")
    DISABLED(false);

    private boolean state;

    private FeatureState(final boolean state)
    {
        this.state = state;
    }

    /**
     * Returns the state as boolean value. 
     * 
     * @return {@code "true"} when the state is {@link #ENABLED} and 
     * {@code "false"} when it is {@link #DISABLED}.
     */
    public boolean state() {
        return state;
    }
    
    /**
     * Finds the feature state by boolean value. 
     * 
     * @param state the state as boolean value. 
     * 
     * @return {@link #ENABLED} when {@code "true"} is passed and {@link DISABLED} 
     * when {@code "false"} is passed.
     */
    public static FeatureState getByState(boolean state) {
        return state ? ENABLED : DISABLED;
    }

}
