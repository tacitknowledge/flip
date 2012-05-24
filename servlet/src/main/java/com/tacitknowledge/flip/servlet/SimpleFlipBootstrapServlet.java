package com.tacitknowledge.flip.servlet;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.FeatureServiceReflectionFactory;
import com.tacitknowledge.flip.FlipContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

/**
 * A servlet used to bootstrap Flip in a standard web environment. This servlet should be used as a
 * startup servlet. It looks for a "package-path" init property that should contain a comma-separated
 * list of packages containing context and property providers.
 *
 * @author Scott Askew <scott@tacitknowledge.com>
 */
public class SimpleFlipBootstrapServlet extends HttpServlet
{
    /** The init param containing a comma-separated list of packages containing context and property providers */
    public static final String PACKAGE_PATH = "package-path";

    /** The servlet config. */
    private ServletConfig config;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        this.config = config;
        String packagePath = config.getInitParameter(PACKAGE_PATH);
        FeatureServiceReflectionFactory factory = new FeatureServiceReflectionFactory();
        FeatureService service = factory.createFeatureService(getPackagesToSearch(packagePath));
        FlipContext.setFeatureService(service);
    }

    @Override
    public ServletConfig getServletConfig() {
        return config;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
    {
    }

    @Override
    public String getServletInfo() {
        return "Simple Flip Bootstrap Servlet";
    }

    @Override
    public void destroy() {
    }

    /**
     * Returns a String array containing the list of packages to search for providers based on the servlet
     * config. The core Flip providers will automatically be prepended to this list.
     *
     * @return a array of package names
     */
    protected String[] getPackagesToSearch(String packagePath)
    {
        if (packagePath == null)
        {
            packagePath = "";
        }

        // Split on either commas, colons, or semicolons and ignore leading and trailing whitespace
        return packagePath.split("\\s*[,:;]\\s*");
    }
}
