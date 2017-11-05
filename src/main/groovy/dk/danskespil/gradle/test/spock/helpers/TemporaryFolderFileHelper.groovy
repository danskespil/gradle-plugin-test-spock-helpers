package dk.danskespil.gradle.test.spock.helpers

import org.junit.rules.TemporaryFolder

// Shorthands for building a gradle project when testing
class TemporaryFolderFileHelper {
    TemporaryFolder temporaryFolder

    File createPathInTemporaryFolder(String path) {
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
    boolean existsInTemporaryFolder(String path) {
        path = normalizePath(path)
        return new File(temporaryFolder.root.absolutePath + "${path}").exists()
    }

    File findPathInTemporaryFolder(String path) {
        path = normalizePath(path)
        return new File(temporaryFolder.root.absolutePath + path)
    }

    File findOrCreatePathInTemporaryFolder(String path) {
        File rv
        if (existsInTemporaryFolder(path)) {
            rv = findPathInTemporaryFolder(path)
        } else {
            rv = createPathInTemporaryFolder(path)
        }
        rv
    }

    /*
     * @Deprecated use findPathInTemporaryFolder instead
     */
    @Deprecated
    File fileInTemporaryFolder(String path) {
        findPathInTemporaryFolder(path)
    }

    private String normalizePath(String path) {
        if (!path.startsWith(File.separator)) {
            path = File.separator + path
        }
        path
    }
}
