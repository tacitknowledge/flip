package com.tacitknowledge.flip.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to be used on handler methods for making it feature toggle aware.
 * 
 * @author Ion Lenta (ilenta@tacitknowledge.com)
 * @author Petric Coroli (pcoroli@tacitknowledge.com)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface FlipSpringHandler
{
    /** The name of the feature */
    String feature();

    /** The path to redirect to if the feature is disabled */
    String disabledUrl();
}
