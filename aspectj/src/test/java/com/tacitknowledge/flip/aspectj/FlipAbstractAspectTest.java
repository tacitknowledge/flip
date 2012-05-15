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

import java.lang.reflect.Method;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.aspectj.fixture.FixtureClass;
import com.tacitknowledge.flip.model.FeatureState;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 *
 * @author ssoloviov
 */
@RunWith(MockitoJUnitRunner.class)
public class FlipAbstractAspectTest {
    
    @Spy
    private FlipAbstractAspect aspect = FlipAspectJAspect.aspectOf();
    
    @Mock
    private FeatureService featureService;
    
    @Mock
    private ProceedingJoinPoint pjp;
    
    @Mock
    private MethodSignature methodSignature;
    
    private FixtureClass fixtureClassObject = new FixtureClass();
    
    private Method currentMethod;
    private Flippable flipAnnotation;
    
    private Method flippableMethod;
    private Method flippableMethodWithoutValue;
    private Method flippableMethodWithParam;
    private Method flippableMethodWithBooleanParam;
    private Method flippableMethodWithByteParam;
    private Method flippableMethodWithCharParam;
    private Method flippableMethodWithDoubleParam;
    private Method flippableMethodWithFloatParam;
    private Method flippableMethodWithLongParam;
    private Method flippableMethodWithIntParam;
    private Method flippableMethodWithShortParam;
    private Method flippableMethodWithElParam;
    private Method flippableMethodWithElParamInsideText;
    private Method flippableMethodReturnsBoolean;
    private Method flippableMethodReturnsByte;
    private Method flippableMethodReturnsChar;
    private Method flippableMethodReturnsDouble;
    private Method flippableMethodReturnsFloat;
    private Method flippableMethodReturnsLong;
    private Method flippableMethodReturnsInt;
    private Method flippableMethodReturnsShort;
    private Method flippableMethodReturnsEl;
    private Method flippableMethodReturnsElInsideText;
    
    @Before
    public void setUp() throws NoSuchMethodException, Throwable {
        when(pjp.getSignature()).thenReturn(methodSignature);
        doReturn(featureService).when(aspect).getFeatureService();
        when(pjp.proceed(any(Object[].class))).thenAnswer(new Answer<Object>(){

            public Object answer(InvocationOnMock invocation) throws Throwable {
                return currentMethod.invoke(fixtureClassObject, (Object[]) invocation.getArguments()[0]);
            }
        });
        when(methodSignature.getMethod()).thenAnswer(new Answer<Method>() {

            public Method answer(InvocationOnMock invocation) throws Throwable {
                return currentMethod;
            }
        });
        when(pjp.getThis()).thenReturn(fixtureClassObject);
       
        createMethodInstances();
    }
    
