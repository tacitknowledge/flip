package com.tacitknowledge.flip.servlet.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Temporary implementation of the flip:enabled tag.
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public class FlipEnabledTag extends TagSupport
{
    private String feature;

    @Override
    public int doStartTag() throws JspException
    {
        if ("enabled".equals(pageContext.getRequest().getParameter(feature)))
        {
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }

    public String getFeature()
    {
        return feature;
    }

    public void setFeature(String feature)
    {
        this.feature = feature;
    }
}
