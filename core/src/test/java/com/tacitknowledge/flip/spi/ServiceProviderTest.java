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
package com.tacitknowledge.flip.spi;

import com.tacitknowledge.flip.fixtures.TestContextProvider;
import com.tacitknowledge.flip.otherfixtures.Test1ContextProvider;
import com.tacitknowledge.flip.properties.FlipProperty;
import com.tacitknowledge.flip.properties.PropertyReader;
import com.tacitknowledge.flip.context.FlipContext;
import java.util.Collection;
import java.util.Iterator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class ServiceProviderTest {
    
    private ServiceProvider serviceProvider;
    
    @Before
    public void setUp() {
        serviceProvider = new ServiceProvider("com.tacitknowledge.flip.fixtures");
    }
    
    @Test
    public void testFindByAnnotation() {
        Collection<Class<?>> objects = serviceProvider.findClass(FlipContext.class);
        assertFalse(objects.isEmpty());
        testAllItemsAreAnnotatedWith(objects, FlipContext.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCannotCreateInstanceWithoutPackagesDeclared() {
        serviceProvider = new ServiceProvider();
    }
    
    @Test
    public void testFindByPackage() {
        Collection<Class<?>> services = serviceProvider.findClass(FlipContext.class);
        assertTrue(services.contains(TestContextProvider.class));
        assertFalse(services.contains(Test1ContextProvider.class));
    }
    
    @Test
    public void testFindByMultiplePackages() {
        serviceProvider = new ServiceProvider("com.tacitknowledge.flip.fixtures", "com.tacitknowledge.flip.otherfixtures");
        Collection<Class<?>> objects = serviceProvider.findClass(FlipContext.class);
        assertFalse(objects.isEmpty());
        boolean found = false;
        for(Class clazz : objects) {
            if (clazz == Test1ContextProvider.class) {
                found = true;
                break;
            }
        }
        assertTrue(found);
        found = false;
        for(Class clazz : objects) {
            if (clazz == TestContextProvider.class) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
    
    @Test
    public void testFindByInterface() {
        Collection<PropertyReader> objects = serviceProvider.find(FlipProperty.class, PropertyReader.class);
        assertFalse(objects.isEmpty());
        testAllItemsOfType(objects, PropertyReader.class);
    }

    private void testAllItemsAreAnnotatedWith(Collection<Class<?>> obj, Class annotationType) {
        Iterator<Class<?>> i = obj.iterator();
        while(i.hasNext()) {
            assertTrue(i.next().isAnnotationPresent(annotationType));
        }
    }

    private void testAllItemsOfType(Collection obj, Class expectedType) {
        Iterator i = obj.iterator();
        while(i.hasNext()) {
            assertTrue(expectedType.isAssignableFrom(i.next().getClass()));
        }
    }
}
