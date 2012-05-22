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

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 *
 * @author ssoloviov
 */
@RunWith(MockitoJUnitRunner.class)
public class FallDownConvertersHandlerTest {
    
    private FallDownConvertersHandler handler;
    
    @Mock
    private Converter converter;
    
    @Mock
    private ConvertersHandler parentHandler;
    
    @Before
    public void setUp() {
        handler = new FallDownConvertersHandler(parentHandler);
        when(parentHandler.getConverter(eq(List.class))).thenReturn(converter);
    }
    
    @Test
    public void testFallDown() {
        assertEquals(converter, handler.getConverter(List.class));
    }
    
    @Test
    public void testDoNotFallDown() {
        Converter converter1 = mock(Converter.class);
        when(converter1.getManagedClasses()).thenReturn(new Class[]{List.class});
        handler.addConverter(converter1);
        
        assertEquals(converter1, handler.getConverter(List.class));
    }
    
}
