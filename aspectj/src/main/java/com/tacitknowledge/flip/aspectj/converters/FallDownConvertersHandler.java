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
 * The converter handler which searches the converter from parent one if
 * it cannot find in its storage. 
 * 
 * @author ssoloviov
 */
public class FallDownConvertersHandler extends ConvertersHandler {

    private ConvertersHandler parent;

    /**
     * Constructs the converter handler and declares the parent converter handler.
     * 
     * @param parent parent converter handler.
     */
    public FallDownConvertersHandler(ConvertersHandler parent) {
        this.parent = parent;
    }
    
    /**
     * Returns the converter by class. If it cannot find the converter if delegates 
     * the search to the parent.
     * 
     * @param klass the class of the converter 
     * @return the converter associated with this class.
     */
    @Override
    public Converter getConverter(Class klass) {
        Converter converter = super.getConverter(klass);
        if (converter == null) {
            converter = parent.getConverter(klass);
        }
        return converter;
    }
    
    
}
