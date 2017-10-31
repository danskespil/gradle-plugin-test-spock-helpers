Build Status master branch [![Build Status master branch](https://travis-ci.org/jwermuth/gradle-plugin-test-spock-helpers.svg?branch=master)](https://travis-ci.org/jwermuth/gradle-plugin-test-spock-helpers)
[ ![Download](https://api.bintray.com/packages/jwermuth/org.jwermuth/gradle-plugin-test-spock-helpers/images/download.svg) ](https://bintray.com/jwermuth/org.jwermuth/gradle-plugin-test-spock-helpers/_latestVersion)

# gradle-test-spock-helpers
This project helps us at Danske Spil (and perhaps you ?) to test plugins written in gradle.

If you get around to writing plugins, it can take a while to figure out how to test them.
This project aids you  by providing a base test class based on the *TemporaryFolder* implementation.

What you get is something like this:
```
class TemporaryFolderSpecification extends Specification {
    @Delegate
    static TestHelper testHelper
    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    PathMaker pathMaker = new PathMaker()

    // run before the first feature method
    def setupSpec() {
        testHelper = new TestHelper()
    }

    // run before every feature method
    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        testHelper.testProjectDir = testProjectDir
    }
    ... more helper stuff
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

# Releasing
* Release, following ajoberstar plugin https://github.com/ajoberstar/gradle-git/wiki/Release%20Plugins
  * ./gradlew clean release -Prelease.scope=major_minor_OR_patch -Prelease.stage=final_OR_rc_OR_milestone_OR_dev
  * e.g. ./gradlew clean release -Prelease.scope=patch -Prelease.stage=final
  * ./gradlew clean release # snapshot version
  * ./gradlew clean release -Prelease.scope=patch -Prelease.stage=dev # e.g. fiddling with readme
* Deployment to bintray.
  * ./gradlew clean bintrayUpload
  * $ ./gradlew clean build bintrayUpload -PbintrayUser=USER -PbintrayApiKey=KEY

