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
package com.tacitknowledge.flip.properties;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The abstract class which refreshes the cache periodically. If the property 
 * with the refresh period is not specified or has an invalid value it will 
 * never refresh. 
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public abstract class RefreshablePropertyReader extends CacheablePropertyReader
{

    /**
     * The name of property which holds the refresh period in seconds. 
     */
    public static final String CONFIG_REREAD_INTERVAL = "flip.properties.reread";

    private final Timer timer = new Timer(true);

    /**
     * {@inheritDoc }
     */
    @Override
    public void initialize(final Properties config)
    {
        super.initialize(config);
        applyTimer();
    }

    private void applyTimer()
    {
        try
        {
            final String intervalString = getConfig().getProperty(CONFIG_REREAD_INTERVAL);
            final int interval = Integer.parseInt(intervalString) * 1000;

            timer.scheduleAtFixedRate(new RereadPropertiesTimer(), interval, interval);
        }
        catch (final NumberFormatException ex)
        {
            Logger.getLogger(XmlPropertyReader.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    private class RereadPropertiesTimer extends TimerTask
    {

        public RereadPropertiesTimer()
        {}

        @Override
        public void run()
        {
            readDescriptors();
        }
    }

}
