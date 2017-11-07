package dk.danskespil.gradle.test.spock.helpers

import org.junit.rules.TemporaryFolder

/**
 * Easy creation and finding of deep paths with or without files 'at the end', e.g. some/dir/ or some/file
 * starting from the temporary folder, so you do not have to worry about where the folder is.
 */
class TemporaryFolderFileHelper {
    TemporaryFolder temporaryFolder
    /**
     * @param path to file, starting from the temporary folder
     * @return the File
     */
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

    /**
     * @param path to file, starting from the temporary folder
     * @return true if the file exists
     */
    boolean existsInTemporaryFolder(String path) {
        path = normalizePath(path)
        return new File(temporaryFolder.root.absolutePath + "${path}").exists()
    }

    /**
     * @param path to file, starting from the temporary folder
     * @return File
     */
    File findPathInTemporaryFolder(String path) {
        path = normalizePath(path)
        return new File(temporaryFolder.root.absolutePath + path)
    }

    /**
     * @param path to file, starting from the temporary folder
     * @return File
     */
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
