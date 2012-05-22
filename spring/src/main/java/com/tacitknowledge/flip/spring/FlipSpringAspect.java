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
package com.tacitknowledge.flip.spring;

import com.tacitknowledge.flip.aspectj.Flippable;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.FlipContext;
import com.tacitknowledge.flip.aspectj.FlipAbstractAspect;
import com.tacitknowledge.flip.aspectj.FlipAopContext;
import com.tacitknowledge.flip.aspectj.ValueExpressionEvaluator;
import com.tacitknowledge.flip.aspectj.converters.Converter;
import com.tacitknowledge.flip.aspectj.converters.ConvertersHandler;
import com.tacitknowledge.flip.aspectj.converters.FallDownConvertersHandler;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * An aspect that intercepts all calls to controller's methods that are annotated with {@link Flippable} annotations.
 * Each of the methods are required to have a {@link RequestMapping} or {@link ModelAttribute} annotation 
 * in their signature. If feature identified by {@link Flippable#feature()} is disabled, the aspect will redirect to the 
 * {@link Flippable#disabledValue()}, or return <code>null</code>, in case of {@link ModelAttribute} or if method 
 * is additionally annotated with {@link ResponseBody}.
 * 
 * This object could be instantiated by Spring IoC container where could be injected
 * {@link FeatureService}, converters and {@link ValueExpressionEvaluator}.
 * By default is used SpEL value expression engine.
 * 
 * @author Ion Lenta (ilenta@tacitknowledge.com)
 * @author Petric Coroli (pcoroli@tacitknowledge.com)
 */
@Aspect
@Order(2)
@SuppressWarnings("unused")
public class FlipSpringAspect extends FlipAbstractAspect
{
    public static final String ASPECT_BEAN_NAME = "flipSpringAspect";
    public static final String FEATURE_SERVICE_BEAN_NAME = "featureService";
    public static final String FEATURE_SERVICE_FACTORY_BEAN_NAME = "featureServiceFactory";
    
    /**
     * Feature Service used to manage the features.
     */
    @Autowired(required=true)
    private FeatureService featureService;
    
    /**
     * Converter handler used to manage the converters.
     */
    private ConvertersHandler convertersHandler = new FallDownConvertersHandler(FlipAopContext.getConvertersHandler());
    
    /**
     * SpEL value expression engine used to evaluate the expressions.
     */
    private ValueExpressionEvaluator valueExpressionEvaluator = new SpElValueExpressionEvaluator();

    public void setFeatureService(FeatureService featureService) {
        this.featureService = featureService;
    }
    
    /** {@inheritDoc } */
    @Override
    public FeatureService getFeatureService() {
        return FlipContext.chooseFeatureService(featureService);
    }

    /** {@inheritDoc } */
    @Override
    public ConvertersHandler getConvertersHandler() {
        return convertersHandler;
    }

    /** {@inheritDoc } */
    @Override
    public ValueExpressionEvaluator getValueExpressionEvaluator() {
        if (valueExpressionEvaluator == null) {
            return super.getValueExpressionEvaluator();
        } else {
            return valueExpressionEvaluator;
        }
    }

    /**
     * Sets the array of converters used additionally to use.
     * 
     * @param convertersArray the array of converters.
     */
    public void setConverters(Converter[] convertersArray) {
        convertersHandler.addConverters(convertersArray);
    }

    /**
     * Sets the Value expression evaluator used to process the expressions.
     * 
     * @param valueExpressionEvaluator the instance of {@link ValueExpressionEvaluator} used 
     *  to process the expressions.
     */
    public void setValueExpressionEvaluator(ValueExpressionEvaluator valueExpressionEvaluator) {
        this.valueExpressionEvaluator = valueExpressionEvaluator;
    }

    /** A pointcut expression that matches all public methods */
    @Pointcut("execution(public * *(..))")
    protected void anyPublicMethod()
    {
        //no-op
    }

    /** A pointcut expression that matches all controller handler methods with {@link RequestMapping} annotation */
    @Pointcut("anyPublicMethod() && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    protected void anyControllerHandlerMethod()
    {
        //no-op
    }

    /** A pointcut expression that matches all model attribute methods with {@link ModelAttribute} annotation */
    @Pointcut("anyPublicMethod() && @annotation(org.springframework.web.bind.annotation.ModelAttribute)")
    protected void anyModelAttributeMethod()
    {
        //no-op
    }

    /** A pointcut expression that matches all controller handler methods with {@link ResponseBody} annotation */
    @Pointcut("anyControllerHandlerMethod() && @annotation(org.springframework.web.bind.annotation.ResponseBody)")
    protected void anyResponseBodyControllerHandlerMethod()
    {
        //no-op
    }

    /** A pointcut expression that matches all controller handler methods without {@link ResponseBody} annotation */
    @Pointcut("anyControllerHandlerMethod() && !@annotation(org.springframework.web.bind.annotation.ResponseBody)")
    protected void anyNonResponseBodyControllerHandlerMethod()
    {
        //no-op
    }

    @Pointcut("@within(com.tacitknowledge.flip.aspectj.Flippable) && anyNonResponseBodyControllerHandlerMethod()")
    public void anyHandlerFeatureCheckerWithinFlipType()
    {
    	//no-op
    }

    @Pointcut("@within(com.tacitknowledge.flip.aspectj.Flippable) && anyResponseBodyControllerHandlerMethod()")
    public void anyResponseBodyHandlerFeatureCheckerWithinFlipType()
    {
    	//no-op
    }

    @Pointcut("@annotation(com.tacitknowledge.flip.aspectj.Flippable) && anyNonResponseBodyControllerHandlerMethod()")
    public void anyHandlerFeatureChecker()
    {
    	//no-op
    }

    @Pointcut("@annotation(com.tacitknowledge.flip.aspectj.Flippable) && anyResponseBodyControllerHandlerMethod()")
    public void anyResponseBodyHandlerFeatureChecker()
    {
    	//no-op
    }

    @Pointcut("@annotation(com.tacitknowledge.flip.aspectj.Flippable) && anyModelAttributeMethod()")
    public void anyModelAttributeFeatureChecker()
    {
    	//no-op
    }
    
}
