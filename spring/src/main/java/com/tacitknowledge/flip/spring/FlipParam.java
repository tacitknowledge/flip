package com.tacitknowledge.flip.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;

/**
 * Annotation to be used on a method's property for making it feature toggle aware.
 * 
 * @author Petric Coroli (pcoroli@tacitknowledge.com)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface FlipParam
{
    /** The name of the feature */
    String feature();

    /** The value parameter to be overridden with if the feature is disabled */
    String disabledValue() default StringUtils.EMPTY;

    /** Setting this to <code>true</code> indicates that <code>disabledValue</code>
     *  should be handled as a SpEL expression.*/
    boolean asSpEL() default false;
}
