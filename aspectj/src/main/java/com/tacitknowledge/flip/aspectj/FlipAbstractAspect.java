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
package com.tacitknowledge.flip.aspectj;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.FlipContext;
import com.tacitknowledge.flip.aspectj.converters.Converter;
import com.tacitknowledge.flip.aspectj.converters.ConvertersHandler;
import com.tacitknowledge.flip.model.FeatureState;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * The base abstract Aspect class which applies the interception of all
 * methods marked with {@link Flippable} annotation. If the method is marked with
 * this annotation will act in the feature toggling. If the feature declared in the
 * annotation is disabled the {@link Flippable#disabledValue() } will be applied.
 * If the class itself is marked with {@link Flippable} annotation, then all
 * methods will act in the feature toggling. Also this aspect allows overriding 
 * the methods parameters which should be marked with {@link FlipParam} annotation.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
@Aspect
public abstract class FlipAbstractAspect {

    /**
     * The prepared regular expression to extract value expression from a string.
     */
    private static final Pattern VALUE_EXPRESSION_REGEX = Pattern.compile("\\$\\{([^}]+)\\}");
    
    /**
     * The default value used if {@link Flippable} annotation has not declared the
     * {@link Flippable#disabledValue() }.
     */
    private String defaultValue;
    
    /**
     * Returns the {@link FeatureService} object used to calculate the features.
     * By default it returns the object set in {@link FlipContext}.
     * 
     * @return {@link FeatureService} object.
     */
    public FeatureService getFeatureService() {
        return FlipContext.chooseFeatureService();
    }

    /**
     * Returns the {@link ConvertersHandler} instance used to manage converters.
     * If the value of {@link Flippable#disabledValue() } or {@link FlipParam#disabledValue() }
     * properties do not contains a value expression the converter is used to
     * obtain the required object.
     * 
     * @return {@link ConvertersHandler} instance.
     */
    public ConvertersHandler getConvertersHandler() {
        return FlipAopContext.getConvertersHandler();
    }
    
    /**
     * Returns the value expressions evaluator. This evaluator is used to evaluate
     * expressions used in {@link Flippable#disabledValue() } or {@link FlipParam#disabledValue() }.
     * If these properties contains an expression like this <code>${var}</code>
     * this value expression evaluator will be used.
     * 
     * @return {@link ValueExpressionEvaluator} instance.
     */
    public ValueExpressionEvaluator getValueExpressionEvaluator() {
        return FlipAopContext.getValueExpressionEvaluator();
    }

    /**
     * Returns the default value if there is no {@link Flippable#disabledValue() } specified.
     * 
     * @return the default disabled value.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the default disabled value. This value will be used if no {@link Flippable#disabledValue() } is
     * specified in the annotation of the method.
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    /**
     * A pointcut declaration which marks all methods in the class.
     */
    @Pointcut("execution(* *(..))")
    public void anyMethod() {}
    
    /**
     * Intercept calls to the methods marked with {@link Flippable} annotation.
     * Firstly it processes the method parameters marked with {@link FlipParam} 
     * annotation. Each parameter value is replaced with the value declared in
     * {@link FlipParam#disabledValue()} if the feature declared in {@link FlipParam#feature() }
     * is disabled.
     * After properties evaluation this method checks if the feature marked in
     * {@link Flippable#feature() } is disabled then the value from {@link Flippable#disabledValue() }
     * is returned.
     * 
     * @param flip the {@link Flippable} annotation instance which marks the method to intercept.
     * @param pjp the object obtained from AspectJ method interceptor.
     * @return the processed value of the method depending from feature state.
     * @throws Throwable 
     */
    @Around(value="anyMethod() && @annotation(flip)", argNames="flip")
    public Object aroundFlippableMethods(ProceedingJoinPoint pjp, Flippable flip) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        Method method = methodSignature.getMethod();
        
        if (isFeatureEnabled(flip.feature())) {
            Annotation[][] paramAnnotations = method.getParameterAnnotations();
            Object[] params = pjp.getArgs();
            Class[] paramTypes = method.getParameterTypes();

            for(int i=0;i<paramAnnotations.length;i++) {
                FlipParam flipParam = findFlipParamAnnoattion(paramAnnotations[i]);
                if (!isFeatureEnabled(flipParam.feature())) {
                    params[i] = getProcessedDisabledValue(paramTypes[i], flipParam.disabledValue(), pjp.getThis());
                }
            }

            return pjp.proceed(params);
        } else {
            return getProcessedDisabledValue(method.getReturnType(), flip.disabledValue(), pjp.getThis());
        }
    }
    
    public Object aroundFlippableMethods(ProceedingJoinPoint pjp) throws Throwable {
        final Flippable flip = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(Flippable.class);
        return aroundFlippableMethods(pjp, flip);
    }
    
    
    /**
     * Returns if the feature is enabled. If the feature is empty then the feature
     * is considered enabled.
     * 
     * @param feature the name of the feature to test.
     * @return true if the feature is enabled or the feature name is empty.
     */
    private boolean isFeatureEnabled(String feature) {
        return feature == null ||
                feature.isEmpty() 
                || getFeatureService().getFeatureState(feature) == FeatureState.ENABLED;
    }
    
    /**
     * Finds the {@link FlipParam} annotation instance from a list of annotations.
     * 
     * @param annotations the list of annotation which marks the parameter.
     * @return the {@link FlipParam} instance if found, otherwise null.
     */
    private FlipParam findFlipParamAnnoattion(Annotation[] annotations) {
        for(Annotation annotation : annotations) {
            if (FlipParam.class.isAssignableFrom(annotation.annotationType())) {
                return (FlipParam) annotation;
            }
        }
        return null;
    }
    
    /**
     * Processes the disabled value used. If the value contains the <code>${}</code>
     * statement then this statement is processed using {@link #getValueExpressionEvaluator() }, 
     * otherwise it is used converter found by {@link #getConvertersHandler() }. 
     * The converter is found by ouputClass parameter. If no converter is found and 
     * the outputClass is not a {@link CharSequence} then {@link IllegalArgumentException}
     * is thrown.
     * 
     * @param outputClass the required object type
     * @param value the disabled value to evaluate.
     * @param context the object of the method intercepted. This object is used as context to
     *  evaluate value expression.
     * 
     * @return the evaluated value
     * @throws IllegalArgumentException if cannot find the converter.
     */
    private Object getProcessedDisabledValue(Class outputClass, String value, Object context) {
        if (value == null || value.isEmpty()) {
            value = defaultValue;
        }
        
        Matcher m = VALUE_EXPRESSION_REGEX.matcher(value);
        if (m.find()) {
            ValueExpressionEvaluator evaluator = getValueExpressionEvaluator();
            if (evaluator == null) {
                return getUntouchedDisabledValue(outputClass, value);
            }
            
            return evaluator.evaluate(context, m.group(1));
        } else {
            Converter converter = getConvertersHandler().getConverter(outputClass);
            if (converter == null) {
                return getUntouchedDisabledValue(outputClass, value);
            }
            
            return converter.convert(value, outputClass);
        }
    }
    
    /**
     * Returns the value if it cannot be converted or evaluated as value expression.
     * If the outputClass parameter is no {@link CharSequence} then the
     * {@link IllegalArgumentException} is thrown, otherwise the value itself is
     * returned.
     * 
     * @param outputClass the required output object type.
     * @param value the value.
     * @return the value string if is possible.
     * @throws IllegalArgumentException if cannot convert value parameter to outputClass.
     */
    private Object getUntouchedDisabledValue(Class outputClass, String value) {
        if (CharSequence.class.isAssignableFrom(outputClass)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Cannot find converter for class [%s].", outputClass.getName()));
        }
    }
    
}
