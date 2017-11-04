package dk.danskespil.gradle.test.spock.helpers
/**
 * Helper to make it easier to create nested filestructures with TemporaryFolder
 * See tests on how to use. Someting along the lines of (again, see tests)
 *
 * new TemporaryFolder().newFolder(new PathSlicer('some/path/and/perhaps/a/file.txt').dirNames)
 *
 */
class PathSlicer {
    ArrayList<String> dirs
    String fileName
    String path
    String[] dirNames

    PathSlicer(String path) {
        if (!path || path == File.separator || path.contains('\\') || path.startsWith(File.separator)) {
            throw new BadPathException(path)
        }

        this.path = path
        dirs = (List<String>) Arrays.asList(path.split(File.separator))

        if (!path.endsWith(File.separator)) {
            fileName = dirs.remove(dirs.size() - 1)
        }

        dirNames = dirs.toArray(new String[dirs.size()])
    }
}
