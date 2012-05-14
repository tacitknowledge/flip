/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tacitknowledge.testweb;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.FlipContext;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author ssoloviov
 */
public class FeatureServiceBeanSetter implements InitializingBean {

    private FeatureService featureService;

    public FeatureService getFeatureService() {
        return featureService;
    }

    public void setFeatureService(FeatureService featureService) {
        this.featureService = featureService;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        FlipContext.setFeatureService(featureService);        
    }
    
}
