/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tacitknowledge.testweb;

import com.tacitknowledge.flip.aspectj.FlipParam;
import com.tacitknowledge.flip.aspectj.Flippable;

/**
 *
 * @author ssoloviov
 */
public class AClass {
    
    @Flippable(feature="test", disabledValue="NO-CODE")
    public String getTestCode() {
        return "CODE-123";
    }
    
    @Flippable
    public String getSourceCode(@FlipParam(feature="test", disabledValue="Animal's") String value) {
        return value + " World";
    }
    
}
