package dk.danskespil.gradle.test.spock.helpers

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * This is the recommended way to implement test of customs classes/plugins in gradle
 * See also https://guides.gradle.org/testing-gradle-plugins/
 */
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
