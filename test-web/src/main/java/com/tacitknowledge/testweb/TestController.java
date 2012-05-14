/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tacitknowledge.testweb;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.tacitknowledge.flip.aspectj.Flippable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ssoloviov
 */
@Controller
public class TestController {
    
    @RequestMapping(value="/index.html")
    public String index() {
        return "index";
    }

    @RequestMapping(value="/page.html")
    @Flippable(feature="test", disabledValue="404")
    public String page() {
        return "page";
    }
    
    
}
