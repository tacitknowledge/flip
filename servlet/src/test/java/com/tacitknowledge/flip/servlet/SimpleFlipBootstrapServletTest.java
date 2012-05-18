package com.tacitknowledge.flip.servlet;

import com.google.common.collect.ObjectArrays;
import com.tacitknowledge.flip.FlipContext;
import org.junit.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Tests for the SimpleFlipBootstrapServlet.
 *
 * @author Scott Askew <scott@tacitknowledge.com>
 */
public class SimpleFlipBootstrapServletTest
{
    @Test
    public void testPackagePathSplitting()
    {
        SimpleFlipBootstrapServlet servlet = new SimpleFlipBootstrapServlet();
        String[] expectedPath = buildExpectedPackagePath("foo", "bar", "baz", "bat");
        // Test comma, colon, and semi-colon separators, with leading and trailing whitespace
        String[] actualPath = servlet.getPackagesToSearch("foo, bar :baz;bat");

        assertEquals(expectedPath.length, actualPath.length);

        for (int i = 0; i < expectedPath.length; i++)
        {
            assertEquals(expectedPath[i], actualPath[i]);
        }
    }

    @Test
    public void testFeatureServiceIsSet() throws ServletException
    {
        SimpleFlipBootstrapServlet servlet = new SimpleFlipBootstrapServlet();
        servlet.init(mock(ServletConfig.class));
        assertNotNull(FlipContext.getFeatureService());
    }

    private String[] buildExpectedPackagePath(String... packages)
    {
        String[] autoPackages = SimpleFlipBootstrapServlet.AUTOMATIC_PACKAGES.split("\\s*[,:]\\s*");
        return ObjectArrays.concat(autoPackages, packages, String.class);
    }
}
