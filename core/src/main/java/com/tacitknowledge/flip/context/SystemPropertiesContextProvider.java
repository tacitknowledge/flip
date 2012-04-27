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
package com.tacitknowledge.flip.context;

import java.util.Properties;

/**
 * The context provider used to retrieve the properties from system properties.
 * This class will return all of properties from {@link java.lang.System#getProperties()}.
 * To access these properties you will have to write the expression like this:
 * {@code system['user.home']}.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
@FlipContext(name = "system", priority = 100)
public class SystemPropertiesContextProvider
{
    
    /**
     * Returns the system properties which will be added to context as they are.
     * 
     * @return system properties.
     */
    @FlipContextProperty()
    public Properties getAllProperties()
    {
        return System.getProperties();
    }
}
