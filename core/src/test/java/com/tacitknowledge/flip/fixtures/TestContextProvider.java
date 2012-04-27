/* Copyright 2012 Tacit Knowledge
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.tacitknowledge.flip.fixtures;

import com.tacitknowledge.flip.context.FlipContext;
import com.tacitknowledge.flip.context.FlipContextProperty;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
@FlipContext(name = "test")
public class TestContextProvider {
    
    public String hello() {
        return "world";
    }
    
    public void ignoreVoidMethod() {
        // do nothing;
    }
    
    public String methodWithParams(Object aParam) {
        return null;
    }
    
    public String getValue()
    {
        return "someValue";
    }
    
    public boolean isBooleanValue()
    {
        return false;
    }
    
    public Boolean isBooleanObjectValue()
    {
        return Boolean.FALSE;
    }
    
    public Object isObjectValueNonBoolean()
    {
        return null;
    }
    
    @FlipContextProperty("intKey")
    public Integer getIntegerValue() {
        return 10;
    }
    
    @FlipContextProperty()
    public Map<String, Object> getAllValuesAsMap() {
        return Collections.singletonMap("key", (Object) "value");
    }
    
    @FlipContextProperty("complexMap")
    public Map<String, Object> getComplexMapValue() {
        return Collections.singletonMap("key", (Object) "value");
    }
    
    @FlipContextProperty("complexObject")
    public Locale getComplexObject() {
        return Locale.CANADA_FRENCH;
    }
    
    @FlipContextProperty()
    public String getAnonMethod() {
        return "blah";
    }
}
