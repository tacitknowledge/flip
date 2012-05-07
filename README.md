Flip feature toggling framework

Contents

1. Overview
2. Install
    1. Ant
    2. Maven
3. Usage
4. Licensing
    

== 1. Overview ==

This framework is intended to resolve the problem when some features of the 
application should be disabled. Such situations could be as disabling the
map based store locator while it is not fully tested, or for some reasons
should not appear on production for a while. Or this framework could be used
for A/B testing. 
Also this framework allows enabling and disabling features runtime (e.g. the
administrator goes to a page where it enables and disables the features and
the changes made applies when it presses the submit button). 
Another function added to the framework is to disable or enable the feature
depending on some conditions (e.g. when the user is from Canada then show
and additional message on the page, for others do not show this message).

== 2. Install ==

=== 2.1. Ant ===

If you application is build using Ant you should copy the following libraries
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


=== 2.2. Maven ===

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

== 3. Usage ==

After you added the library to the project tree you have to integrate it. 
Firstly you should instantiate the com.tacitknowledge.flip.FeatureService
object by using one of two default factories:
1. FeatureServiceReflectionFactory - which looks into the packages passed to the 
    factory methods for required context providers and property readers.
2. FeatureServiceDirectFactory - here you have to pass itself the context providers
    and property readers. This factory almost is used to use with DI.

After you started the feature service you have to place the invocations to
feature service to obtain the feature state in that places of your code 
where is needed. These steps are specific for your project and they do not 
have a default implementations.

The next step is to define the feature states. This could be done by using
the default property reader XmlPropertyReader or by implementing your own.
XmlPropertyReader obtains the feature descriptors from XML file which could
be placed in your project.


== 4. Licensing ==

This framework is released under Apache 2.0 Public License. The text of the
license you can find at http://www.apache.org/licenses/LICENSE-2.0.txt.
