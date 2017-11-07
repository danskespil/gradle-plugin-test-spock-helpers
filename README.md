# gradle-test-spock-helpers
## Status
[![Build Status master branch](https://travis-ci.org/jwermuth/gradle-plugin-test-spock-helpers.svg?branch=master)](https://travis-ci.org/jwermuth/gradle-plugin-test-spock-helpers) Build Status master branch. 
[ ![Download](https://api.bintray.com/packages/jwermuth/oss/gradle-plugin-test-spock-helpers/images/download.svg) ](https://bintray.com/jwermuth/oss/gradle-plugin-test-spock-helpers/_latestVersion) Latest version

## Whats this project for
If you are using gradle and want to start testing your custom tasks or plugins, this is a project for you. 
We are using it at [Danske Spil](https://danskespil.dk/) and [Lund&Bendsen](https://home.lundogbendsen.dk/), perhaps you
can also benefit from it.

## Why should you care
If you get around to writing custom classes, and in particular plugins, it can take a while to figure out how to test them.
The test-approach has changed over time, and even though the documentation is quite good, it can take a while to find all
the right things to do. This project can kick-start you on that journey:

1. you can read the code and see how we have done it
2. You will get a working example, that is being used, and maintained by other people. You can skip some of the 
```
10 get a simple example running
20 meet a real problem
30 read documentation
40 goto 10
```
stuff.

## How does it work
This project aids you by providing a [Spock](http://spockframework.org/) base test class based on the [TemporaryFolder](http://junit.org/junit4/javadoc/4.12/org/junit/rules/TemporaryFolder.html) implementation.
Never heard of Spock! Dont be scared. [Check out the intro](http://spockframework.org/spock/docs/1.1/introduction.html).
Never heard of TemporaryFolder! Its one of the things you want to have when testing. [Check it out](http://junit.org/junit4/javadoc/4.12/org/junit/rules/TemporaryFolder.html) 
or, just add this project to your dependencies and start using it.

## How do I get started
Add this to you build.gradle 
```groovy
repositories {
    jcenter()
}

dependencies {
    testCompile 'dk.danskespil.gradle.plugins:gradle-plugin-test-spock-helpers:0.1.2'
    testCompile('org.spockframework:spock-core:1.0-groovy-2.4') {
        exclude module: 'groovy-all'
    }
}
```
Which allows you to write a test like this
```
class ApplyTest extends TemporaryFolderSpecification {

    def "When calling custom terraform apply task, you can configure which plan file you want to read from "() {
        given:
        buildFile << """
          plugins {
-- Magic -->  id 'dk.danskespil.gradle.plugins.terraform'
          }
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Apply) {
            plan = file('plan.bin')
          }
        """
-- Convenience -->  def f = findOrCreatePathInTemporaryFolder('some-file')
        ...

        when:
-- Shorthand -->  def build = buildWithTasks(':cut')

        then:
        ...
    }

}
```
**Magic** is the easiest way I have found that will put the classes of custom tasks on the build path of the test.
It requires that you define your custom classes in a plugin, even if you do not plan to use them as
a plugin. You can check the example project on how to create a plugin.

**Convenience**
Files are used a lot in tests, so there are some convenience methods for file handling added.   

**Shorthand** is just a shorthand for
```groovy
GradleRunner.create()
 .withPluginClasspath()
 .withProjectDir(projectDir)
 .withArguments(':cut')
```
which is something that you will write again and again when testing gradle custom classes.



#Example project
https://github.com/jwermuth/gradle-plugin-terraform

# Releasing and publishing
This is just for the maintainer (me, for the moment).

## Flow
Want to share something that is close to ready with the world! 
1. ./gradlew clean build release generatePomFileForMyPublicationPublication bintrayUpload -Prelease.stage=milestone -Prelease.scope=patch_or_minor_or_major 
2. Go to bintray https://bintray.com/jwermuth/oss/gradle-plugin-test-spock-helpers and release the new packages

Once happy with a full release, do a final release 
1. ./gradlew clean build release generatePomFileForMyPublicationPublication bintrayUpload -Prelease.stage=final -Prelease.scope=patch_or_minor_or_major
2. Go to bintray https://bintray.com/jwermuth/oss/gradle-plugin-test-spock-helpers and release the new packages

## Commands

* Release, following ajoberstar plugin https://github.com/ajoberstar/gradle-git/wiki/Release%20Plugins
  * ./gradlew clean release -Prelease.scope=major_minor_OR_patch -Prelease.stage=final_OR_rc_OR_milestone_OR_dev
  * e.g. ./gradlew clean release -Prelease.scope=patch -Prelease.stage=final
  * ./gradlew clean release # snapshot version
  * ./gradlew clean release -Prelease.scope=patch -Prelease.stage=dev # e.g. fiddling with readme
* Deployment to bintray.
  * Provide credentials. I have added mine to ~/.gradle/gradle.properties:
    * bintrayUser=USER
    * bintrayApiKey=KEY
  * ./gradlew clean build generatePomFileForMyPublicationPublication bintrayUpload

Releases to bintray are not automatically published, so you have to go to bintray and click your way to the last part.
