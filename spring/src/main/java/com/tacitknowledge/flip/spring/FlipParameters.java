package com.tacitknowledge.flip.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation to be used on a method indicating that its parameters
 * are subject to {@link FlipParam} override
 * 
 * @author Petric Coroli (pcoroli@tacitknowledge.com)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FlipParameters
{
    // no op
}
