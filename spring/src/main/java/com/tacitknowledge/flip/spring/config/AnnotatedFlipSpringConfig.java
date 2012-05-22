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
package com.tacitknowledge.flip.spring.config;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.FeatureServiceDirectFactory;
import com.tacitknowledge.flip.spring.FlipSpringAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Spring annotation based configuration used to activate flip.
 * This spring configuration should be imported by your application configuration.
 * This configuration do not created the aspect. This should be done in your configuration
 * invoking {@link #createFlipSpringAspect(java.lang.String) } or 
 * {@link #createFlipSpringAspect(com.tacitknowledge.flip.FeatureService, java.lang.String) }
 * methods.
 * To create {@link FeatureService} instance you should create the 
 * {@link FeatureServiceDirectFactory} configured bean.
 * By default the {@link FeatureServiceDirectFactory} bean should be named as
 * "featureServiceFactory". The {@link FlipSpringAspect} bean should be named
 * as "flipSptingAspect".
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
@Configuration
@ImportResource("classpath:com/tacitknowledge/flip/spring/context.xml")
public class AnnotatedFlipSpringConfig {
    
    @Autowired
    private FeatureServiceDirectFactory factory;

    /**
     * Creates the Flip Aspect with defined {@link FeatureService} and defaultUrl.
     * 
     * @param featureService the {@link FeatureService} to inject into aspect.
     * @param defaultUrl the default url to use.
     * @return the {@link FlipSpringAspect}.
     */
    public static FlipSpringAspect createFlipSpringAspect(FeatureService featureService, String defaultUrl) {
        FlipSpringAspect aspect = new FlipSpringAspect();
        aspect.setFeatureService(featureService);
        aspect.setDefaultValue(defaultUrl);
        return aspect;
    }

    /**
     * Creates the Flip Aspect with defined defaultUrl only.
     * 
     * @param defaultUrl the default url to use.
     * @return the {@link FlipSpringAspect}.
     */
    public static FlipSpringAspect createFlipSpringAspect(String defaultUrl) {
        FlipSpringAspect aspect = new FlipSpringAspect();
        aspect.setDefaultValue(defaultUrl);
        return aspect;
    }
    
    /**
     * Creates a {@link FeatureService} bean. It is created using bean of type
     * {@link FeatureServiceDirectFactory} named as "featureServiceFactory".
     * 
     * @return the {@link FeatureService}.
     */
    @Bean(name=FlipSpringAspect.FEATURE_SERVICE_BEAN_NAME)
    public FeatureService featureService() {
        return factory.createFeatureService();
    }
    
}
