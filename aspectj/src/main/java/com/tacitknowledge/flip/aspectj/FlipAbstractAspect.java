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

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.FlipContext;
import com.tacitknowledge.flip.model.FeatureState;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 *
 * @author ssoloviov
 */
@Aspect
public abstract class FlipAbstractAspect {
    
    public FeatureService getFeatureService() {
        return FlipContext.chooseFeatureService();
    }
    
    @Pointcut("execution(* *(..))")
    public void anyMethod() {}
    
    @Around(value="anyMethod() && @annotation(flip)", argNames="flip")
    public Object aroundFlippableMethods(ProceedingJoinPoint pjp, Flippable flip) throws Throwable {
        if (getFeatureService().getFeatureState(flip.feature()) == FeatureState.DISABLED) {
            return flip.disabledValue();
        } else {
            return pjp.proceed();
        }
    } 
    
    @Around(value="anyMethod() && @within(flip)", argNames="flip")
    public Object aroundFlippableClass(ProceedingJoinPoint pjp, Flippable flip) throws Throwable {
        if (getFeatureService().getFeatureState(flip.feature()) == FeatureState.DISABLED) {
            return flip.disabledValue();
        } else {
            return pjp.proceed();
        }
    }
    
}
