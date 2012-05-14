/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tacitknowledge.testweb;

import com.tacitknowledge.flip.aspectj.Flippable;

/**
 *
 * @author ssoloviov
 */
@Flippable(feature="test", disabledValue="NO-CODE")
public class AClass {
    
    public String getTestCode() {
        return "CODE-123";
    }
    
}
