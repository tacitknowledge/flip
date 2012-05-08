package com.tacitknowledge.flip.spring;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.doReturn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.model.FeatureState;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Testing functionality of {@link FlipAspect}
 * 
 * @author Ion Lenta (ilenta@tacitknowledge.com)
 * @author Petric Coroli (pcoroli@tacitknowledge.com)
 */
@SuppressWarnings("boxing")
@RunWith(MockitoJUnitRunner.class)
public class FlipSpringAspectTest
{
    private static final String FEATURE = "feature";

    private static final String URL = "url";

    @InjectMocks
    @Spy
    private final FlipSpringAspect aspect = new FlipSpringAspect();

    @Mock
    private FeatureService featureService;

    @Mock
    private FlipSpringHandler flip;

    @Mock
    private ProceedingJoinPoint pjp;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    private final Object expectedReturnedValue = new Object();

    @Before
    public void setUp() throws Throwable
    {
        when(flip.feature()).thenReturn(FEATURE);
        when(flip.disabledUrl()).thenReturn(URL);
        when(pjp.proceed()).thenReturn(expectedReturnedValue);
        doReturn(flip).when(aspect).getMethodAnnotation(eq(pjp), eq(FlipSpringHandler.class));
    }

    @Test
    public void testControllerWithEnabledFeature() throws Throwable
    {
        when(featureService.getFeatureState(FEATURE)).thenReturn(FeatureState.ENABLED);
        assertSame(expectedReturnedValue, aspect.aroundHandlerFeatureChecker(pjp));
        verify(pjp).proceed();
    }

    @Test
    public void testControllerWithDisabledFeature() throws Throwable
    {
        when(featureService.getFeatureState(FEATURE)).thenReturn(FeatureState.DISABLED);
        assertSame(URL, aspect.aroundHandlerFeatureChecker(pjp));
        verify(pjp, never()).proceed();
    }

    @Test
    public void testResponseBodyControllerWithEnabledFeature() throws Throwable
    {
        when(featureService.getFeatureState(FEATURE)).thenReturn(FeatureState.ENABLED);
        assertSame(expectedReturnedValue, aspect.aroundResponseBodyHandlerFeatureChecker(pjp));
        verify(pjp).proceed();
    }

    @Test
    public void testResponseBodyControllerWithDisabledFeature() throws Throwable
    {
        when(featureService.getFeatureState(FEATURE)).thenReturn(FeatureState.DISABLED);
        assertSame(null, aspect.aroundResponseBodyHandlerFeatureChecker(pjp));
        verify(pjp, never()).proceed();
    }

    @Test
    public void testModelAttributeWithEnabledFeature() throws Throwable
    {
        when(featureService.getFeatureState(FEATURE)).thenReturn(FeatureState.ENABLED);
        assertSame(expectedReturnedValue, aspect.aroundModelAttributeFeatureChecker(pjp));
        verify(pjp).proceed();
    }

    @Test
    public void testModelAttributeWithDisabledFeature() throws Throwable
    {
        when(featureService.getFeatureState(FEATURE)).thenReturn(FeatureState.DISABLED);
        assertSame(null, aspect.aroundModelAttributeFeatureChecker(pjp));
        verify(pjp, never()).proceed();
    }

    @Test
    public void testControllerWithinFlipTypeWithEnabledFeature() throws Throwable
    {
        when(featureService.getFeatureState(FEATURE)).thenReturn(FeatureState.ENABLED);
        assertSame(expectedReturnedValue, aspect.aroundHandlerFeatureCheckerWithinFlipType(pjp));
        verify(pjp).proceed();
    }

    @Test
    public void testControllerWithinFlipTypeWithDisabledFeature() throws Throwable
    {
        when(featureService.getFeatureState(FEATURE)).thenReturn(FeatureState.DISABLED);
        assertSame(URL, aspect.aroundHandlerFeatureCheckerWithinFlipType(pjp));
        verify(pjp, never()).proceed();
    }

    @Test
    public void testResponseBodyControllerWithinFlipTypeWithEnabledFeature() throws Throwable
    {
        when(featureService.getFeatureState(FEATURE)).thenReturn(FeatureState.ENABLED);
        assertSame(expectedReturnedValue, aspect.aroundResponseBodyHandlerFeatureCheckerWithinFlipType(pjp));
        verify(pjp).proceed();
    }

    @Test
    public void testResponseBodyControllerWithinFlipTypeWithDisabledFeature() throws Throwable
    {
        when(featureService.getFeatureState(FEATURE)).thenReturn(FeatureState.DISABLED);
        assertSame(null, aspect.aroundResponseBodyHandlerFeatureCheckerWithinFlipType(pjp));
        verify(pjp, never()).proceed();
    }
}
