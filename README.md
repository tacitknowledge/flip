## Flip - feature toggling framework

Flip is embracing the concept of [Feature Toggle](http://martinfowler.com/bliki/FeatureToggle.html),
and brings it into the Java world.

There are a several ways that this can be used, including:

1. Reducing the need to branch by feature in git by having all work done on master. Features that are not ready for production can be disabled.
2. Features will eventually be enabled or disabled per user group, enabling closed betas.
3. Should a feature become problematic, we can turn it off without requiring a build and deploy.

# Installation

## Ant

If you application is built using Ant you should copy the following libraries
to your project library folder:

1. flip-core-1.0-SNAPSHOT.jar (the core library itself)
2. commons-collections-3.2.1.jar from http://commons.apache.org/collections/
3. commons-jexl-2.1.1.jar from http://commons.apache.org/jexl/
4. commons-lang3-3.1.jar from http://commons.apache.org/lang/
5. commons-logging-1.1.1.jar from http://commons.apache.org/logging/
6. reflections-0.9.6.jar from http://code.google.com/p/reflections/
7. dom4j-1.6.1.jar from http://dom4j.sourceforge.net/
8. guava-10.0.jar from http://dom4j.sourceforge.net/
9. javassist-3.12.1.GA.jar from http://www.javassist.org/

## Maven

If you build your application using maven you simply add the following dependency:

    <dependency>
        <groupId>com.tacitknowledge</groupId>
        <artifactId>flip</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

or each module separately:

    <dependency>
        <groupId>com.tacitknowledge.flip</groupId>
        <artifactId>core</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    
    <dependency>
        <groupId>com.tacitknowledge.flip</groupId>
        <artifactId>servlet</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    
    <dependency>
        <groupId>com.tacitknowledge.flip</groupId>
        <artifactId>spring</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

# Documentation

The documentation for Flip is on the [Flip Wiki](https://github.com/tacitknowledge/flip/wiki)

# Usage

After you added the library to the project tree you have to integrate it. 
Firstly you should instantiate the `com.tacitknowledge.flip.FeatureService`
object by using one of two default factories:

1. `FeatureServiceReflectionFactory` - as the name implies, it leverages capabilities
of reflection to perform lookup process, identifying context providers and property readers defined
withing the system.
2. `FeatureServiceDirectFactory` - this factory expects that required entities will be provided, leaving
responsibility of initialization of `context providers` and `property readers` to the underlying DI framework,
or to the developer. 

After you started the feature service you have to place the invocations to
feature service to obtain the feature state in that places of your code 
where is needed. These steps are specific for your project and they do not 
have a default implementations.

The next step is to define the feature states. This could be done by using
the default property reader `XmlPropertyReader` or by implementing your own.
XmlPropertyReader obtains the feature descriptors from XML file which could
be placed in your project.


# Licensing

This framework is released under Apache 2.0 Public License. The text of the
license you can find at http://www.apache.org/licenses/LICENSE-2.0.txt.

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
