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

/**
 * The converted class used to convert disabled values (represented by string)
 * to objects of required type.
 * 
 * @author ssoloviov
 */
public interface Converter {
   
    /**
     * Returns the array of class to which this converter could convert.
     * 
     * @return the array of classes to which this converter can convert.
     */
    Class[] getManagedClasses();
    
    /**
     * Converts a string into an object. 
     * @param expression the string to convert
     * @param outputClass The type of object to convert to.
     * @return the object converted.
     */
    Object convert(String expression, Class outputClass);
    
}
