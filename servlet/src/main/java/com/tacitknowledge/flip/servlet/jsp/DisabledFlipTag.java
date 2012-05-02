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
package com.tacitknowledge.flip.servlet.jsp;

import com.tacitknowledge.flip.model.FeatureState;

/**
 * JSP tag to display the contents of the feature if its state is {@link FeatureState#DISABLED}.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class DisabledFlipTag extends JspFlipTag
{
    /**
     * Constructs the tag with its state as {@link FeatureState#DISABLED}.
     */
    public DisabledFlipTag()
    {
        super(FeatureState.DISABLED);
    }
}
