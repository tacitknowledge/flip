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

import com.tacitknowledge.flip.spring.FlipSpringAspect;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.RuntimeBeanNameReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Element;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class InterceptHandlerParser extends AbstractBeanDefinitionParser {

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("com/tacitknowledge/flip/spring/context.xml"));
        
        BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder.rootBeanDefinition(FlipSpringAspect.class);
        String defaultUrlValue = element.getAttribute("default-url");
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue("disabledUrl", defaultUrlValue);
        propertyValues.addPropertyValue("featureService", new RuntimeBeanReference("featureService"));
        beanBuilder.getRawBeanDefinition().setPropertyValues(propertyValues);
        
        for(String name : factory.getBeanDefinitionNames()) {
            parserContext.getRegistry().registerBeanDefinition(name, factory.getBeanDefinition(name));
        }
        parserContext.getRegistry().registerBeanDefinition("flipHandlerAspect", beanBuilder.getBeanDefinition());
        
        return null;
    }
    
}
