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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;



/**
 * Annotation to be used on handler methods for making it feature toggle aware.
 * If you have only to intercept the methods parameters you can declare it 
 * without any parameters.
 * 
 * @author Ion Lenta (ilenta@tacitknowledge.com)
 * @author Petric Coroli (pcoroli@tacitknowledge.com)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Flippable
{
    /** The name of the feature */
    String feature() default StringUtils.EMPTY;

    /** The path to redirect to if the feature is disabled. 
     * This parameter could contain value expression inside <code>${}</code>.
     */
    String disabledValue() default StringUtils.EMPTY;
}
