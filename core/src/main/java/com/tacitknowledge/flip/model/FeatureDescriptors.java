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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Class describing feature descriptors objects. Holds an 
 * array of {@link FeatureDescriptor}s.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
@XmlRootElement(name = "features", namespace=FeatureDescriptors.NAMESPACE)
@XmlAccessorType(value = XmlAccessType.FIELD)
public class FeatureDescriptors {
    
    public static final String NAMESPACE = "http://www.tacitknowledge.com/flip";
    
    @XmlElements(value =
    @XmlElement(name = "feature", type = FeatureDescriptor.class, namespace=NAMESPACE))
    private FeatureDescriptor[] features;

    public FeatureDescriptor[] getFeatures() {
        return features;
    }

    public void setFeatures(final FeatureDescriptor[] features) {
        this.features = features;
    }
    
}
