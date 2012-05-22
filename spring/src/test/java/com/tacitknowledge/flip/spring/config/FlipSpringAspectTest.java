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
package com.tacitknowledge.flip.spring.config;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.FlipContext;
import com.tacitknowledge.flip.aspectj.FlipAopContext;
import com.tacitknowledge.flip.aspectj.ValueExpressionEvaluator;
import com.tacitknowledge.flip.aspectj.converters.Converter;
import com.tacitknowledge.flip.aspectj.converters.FallDownConvertersHandler;
import com.tacitknowledge.flip.spring.FlipSpringAspect;
import com.tacitknowledge.flip.spring.SpElValueExpressionEvaluator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.expression.spel.SpelEvaluationException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author ssoloviov
 */
@RunWith(MockitoJUnitRunner.class)
public class FlipSpringAspectTest {
    
    private FlipSpringAspect aspect;
    
    @Mock
    private Converter converter;
    
    @Mock
    private FeatureService featureService;
    
    @Mock
    private FeatureService featureService1;
    
    @Mock
    private ValueExpressionEvaluator evaluator;
    
    @Mock
    private ValueExpressionEvaluator evaluator1;
    
    @Before
    public void setUp() {
        aspect = new FlipSpringAspect();
    }

    @After
    public void tearDown() {
        FlipContext.setFeatureService(null);
    }
    
    @Test
    public void testFeatureServiceRetrieving() {
        FlipContext.setFeatureService(featureService1);
        aspect.setFeatureService(featureService);

        assertEquals(featureService, aspect.getFeatureService());
    }
    
    @Test
    public void testFeatureServiceRetrievingFallDown() {
        FlipContext.setFeatureService(featureService1);
        aspect.setFeatureService(null);

        assertEquals(featureService1, aspect.getFeatureService());
    }
    
    @Test
    public void testSpElValueExpressionEvaluatorSet() {
        assertThat(aspect.getValueExpressionEvaluator(), instanceOf(SpElValueExpressionEvaluator.class));
    }
    
    @Test
    public void testSpElValueExpressionEvaluatorRetrieving() {
        FlipAopContext.setValueExpressionEvaluator(evaluator1);
        aspect.setValueExpressionEvaluator(evaluator);
        assertEquals(evaluator, aspect.getValueExpressionEvaluator());
    }
    
    @Test
    public void testSpElValueExpressionEvaluatorRetrievingFallDown() {
        FlipAopContext.setValueExpressionEvaluator(evaluator1);
        aspect.setValueExpressionEvaluator(null);
        assertEquals(evaluator1, aspect.getValueExpressionEvaluator());
    }
    
    @Test
    public void testConvertersHandler() {
        assertNotNull(aspect.getConvertersHandler());
        assertThat(aspect.getConvertersHandler(), instanceOf(FallDownConvertersHandler.class));
    }
    
    
}
