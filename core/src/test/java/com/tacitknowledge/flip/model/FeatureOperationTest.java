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
package com.tacitknowledge.flip.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class FeatureOperationTest {
    
    @Test
    public void testEqualsStrings() {
        assertEquals("A eq \"A\"", FeatureOperation.EQUALS.buildCondition("A", "A"));
    }
    
    @Test
    public void testEqualsNumbers() {
        assertEquals("A eq 1", FeatureOperation.EQUALS.buildCondition("A", "1"));
    }

    @Test
    public void testEqualsNullParam() {
        assertEquals("null eq 1", FeatureOperation.EQUALS.buildCondition(null, "1"));
    }

    @Test
    public void testEqualsNullValue() {
        assertEquals("a eq null", FeatureOperation.EQUALS.buildCondition("a", null));
    }

    @Test
    public void testEqualsBooleans() {
        assertEquals("true eq true", FeatureOperation.EQUALS.buildCondition("true", "true"));
    }

    @Test
    public void testNotEqualsNumbers() {
        assertEquals("2 ne 3", FeatureOperation.NOT_EQUALS.buildCondition("2", "3"));
    }
    
    @Test
    public void testNotEqualsStrings() {
        assertEquals("A ne \"a\"", FeatureOperation.NOT_EQUALS.buildCondition("A", "a"));
    }
    
    @Test
    public void testNotEqualsNullParam() {
        assertEquals("null ne \"a\"", FeatureOperation.NOT_EQUALS.buildCondition(null, "a"));
    }
    
    @Test
    public void testNotEqualsNullValue() {
        assertEquals("A ne null", FeatureOperation.NOT_EQUALS.buildCondition("A", null));
    }
    
    @Test
    public void testNotEqualsBooleans() {
        assertEquals("true ne false", FeatureOperation.NOT_EQUALS.buildCondition("true", "false"));
    }
    
    @Test
    public void testLess() {
        assertEquals("1 lt 5", FeatureOperation.LESS.buildCondition("1", "5"));
    }

    @Test
    public void testGreater() {
        assertEquals("3 gt 2", FeatureOperation.GREATER.buildCondition("3", "2"));
    }
    
    @Test
    public void testGreaterOrEquals() {
        assertEquals("3 ge 2", FeatureOperation.GREATER_EQUALS.buildCondition("3", "2"));
    }
    @Test
    public void testLessOrEquals() {
        assertEquals("1 le 5", FeatureOperation.LESS_EQUALS.buildCondition("1", "5"));
    }
    
    @Test
    public void testMatches() {
        assertEquals("abab =~ \"(ab){2}\"", FeatureOperation.MATCHES.buildCondition("abab", "(ab){2}"));
    }

    @Test
    public void testNotMatches() {
        assertEquals("abab !~ \"(ab){2}\"", FeatureOperation.NOT_MATCHES.buildCondition("abab", "(ab){2}"));
    }
        
}
