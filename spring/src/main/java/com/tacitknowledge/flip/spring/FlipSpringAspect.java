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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.model.FeatureState;
import com.tacitknowledge.flip.spring.FlipParam;
import com.tacitknowledge.flip.spring.FlipParameters;
import com.tacitknowledge.flip.spring.FlipSpringHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * An aspect that intercepts all calls to controller's methods that are annotated with {@link Flip} annotations.
 * Each of the methods are required to have a {@link RequestMapping} or {@link ModelAttribute} annotation 
 * in their signature. If feature identified by {@link Flip#feature()} is disabled, the aspect will redirect to the 
 * {@link Flip#disabledUrl()}, or return <code>null</code>, in case of {@link ModelAttribute} or if method 
 * is additionally annotated with {@link ResponseBody}.
 * 
 * @author Ion Lenta (ilenta@tacitknowledge.com)
 * @author Petric Coroli (pcoroli@tacitknowledge.com)
 */
@Aspect
@Order(2)
@SuppressWarnings("unused")
public class FlipSpringAspect
{
    private static final String FLIP_ARGNAME = "flip";
    
    @Autowired
    private FeatureService featureService;
    
    //@Value("#{systemProperties.flip.web.defaultDisabledUrl}")
    private String defaultFlipDisabledUrl;

    public FeatureService getFeatureService() {
        return featureService;
    }

    public void setFeatureService(FeatureService featureService) {
        this.featureService = featureService;
    }

    public String getDefaultFlipDisabledUrl() {
        return defaultFlipDisabledUrl;
    }

    public void setDefaultFlipDisabledUrl(String defaultFlipDisabledUrl) {
        this.defaultFlipDisabledUrl = defaultFlipDisabledUrl;
    }

    /**
     * The advice is executed before and after a controller handler whose class is annotated with
     * the {@link Flip} annotation
     * @param pjp gives access to the actual object being called
     * @param flip the annotation placed on type
     * @return If feature identified by <code>flip.feature()</code> is disabled, 
     *      the aspect will be redirected to the <code>flip.disabledUrl()</code>.
     * @throws Throwable if any exceptions occurred 
     */
    public Object aroundHandlerFeatureCheckerWithinFlipType(final ProceedingJoinPoint pjp)
        throws Throwable
    {
        final FlipSpringHandler flip = getMethodAnnotation(pjp, FlipSpringHandler.class);
        return processAroundHandlerForFeature(pjp, flip.feature(), flip.disabledUrl() == null ? defaultFlipDisabledUrl : flip.disabledUrl());
    }

    /**
     * The advice is executed before and after a controller handler whose class is annotated with
     * the {@link Flip} annotation
     * @param pjp gives access to the actual object being called
     * @param flip the annotation placed on type
     * @return If feature identified by <code>flip.feature()</code> is disabled,
     *      the aspect will return <code>null</code>.
     * @throws Throwable if any exceptions occurred 
     */
    public Object aroundResponseBodyHandlerFeatureCheckerWithinFlipType(final ProceedingJoinPoint pjp)
        throws Throwable
    {
        final FlipSpringHandler flip = getMethodAnnotation(pjp, FlipSpringHandler.class);
        return processAroundHandlerForFeature(pjp, flip.feature(), null);
    }

    /**
     * The advice is executed before and after a controller handler that is annotated with
     * the {@link Flip} annotation
     * @param pjp gives access to the actual object being called
     * @param flip the annotation placed on the method
     * @return If feature identified by <code>flip.feature()</code> is disabled, 
     *      the aspect will be redirected to the <code>flip.disabledUrl()</code>.
     * @throws Throwable if any exceptions occurred 
     */
    public Object aroundHandlerFeatureChecker(final ProceedingJoinPoint pjp) throws Throwable
    {
        final FlipSpringHandler flip = getMethodAnnotation(pjp, FlipSpringHandler.class);
        return processAroundHandlerForFeature(pjp, flip.feature(), flip.disabledUrl() == null ? defaultFlipDisabledUrl : flip.disabledUrl());
    }

    /**
     * The advice is executed before and after a controller handler that is annotated with
     * the {@link Flip} annotation
     * @param pjp gives access to the actual object being called
     * @param flip the annotation placed on the method
     * @return If feature identified by <code>flip.feature()</code> is disabled,
     *      the aspect will return <code>null</code>.
     * @throws Throwable if any exceptions occurred 
     */
    public Object aroundResponseBodyHandlerFeatureChecker(final ProceedingJoinPoint pjp)
        throws Throwable
    {
        final FlipSpringHandler flip = getMethodAnnotation(pjp, FlipSpringHandler.class);
        return processAroundHandlerForFeature(pjp, flip.feature(), null);
    }

    /**
     * The advice is executed before and after a controller's model attribute method that is annotated with
     * the {@link Flip} annotation
     * @param pjp gives access to the actual object being called
     * @param flip the annotation placed on the method
     * @return If feature identified by <code>flip.feature()</code> is disabled, 
     *      the aspect will return <code>null</code>.
     * @throws Throwable if any exceptions occurred 
     */
    public Object aroundModelAttributeFeatureChecker(final ProceedingJoinPoint pjp) throws Throwable
    {
        final FlipSpringHandler flip =  getMethodAnnotation(pjp, FlipSpringHandler.class);
        return processAroundHandlerForFeature(pjp, flip.feature(), null);
    }

    /**
     * The advice is executed before and after a method invocation, that is annotated with 
     * {@link FlipParameters} annotation. All of the method's parameters marked with {@link FlipParam} annotation
     * are taken through the process of feature toggling, replacing the value of the original parameter with that of
     * {@link FlipParam#disabledValue()}, in case feature identified by {@link FlipParam#feature()} is disabled.
     * Additionally, if {@link FlipParam#asSpEL()} is set to <code>true</code>, the value of 
     * {@link FlipParam#disabledValue()} is considered to be a SpEL expression that gets evaluated respectively,
     * by the means of 
     * <a href="http://static.springsource.org/spring/docs/3.0.5.RELEASE/reference/expressions.html">SpEL</a>
     * 
     * @param pjp gives access to the actual object being called
     * @param flip the annotation placed on a method
     * @return value from the actual method that is being woven
     * @throws Throwable if any exceptions occurred
     */
    public Object aroundMethodFeatureCheckerWithFlipParameters(final ProceedingJoinPoint pjp)
        throws Throwable
    {
        final MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        final Method method = methodSignature.getMethod();

        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] pjpArgs = pjp.getArgs();

        int paramIndex = 0;
        for (final Annotation[] annotations : parameterAnnotations)
        {
            final Class<?> parameterType = parameterTypes[paramIndex];

            for (final Annotation annotation : annotations)
            {
                if (annotation instanceof FlipParam)
                {
                    processFlipParamAnnotation(pjpArgs, paramIndex, parameterType, (FlipParam) annotation);
                }
            }
            paramIndex++;
        }

        return pjp.proceed(pjpArgs);
    }

    /** A pointcut expression that matches all methods */
    @Pointcut("execution(* *(..))")
    protected void anyMethod()
    {
        //no-op
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

    @Pointcut("@within(com.tacitknowledge.flip.spring.FlipSpringHandler) && anyNonResponseBodyControllerHandlerMethod()")
    public void anyHandlerFeatureCheckerWithinFlipType()
    {
    	//no-op
    }

    @Pointcut("@within(com.tacitknowledge.flip.spring.FlipSpringHandler) && anyResponseBodyControllerHandlerMethod()")
    public void anyResponseBodyHandlerFeatureCheckerWithinFlipType()
    {
    	//no-op
    }

    @Pointcut("@annotation(com.tacitknowledge.flip.spring.FlipSpringHandler) && anyNonResponseBodyControllerHandlerMethod()")
    public void anyHandlerFeatureChecker()
    {
    	//no-op
    }

    @Pointcut("@annotation(com.tacitknowledge.flip.spring.FlipSpringHandler) && anyResponseBodyControllerHandlerMethod()")
    public void anyResponseBodyHandlerFeatureChecker()
    {
    	//no-op
    }

    @Pointcut("@annotation(com.tacitknowledge.flip.spring.FlipSpringHandler) && anyModelAttributeMethod()")
    public void anyModelAttributeFeatureChecker()
    {
    	//no-op
    }

    @Pointcut("anyMethod() && @annotation(com.tacitknowledge.flip.spring.FlipParameters)")
    public void anyMethodFeatureCheckerWithFlipParameters()
    {
    	//no-op
    }

    protected <T extends Annotation> T getMethodAnnotation(final ProceedingJoinPoint pjp, final Class<T> annotation)
    {
        return ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(annotation);
    }
    
    private Object processAroundHandlerForFeature(final ProceedingJoinPoint pjp, final String feature,
            final Object disabledResult) throws Throwable
    {
        return getFeatureService().getFeatureState(feature) == FeatureState.ENABLED ? pjp.proceed() : disabledResult;
    }

    private void processFlipParamAnnotation(final Object[] pjpArgs, final int paramIndex, final Class<?> parameterType,
            final FlipParam flip)
    {
        if (getFeatureService().getFeatureState(flip.feature()) == FeatureState.DISABLED)
        {
            Object newValue = flip.disabledValue();
            if (flip.asSpEL())
            {
                final ExpressionParser parser = new SpelExpressionParser();
                newValue = parser.parseExpression(flip.disabledValue()).getValue(parameterType);
            }
            pjpArgs[paramIndex] = newValue;
        }
    }
}
