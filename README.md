## Flip - A feature toggling framework

Flip makes it simple to use [Feature Toggles](http://martinfowler.com/bliki/FeatureToggle.html),
(also known as Feature Flags, Feature Bits, Feature Switches, etc) in your Java application.

Feature toggles can be used for many purposes, including:

1. Reducing the need to branch by feature in your source control system by having all work done on the main code line.
   Features that are not ready for production can simply be disabled.
2. New features can be rolled out to a subset of users (e.g. by group membership, canary testing, etc)
2. Should a feature become problematic, you can turn it off without requiring a build and deploy.

# Start Using Flip

Flip is available in Maven Central under the following identifiers:

* for basic Java applications: [com.tacitknowledge.flip:core:1.0](http://search.maven.org/#artifactdetails%7Ccom.tacitknowledge.flip%7Ccore%7C1.0%7Cjar)
* for servlet applications: [com.tacitknowledge.flip:servlet:1.0](http://search.maven.org/#artifactdetails%7Ccom.tacitknowledge.flip%7Cservlet%7C1.0%7Cjar)
* for Spring MVC applications: [com.tacitknowledge.flip:spring:1.0](http://search.maven.org/#artifactdetails%7Ccom.tacitknowledge.flip%7Cspring%7C1.0%7Cjar)

If you're using Ant we recommend that you use Apache Ivy. If you can't do that for whatever reason, take a look at our
[instructions for installation under Ant](https://github.com/tacitknowledge/flip/wiki/Installation-under-Ant).

The quickest way to understand how to use Flip is by looking at the [examples](https://github.com/tacitknowledge/flip/tree/master/examples).

# Documentation

The documentation for Flip is on the [Flip Wiki](https://github.com/tacitknowledge/flip/wiki)

# Licensing

This framework is released under Apache 2.0 Public License. You can find the license text at http://www.apache.org/licenses/LICENSE-2.0.txt.

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the remote branch (`git push origin my-new-feature`)
5. Create a new Pull Request
