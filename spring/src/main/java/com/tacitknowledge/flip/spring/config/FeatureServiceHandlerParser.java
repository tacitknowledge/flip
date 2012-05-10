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

import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.tacitknowledge.flip.FeatureServiceDirectFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanNameReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class FeatureServiceHandlerParser extends AbstractBeanDefinitionParser {
    
    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder factoryBuilder = BeanDefinitionBuilder.rootBeanDefinition(FeatureServiceDirectFactory.class);
        RootBeanDefinition factoryBean = (RootBeanDefinition) factoryBuilder.getBeanDefinition();
        parserContext.getRegistry().registerBeanDefinition("featureServiceFactory", factoryBean);
        
        MutablePropertyValues factoryPropertyValues = new MutablePropertyValues();
        factoryBean.setPropertyValues(factoryPropertyValues);
        
        String environmentBean = element.getAttribute("environment");
        if (environmentBean != null && !environmentBean.isEmpty()) {
            factoryPropertyValues.addPropertyValue("environment", new RuntimeBeanNameReference(environmentBean));
        }

        Element contextProvidersElement = DomUtils.getChildElementByTagName(element, "context-providers");
        if (contextProvidersElement != null) {
            List contextProvidersList = parserContext.getDelegate().parseListElement(contextProvidersElement, factoryBean);
            if (contextProvidersList != null && !contextProvidersList.isEmpty()) {
                factoryPropertyValues.addPropertyValue("contextProviders", contextProvidersList);
            }
        }

        Element propertyReadersElement = DomUtils.getChildElementByTagName(element, "property-readers");
        if (propertyReadersElement != null && propertyReadersElement.hasChildNodes()) {
            List propertyReadersList = parserContext.getDelegate().parseListElement(propertyReadersElement, factoryBean);
            if (propertyReadersList != null && !propertyReadersList.isEmpty()) {
                factoryPropertyValues.addPropertyValue("propertyReaders", propertyReadersList);
            }
        }
        
        Element propertiesElement = DomUtils.getChildElementByTagName(element, "properties");
        if (propertiesElement != null && propertiesElement.hasChildNodes()) {
            Properties properties = parserContext.getDelegate().parsePropsElement(propertiesElement);
            if (properties != null && !properties.isEmpty()) {
                factoryPropertyValues.addPropertyValue("properties", properties);
            }
        }
        
        BeanDefinitionBuilder featureServiceBuilder = BeanDefinitionBuilder.genericBeanDefinition();
        BeanDefinition featureServiceRawBean = featureServiceBuilder.getRawBeanDefinition();
        featureServiceRawBean.setFactoryBeanName("featureServiceFactory");
        featureServiceRawBean.setFactoryMethodName("createFeatureService");
        parserContext.getRegistry().registerBeanDefinition("featureService", featureServiceBuilder.getBeanDefinition());
        
        return null;
    }
    
}
