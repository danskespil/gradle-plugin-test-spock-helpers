[![Build Status master branch](https://travis-ci.org/jwermuth/gradle-plugin-test-spock-helpers.svg?branch=master)](https://travis-ci.org/jwermuth/gradle-plugin-test-spock-helpers) Build Status master branch 

[ ![Download](https://api.bintray.com/packages/jwermuth/oss/gradle-plugin-test-spock-helpers/images/download.svg) ](https://bintray.com/jwermuth/oss/gradle-plugin-test-spock-helpers/_latestVersion) Latest version

**This plugin is not fully publically available yet. If you use the watch feature, I will announce when its operational.**

# gradle-test-spock-helpers
This project helps us at Danske Spil and Lund&Bendsen (and perhaps you ?) to test plugins written in gradle.

If you get around to writing plugins, it can take a while to figure out how to test them.
This project aids you  by providing a base test class based on the *TemporaryFolder* implementation.

What you get is something like this:
```
class TemporaryFolderSpecification extends Specification {
    @Delegate
    static TemporaryFolderFileHelper testHelper
    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    // run before the first feature method
    def setupSpec() {
        testHelper = new TemporaryFolderFileHelper()
    }

    // run before every feature method
    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        testHelper.temporaryFolder = testProjectDir
    }
    BuildResult buildWithTasks(String... tasks) {
        return base(testProjectDir.root, tasks).build()
    }

    BuildResult buildAndFailWithTasks(String... tasks) {
        return base(testProjectDir.root, tasks).buildAndFail()
    }

    private static base(File projectDir, String... tasks) {
        return GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDir)
                .withArguments(tasks)
    }
}
```
Which allows you to write a test like this:
```
class ApplyTest extends TemporaryFolderSpecification {

    def "When calling custom terraform apply task, you can configure which plan file you want to read from "() {
        given:
        buildFile << """
          plugins {
            id 'dk.danskespil.gradle.plugins.terraform'

          }
          task cut(type: dk.danskespil.gradle.plugins.terraform.tasks.Apply) {
            plan = file('plan.bin')
          }
        """
        createPathInTemporaryFolder('plan.bin') << "binary-content"

        when:
        def build = buildWithTasks(':cut')

        then:
        build.output.contains('terraform apply plan.bin')
    }

}

```
by putting something like this in the build.gradle of your project:
```
dependencies {
    compile project(':gradle-task-helpers') // TODO add the correct url once the project is shared
    testCompile project(':gradle-test-spock-helpers')
    testCompile('org.spockframework:spock-core:1.0-groovy-2.4') {
        exclude module: 'groovy-all'
    }
}
```
Why do you care ? You will get a working example, that is being used, and maintained by other people. You can skip some of the 
```
10 get a simple example running
20 meet a real problem
30 read documentation
40 goto 10
```
stuff.

#Example project
https://github.com/jwermuth/gradle-plugin-terraform

# Releasing and publishing
This is just for the maintainer (me, for the moment).

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
