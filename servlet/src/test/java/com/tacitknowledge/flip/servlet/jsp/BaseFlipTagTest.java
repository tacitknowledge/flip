/* Copyright 2012 Tacit Knowledge
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.tacitknowledge.flip.servlet.jsp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import com.tacitknowledge.flip.exceptions.FlipException;
import org.junit.Before;
import org.junit.Test;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.model.FeatureState;
import com.tacitknowledge.flip.servlet.jsp.BaseFlipTag;

public class BaseFlipTagTest
{
    private BaseFlipTag tag;

    private FeatureService featureService;

    @Before
    public void setUp()
    {
        featureService = mock(FeatureService.class);
        tag = new BaseFlipTag(FeatureState.DISABLED)
        {
            @Override
            protected FeatureService getFeatureService()
            {
                return featureService;
            }
        };
    }

    @Test
    public void testEvalBodyInclude() throws JspException
    {
        when(featureService.getFeatureState("name")).thenReturn(FeatureState.DISABLED);
        tag.setFeature("name");
        assertEquals(Tag.EVAL_BODY_INCLUDE, tag.doStartTag());
    }

    @Test
    public void testSkipBody() throws JspException
    {
        when(featureService.getFeatureState("name")).thenReturn(FeatureState.ENABLED);
        tag.setFeature("name");
        assertEquals(Tag.SKIP_BODY, tag.doStartTag());
    }

    @Test
    public void testThrowFileExceptionWhenFeatureServiceNotSet() throws JspException
    {
        BaseFlipTag t = new BaseFlipTag(FeatureState.ENABLED)
        {
            @Override
            protected FeatureService getFeatureService()
            {
                return null;
            }
        };

        try
        {
            t.doStartTag();
            fail("Expected a FlipException to be thrown.");
        }
        catch (FlipException e)
        {
            // Expected
        }
    }
}
