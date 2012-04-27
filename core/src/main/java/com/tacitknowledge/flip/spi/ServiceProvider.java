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

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.reflections.Reflections;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

/**
 * Class used to lookup classes by annotation. This class is capable to find classes 
 * in specific packages available by current class loader which are marked by an 
 * annotation. Additionally this class is able to instantiate the classes found 
 * using default constructor. 
 * 
 * @author Serghei Soloviov <ssoloviov@tacitknowledge.com>
 */
public class ServiceProvider
{
    private final Reflections reflections;

    /**
     * Constructs new ServiceProvider which is able to find classes in declared 
     * packages. The packages passed should be available from current class loader. 
     * This class do not loads the classes into class loader. It reads the class 
     * file and analyzes it to contain the specific annotation.
     * 
     * @param pkgs the packages where to lookup the classes. 
     * @throws IllegalArgumentException if there are no packages passed to lookup.
     */
    public ServiceProvider(final String... pkgs)
    {
        if (pkgs == null || pkgs.length == 0)
        {
            throw new IllegalArgumentException("The list of packages to find classes should contain at least one item.");
        }

        reflections = new Reflections(pkgs);
    }

    /**
     * Finds classes by the annotation. The annotation should 
     * 
     * @param annotationClass the annotation class which marks the classes to find. 
     * @return the collection of classes found. If no classes found returns an empty collection.
     */
    protected Collection<Class<?>> findClass(final Class<? extends Annotation> annotationClass)
    {
        return reflections.getTypesAnnotatedWith(annotationClass);
    }

    /**
     * Finds the classes marked by the annotation and instantiates them. 
     * 
     * @param annotatedClass the annotation which marks the classes. 
     * @return the collections of objects instantiated from classes found. If no 
     * classes were found returns an empty collection.
     */
    public Collection<Object> find(final Class<? extends Annotation> annotatedClass)
    {
        return find(annotatedClass, Object.class);
    }

    /**
     * Finds classes by the annotation and which extends from the resultingClass 
     * and instantiates them. If the classes which are marked by the annotation 
     * and do not extend the resultingClass then they are ignored. 
     * 
     * @param <T> the class of objects to be returned.
     * @param annotatedClass the annotation which marks the classes to find.
     * @param resultingClass the class from which the found classes should extend. 
     * It could be an interface or a class. 
     * @return the collection of objects found.
     */
    public <T> Collection<T> find(final Class<? extends Annotation> annotatedClass, final Class<T> resultingClass)
    {
        final Collection<Class<?>> filteredList = Collections2.filter(findClass(annotatedClass),
                Predicates.assignableFrom(resultingClass));

        return Collections2.transform(filteredList, new ClassToObjectTransformer<T>());
    }

    private static class ClassToObjectTransformer<T> implements Function<Class<?>, T>
    {

        @Override
        public T apply(final Class<?> input)
        {
            try
            {
                return (T) input.newInstance();
            }
            catch (final InstantiationException ex)
            {
                throw new RuntimeException(String.format("Cannot create an instance for type %s.", input.getName()), ex);
            }
            catch (final IllegalAccessException ex)
            {
                throw new RuntimeException(String.format("Cannot create an instance for type %s.", input.getName()), ex);
            }
        }
    }
}