    @Test
    public void testFlippableMethodDisabled() throws Throwable {
        setCurrentMethod(flippableMethod);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        
        assertEquals("test-value", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodEnabled() throws Throwable {
        setCurrentMethod(flippableMethod);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.ENABLED);
        
        assertEquals("result", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithoutValueDisabled() throws Throwable {
        setCurrentMethod(flippableMethodWithoutValue);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        
        assertEquals("result", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithoutValueEnabled() throws Throwable {
        setCurrentMethod(flippableMethodWithoutValue);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.ENABLED);
        
        assertEquals("result", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithParamEnabled() throws Throwable {
        setCurrentMethod(flippableMethodWithParam);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.ENABLED);
        when(pjp.getArgs()).thenReturn(new String[]{"xx"});
        
        assertEquals("resultxx", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithParamDisabled() throws Throwable {
        setCurrentMethod(flippableMethodWithParam);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        when(pjp.getArgs()).thenReturn(new String[]{"xx"});
        
        assertEquals("resultaaa", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithBooleanParamDisabled() throws Throwable {
        setCurrentMethod(flippableMethodWithBooleanParam);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        when(pjp.getArgs()).thenReturn(new Boolean[]{true});
        
        assertEquals("result FALSE", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithByteParamDisabled() throws Throwable {
        setCurrentMethod(flippableMethodWithByteParam);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        when(pjp.getArgs()).thenReturn(new Byte[]{1});
        
        assertEquals("result10", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithCharParamDisabled() throws Throwable {
        setCurrentMethod(flippableMethodWithCharParam);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        when(pjp.getArgs()).thenReturn(new Character[]{'a'});
        
        assertEquals("resultx", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithDoubleParamDisabled() throws Throwable {
        setCurrentMethod(flippableMethodWithDoubleParam);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        when(pjp.getArgs()).thenReturn(new Double[]{3.14159265d});
        
        assertEquals("result10.5", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithFloatParamDisabled() throws Throwable {
        setCurrentMethod(flippableMethodWithFloatParam);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        when(pjp.getArgs()).thenReturn(new Float[]{3.14159265f});
        
        assertEquals("result10.5", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithIntParamDisabled() throws Throwable {
        setCurrentMethod(flippableMethodWithIntParam);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        when(pjp.getArgs()).thenReturn(new Integer[]{3});
        
        assertEquals("result10", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithLongParamDisabled() throws Throwable {
        setCurrentMethod(flippableMethodWithLongParam);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        when(pjp.getArgs()).thenReturn(new Long[]{3l});
        
        assertEquals("result10", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithShortParamDisabled() throws Throwable {
        setCurrentMethod(flippableMethodWithShortParam);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        when(pjp.getArgs()).thenReturn(new Short[]{3});
        
        assertEquals("result10", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithElParamDisabled() throws Throwable {
        setCurrentMethod(flippableMethodWithElParam);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        when(pjp.getArgs()).thenReturn(new String[]{"blablabla"});
        
        assertEquals("resultabrakadabra", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodWithElParamInsideTextDisabled() throws Throwable {
        setCurrentMethod(flippableMethodWithElParamInsideText);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        when(pjp.getArgs()).thenReturn(new String[]{"blablabla"});
        
        assertEquals("resultabrakadabra", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    //-----------------
    @Test
    public void testFlippableMethodReturnsBooleanDisabled() throws Throwable {
        setCurrentMethod(flippableMethodReturnsBoolean);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        
        assertEquals(false, aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodReturnsByteDisabled() throws Throwable {
        setCurrentMethod(flippableMethodReturnsByte);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        
        assertEquals((byte)10, aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodReturnsCharDisabled() throws Throwable {
        setCurrentMethod(flippableMethodReturnsChar);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        
        assertEquals('x', aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodReturnsDoubleDisabled() throws Throwable {
        setCurrentMethod(flippableMethodReturnsDouble);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        
        assertEquals(10.5d, aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodReturnsFloatDisabled() throws Throwable {
        setCurrentMethod(flippableMethodReturnsFloat);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        
        assertEquals(10.5f, aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodReturnsIntDisabled() throws Throwable {
        setCurrentMethod(flippableMethodReturnsInt);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        
        assertEquals(10, aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodReturnsLongDisabled() throws Throwable {
        setCurrentMethod(flippableMethodReturnsLong);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        
        assertEquals(10l, aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodReturnsShortDisabled() throws Throwable {
        setCurrentMethod(flippableMethodReturnsShort);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        
        assertEquals((short)10, aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodReturnsElDisabled() throws Throwable {
        setCurrentMethod(flippableMethodReturnsEl);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        
        assertEquals("abrakadabra", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }
    
    @Test
    public void testFlippableMethodReturnsElInsideTextDisabled() throws Throwable {
        setCurrentMethod(flippableMethodReturnsElInsideText);
        when(featureService.getFeatureState(eq("test"))).thenReturn(FeatureState.DISABLED);
        
        assertEquals("abrakadabra", aspect.aroundFlippableMethods(pjp, flipAnnotation));
    }

    private void createMethodInstances() throws NoSuchMethodException {
        flippableMethod = FixtureClass.class.getMethod("flippableMethod");
        flippableMethodWithoutValue = FixtureClass.class.getMethod("flippableMethodWithoutValue");
        flippableMethodWithParam = FixtureClass.class.getMethod("flippableMethodWithParam", String.class);
        flippableMethodWithBooleanParam = FixtureClass.class.getMethod("flippableMethodWithBooleanParam", Boolean.TYPE);
        flippableMethodWithByteParam = FixtureClass.class.getMethod("flippableMethodWithByteParam", Byte.TYPE);
        flippableMethodWithCharParam = FixtureClass.class.getMethod("flippableMethodWithCharParam", Character.TYPE);
        flippableMethodWithDoubleParam = FixtureClass.class.getMethod("flippableMethodWithDoubleParam", Double.TYPE);
        flippableMethodWithFloatParam = FixtureClass.class.getMethod("flippableMethodWithFloatParam", Float.TYPE);
        flippableMethodWithIntParam = FixtureClass.class.getMethod("flippableMethodWithIntParam", Integer.TYPE);
        flippableMethodWithLongParam = FixtureClass.class.getMethod("flippableMethodWithLongParam", Long.TYPE);
        flippableMethodWithShortParam = FixtureClass.class.getMethod("flippableMethodWithShortParam", Short.TYPE);
        flippableMethodWithElParam = FixtureClass.class.getMethod("flippableMethodWithElParam", String.class);
        flippableMethodWithElParamInsideText = FixtureClass.class.getMethod("flippableMethodWithElParamInsideText", String.class);

        flippableMethodReturnsBoolean = FixtureClass.class.getMethod("flippableMethodReturnsBoolean");
        flippableMethodReturnsByte = FixtureClass.class.getMethod("flippableMethodReturnsByte");
        flippableMethodReturnsChar = FixtureClass.class.getMethod("flippableMethodReturnsChar");
        flippableMethodReturnsDouble = FixtureClass.class.getMethod("flippableMethodReturnsDouble");
        flippableMethodReturnsFloat = FixtureClass.class.getMethod("flippableMethodReturnsFloat");
        flippableMethodReturnsInt = FixtureClass.class.getMethod("flippableMethodReturnsInt");
        flippableMethodReturnsLong = FixtureClass.class.getMethod("flippableMethodReturnsLong");
        flippableMethodReturnsShort = FixtureClass.class.getMethod("flippableMethodReturnsShort");
        flippableMethodReturnsEl = FixtureClass.class.getMethod("flippableMethodReturnsEl");
        flippableMethodReturnsElInsideText = FixtureClass.class.getMethod("flippableMethodReturnsElInsideText");
        
    }

    private void setCurrentMethod(Method flippableMethod) {
        currentMethod = flippableMethod;
        flipAnnotation = currentMethod.getAnnotation(Flippable.class);
    }
    
}
