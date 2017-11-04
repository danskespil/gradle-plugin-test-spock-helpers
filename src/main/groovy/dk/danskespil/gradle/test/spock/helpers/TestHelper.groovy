package dk.danskespil.gradle.test.spock.helpers

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.rules.TemporaryFolder

// Shorthands for building a gradle project when testing
class TestHelper {
    TemporaryFolder testProjectDir

    File createNewPath(TemporaryFolder temporaryFolder, String path) {
        File rv = null

        PathSlicer pathSlicer = new PathSlicer(path)
        if (pathSlicer.dirs.size() > 0) {
            rv = temporaryFolder.newFolder(pathSlicer.dirNames)
        }
        if (pathSlicer.fileName) {
            rv = temporaryFolder.newFile(pathSlicer.path)
        }
        rv
    }

    // Easy creation of deep paths with or without files 'at the end'
    File createPathInTemporaryFolder(String path) {
        createNewPath(testProjectDir, path)
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

    boolean existsInTemporaryFolder(String path) {
        path = normalizePath(path)
        return new File(testProjectDir.root.absolutePath + "${path}").exists()
    }

    private String normalizePath(String path) {
        if (!path.startsWith(File.separator)) {
            path = File.separator + path
        }
        path
    }

    File fileInTemporaryFolder(String path) {
        path = normalizePath(path)
        return new File(testProjectDir.root.absolutePath + path)
    }
}
