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
package com.tacitknowledge.flip.aspectj.fixture;

import com.tacitknowledge.flip.aspectj.FlipParam;
import com.tacitknowledge.flip.aspectj.Flippable;

/**
 *
 * @author ssoloviov
 */
public class FixtureClass {
   
    private String value = "abrakadabra";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Flippable(feature="test", disabledValue="test-value")
    public String flippableMethod() {
        return "result";
    }
   
    @Flippable
    public String flippableMethodWithoutValue() {
        return "result";
    }
   
    @Flippable
    public String flippableMethodWithParam(@FlipParam(feature="test", disabledValue="aaa") String param) {
        return "result" + param;
    }

    @Flippable
    public String flippableMethodWithBooleanParam(@FlipParam(feature="test", disabledValue="false") boolean param) {
        return "result" + (param ? " TRUE" : " FALSE");
    }

    @Flippable
    public String flippableMethodWithByteParam(@FlipParam(feature="test", disabledValue="10") byte param) {
        return "result" + param;
    }

    @Flippable
    public String flippableMethodWithCharParam(@FlipParam(feature="test", disabledValue="x") char param) {
        return "result" + param;
    }

    @Flippable
    public String flippableMethodWithDoubleParam(@FlipParam(feature="test", disabledValue="10.5") double param) {
        return "result" + param;
    }

    @Flippable
    public String flippableMethodWithFloatParam(@FlipParam(feature="test", disabledValue="10.5") float param) {
        return "result" + param;
    }

    @Flippable
    public String flippableMethodWithIntParam(@FlipParam(feature="test", disabledValue="10") int param) {
        return "result" + param;
    }

    @Flippable
    public String flippableMethodWithLongParam(@FlipParam(feature="test", disabledValue="10") long param) {
        return "result" + param;
    }

    @Flippable
    public String flippableMethodWithShortParam(@FlipParam(feature="test", disabledValue="10") short param) {
        return "result" + param;
    }

    @Flippable
    public String flippableMethodWithElParam(@FlipParam(feature="test", disabledValue="${value}") String param) {
        return "result" + param;
    }

    @Flippable
    public String flippableMethodWithElParamInsideText(@FlipParam(feature="test", disabledValue="blabla ${value}") String param) {
        return "result" + param;
    }

    @Flippable(feature="test", disabledValue="false")
    public boolean flippableMethodReturnsBoolean() {
        return true;
    }

    @Flippable(feature="test", disabledValue="10")
    public byte flippableMethodReturnsByte() {
        return 3;
    }

    @Flippable(feature="test", disabledValue="x")
    public char flippableMethodReturnsChar() {
        return 'a';
    }

    @Flippable(feature="test", disabledValue="10.5")
    public double flippableMethodReturnsDouble() {
        return 3.14159265d;
    }

    @Flippable(feature="test", disabledValue="10.5")
    public float flippableMethodReturnsFloat() {
        return 3.14159265f;
    }

    @Flippable(feature="test", disabledValue="10")
    public int flippableMethodReturnsInt() {
        return 3;
    }

    @Flippable(feature="test", disabledValue="10")
    public long flippableMethodReturnsLong() {
        return 3l;
    }

    @Flippable(feature="test", disabledValue="10")
    public short flippableMethodReturnsShort() {
        return 3;
    }

    @Flippable(feature="test", disabledValue="${value}")
    public String flippableMethodReturnsEl() {
        return "result";
    }

    @Flippable(feature="test", disabledValue="blabla ${value}")
    public String flippableMethodReturnsElInsideText() {
        return "result";
    }
    
}
