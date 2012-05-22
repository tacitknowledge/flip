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
package com.tacitknowledge.flip.aspectj.converters;

import org.apache.commons.lang3.BooleanUtils;

/**
 * The converted used to convert from string boolean values.
 * By default this converter will convert to <code>true</code> the strings 
 * <code>true</code>, <code>yes</code> or <code>on</code>, in other cases
 * will return <code>false</code>.
 * 
 * @author ssoloviov
 */
public class BooleanConverter implements Converter {

    private static final Class[] MANAGED_CLASSES = new Class[] {Boolean.TYPE, Boolean.class};
   
    /** {@inheritDoc } */
    public Class[] getManagedClasses() {
        return MANAGED_CLASSES;
    }

    /** {@inheritDoc } */
    public Object convert(String expression, Class outputClass) {
        return BooleanUtils.toBoolean(expression);
    }
    
}
