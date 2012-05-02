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
package com.tacitknowledge.flip.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.tacitknowledge.flip.model.FeatureDescriptor;
import com.tacitknowledge.flip.model.FeatureState;
import com.tacitknowledge.flip.properties.FeatureDescriptorsMap;

/**
 * The web filter used to connect flip service to the web context. This filter
 * if is declared in the {@code web.xml} will add all of request parameters {@code flip.feature-name}
 * to the session which will allow overriding some of the features. Each request 
 * parameter should start with {@code "flip."} and the value should be 
 * {@code "enabled"} or {@code "disabled"}. The feature name to override will be
 * evaluated by the following rule: from the parameter name will be removed 
 * {@code "flip."} string from the start. The rest is the feature name to override.
 * This filter do not creates the session. If there is no session and in the request
 * exist feature overriding parameters they will be ignored and will not be added
 * elsewhere.
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 * @author Petric Coroli <pcoroli@tacitknowledge.com>
 */
public class FlipFilter implements Filter
{
    public static final String SESSION_FEATURES_KEY = "FLIP_SESSION_FEATURES";

    private static final String REQUEST_PARAM_PREFIX = "flip.";

    public void init(final FilterConfig filterConfig) throws ServletException
    {
        // no op
    }

    /**
     * {@inheritDoc }
     */
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
        throws IOException, ServletException
    {
        applyFeaturesFromRequestParameters(request);

        chain.doFilter(request, response);
        FlipWebContext.clear();
    }

    /**
     * {@inheritDoc }
     */
    public void destroy()
    {

    }

    /**
     * The method which uses the request parameters and apply them into the session
     * attribute {@link #SESSION_FEATURES_KEY}. This session attribute holds the
     * map {@link FeatureDescriptorsMap}. The method do not creates the session. So
     * if the session do not exists then the request parameters will be ignored.
     * If in the session under key {@link #SESSION_FEATURES_KEY} persists the 
     * object other than of type {@link FeatureDescriptorsMap} or there is
     * no object in the session then the new object will be created and stored there. 
     * 
     * @param request the request to process 
     */
    private void applyFeaturesFromRequestParameters(final ServletRequest request)
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpSession session = httpRequest.getSession(false);

        FeatureDescriptorsMap featureDescriptors;
        if (session != null)
        {
            featureDescriptors = getSafeSessionFeatures(session);
            session.setAttribute(SESSION_FEATURES_KEY, featureDescriptors);
        }
        else
        {
            featureDescriptors = new FeatureDescriptorsMap();
        }

        applyRequestFeaturesToFeatureDescriptorsMap(request, featureDescriptors);
        FlipWebContext.setFeatureDescriptors(featureDescriptors);
    }

    /**
     * Returns the {@link FeatureDescriptorsMap} object stored in the session
     * under {@link #SESSION_FEATURES_KEY} key. If there is no such object or the 
     * object is of another type the new object will be created. It do not stores
     * the object into the session back.
     * 
     * @param session the session object where to extract the {@link FeatureDescriptorsMap}
     * @return the {@link FeatureDescriptorsMap} stored in the session or new one created on other cases.
     */
    private FeatureDescriptorsMap getSafeSessionFeatures(final HttpSession session)
    {
        final Object sessionObject = session.getAttribute(SESSION_FEATURES_KEY);

        if (sessionObject != null && sessionObject instanceof FeatureDescriptorsMap)
        {
            return (FeatureDescriptorsMap) sessionObject;
        }

        return new FeatureDescriptorsMap();
    }

    /**
     * Moves the features descriptors generated on request parameters to the {@link FeatureDescriptorsMap}.
     * 
     * @param request the request where to extract the request parameters.
     * @param featureDescriptors the {@link FeatureDescriptorsMap} where to store the {@link FeatureDescriptor} objects.
     */
    private void applyRequestFeaturesToFeatureDescriptorsMap(final ServletRequest request,
            final FeatureDescriptorsMap featureDescriptors)
    {
        final Map<String, FeatureDescriptor> transformedParamMap = Maps.transformEntries(request.getParameterMap(),
                new RequestParametersTransformer());
        final Map<String, FeatureDescriptor> parameterMap = Maps.filterEntries(transformedParamMap,
                new RequestParametersFilter());

        for (final FeatureDescriptor descriptor : parameterMap.values())
        {
            featureDescriptors.put(descriptor.getName(), descriptor);
        }
    }

    private static class RequestParametersTransformer implements
            Maps.EntryTransformer<String, String[], FeatureDescriptor>
    {
        public FeatureDescriptor transformEntry(final String key, final String[] value)
        {
            if (value == null || value.length == 0)
            {
                return null;
            }

            try
            {
                final FeatureState state = FeatureState.valueOf(value[0].toUpperCase());

                final FeatureDescriptor featureDescriptor = new FeatureDescriptor();
                featureDescriptor.setName(key.replaceFirst("^" + Pattern.quote(REQUEST_PARAM_PREFIX), ""));
                featureDescriptor.setState(state);
                return featureDescriptor;
            }
            catch (final IllegalArgumentException e)
            {
                return null;
            }
        }
    }

    private static class RequestParametersFilter implements Predicate<Map.Entry<String, FeatureDescriptor>>
    {
        public boolean apply(final Map.Entry<String, FeatureDescriptor> input)
        {
            return input.getKey().startsWith(REQUEST_PARAM_PREFIX) && input.getValue() != null;
        }
    }
}
