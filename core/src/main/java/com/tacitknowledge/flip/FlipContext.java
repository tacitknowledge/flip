/*
 * Copyright 2012 Tacit Knowledge.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tacitknowledge.flip;

/**
 *
 * @author ssoloviov
 */
public class FlipContext {
    
    private static FeatureService featureService;
    
    /**
     * Returns the {@link FeatureService}. This method by default is used by {@link JspFlipTag} to
     * obtain the default feature service. The value of this property should be set by developer 
     * on application start-up time.
     * 
     * @return {@link FeatureService} set by developer.
     */
    public static FeatureService getFeatureService() {
        return featureService;
    }
    
    /**
     * Sets the {@link FeatureService} to be used lately in the application. On
     * application start-up time the developer should set this value to be used lately
     * by application.
     * 
     * @param featureServiceObj the {@link FeatureService} object.
     */
    public static synchronized void setFeatureService(FeatureService value) {
        featureService = value;
    }
    
    public static FeatureService chooseFeatureService(FeatureService... services) {
        if (services != null) {
            for(FeatureService service : services) {
                if (service != null) {
                    return service;
                }
            }
        }
        return featureService;
    }
    
}
