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

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ssoloviov
 */
@Aspect
public class FlipSpringAspectPointcuts {
    
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

    @Pointcut("@within(flip) && anyNonResponseBodyControllerHandlerMethod()")
    public void anyHandlerFeatureCheckerWithinFlipType()
    {
    }

    @Pointcut("@within(flip) && anyResponseBodyControllerHandlerMethod()")
    public void anyResponseBodyHandlerFeatureCheckerWithinFlipType()
    {
    }

    @Pointcut("@annotation(flip) && anyNonResponseBodyControllerHandlerMethod()")
    public void anyHandlerFeatureChecker()
    {
    }

    @Pointcut("@annotation(flip) && anyResponseBodyControllerHandlerMethod()")
    public void anyResponseBodyHandlerFeatureChecker()
    {
    }

    @Pointcut("@annotation(flip) && anyModelAttributeMethod()")
    public void anyModelAttributeFeatureChecker()
    {
    }

    @Pointcut("anyMethod() && @annotation(flip)")
    public void anyMethodFeatureCheckerWithFlipParameters()
    {
    }    
    
}
