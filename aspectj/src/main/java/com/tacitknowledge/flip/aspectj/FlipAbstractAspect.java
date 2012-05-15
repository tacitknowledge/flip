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

    private static final Pattern VALUE_EXPRESSION_REGEX = Pattern.compile("\\$\\{([^}]+)\\}");
    
    /**
     * Returns the {@link FeatureService} object used to calculate the features.
     * By default it returns the object set in {@link FlipContext}.
     * 
     * @return {@link FeatureService} object.
     */
    public FeatureService getFeatureService() {
        return FlipContext.chooseFeatureService();
    }
    
    public Converter getConverter(Class klass) {
        return FlipAopContext.getConverter(klass);
    }
    
    public ValueExpressionEvaluator getValueExpressionEvaluator() {
        return FlipAopContext.getValueExpressionEvaluator();
    }
    
    /**
     * A pointcut declaration which marks all methods in the class.
     */
    @Pointcut("execution(* *(..))")
    public void anyMethod() {}
    
    /**
     * The around advice used to intercept the methods marked with the
     * {@link Flippable} annotation.
     */
    @Around(value="anyMethod() && @annotation(flip)", argNames="flip")
    public Object aroundFlippableMethods(ProceedingJoinPoint pjp, Flippable flip) throws Throwable {
        return interceptMethod(flip, pjp);
    }
    
    @Around(value="anyMethod() && @within(flip)", argNames="flip")
    public Object aroundFlippableClass(ProceedingJoinPoint pjp, Flippable flip) throws Throwable {
        return interceptMethod(flip, pjp);
    }

    private Object interceptMethod(Flippable flip, ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Object[] params = pjp.getArgs();
        Class[] paramTypes = method.getParameterTypes();
        
        for(int i=0;i<paramAnnotations.length;i++) {
            FlipParam flipParam = findFlipParamAnnoattion(paramAnnotations[i]);
            if (!isFeatureEnabledOrHasNotValue(flipParam.feature())) {
                params[i] = getProcessedDisabledValue(paramTypes[i], flipParam.disabledValue(), pjp.getThis());
            }
        }
        
        if (isFeatureEnabledOrHasNotValue(flip.feature())) {
            return pjp.proceed(params);
        } else {
            return getProcessedDisabledValue(method.getReturnType(), flip.disabledValue(), pjp.getThis());
        }
    }
    
    private boolean isFeatureEnabledOrHasNotValue(String feature) {
        return feature == null ||
                feature.isEmpty() 
                || getFeatureService().getFeatureState(feature) == FeatureState.ENABLED;
    }
    
    private FlipParam findFlipParamAnnoattion(Annotation[] annotations) {
        for(Annotation annotation : annotations) {
            if (FlipParam.class.isAssignableFrom(annotation.annotationType())) {
                return (FlipParam) annotation;
            }
        }
        return null;
    }
    
    private Object getProcessedDisabledValue(Class outputClass, String value, Object context) {
        Matcher m = VALUE_EXPRESSION_REGEX.matcher(value);
        if (m.find()) {
            ValueExpressionEvaluator evaluator = getValueExpressionEvaluator();
            if (evaluator == null) {
                return getUntouchedDisabledValue(outputClass, value);
            }
            
            return evaluator.evaluate(context, m.group(1));
        } else {
            Converter converter = getConverter(outputClass);
            if (converter == null) {
                return getUntouchedDisabledValue(outputClass, value);
            }
            
            return converter.convert(value, outputClass);
        }
    }
    
    private Object getUntouchedDisabledValue(Class outputClass, String value) {
        if (CharSequence.class.isAssignableFrom(outputClass)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Cannot find converter for class [%s].", outputClass.getName()));
        }
    }
    
}
