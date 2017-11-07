package dk.danskespil.gradle.test.spock.helpers

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

class TemporaryFolderFileHelperTest extends Specification {
    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    TemporaryFolderFileHelper cut

    def setup() {
        cut = new TemporaryFolderFileHelper()
        cut.temporaryFolder = testProjectDir
    }

    def "create and write to file"() {
        when:
        cut.createPathInTemporaryFolder('settings.gradle') << "include 'project1'"

        then:
        new File(testProjectDir.root.absolutePath + "/settings.gradle").text.contains("include 'project1'")
    }

    def "write to existing file"() {
        given:
        testProjectDir.newFile('settings.gradle').createNewFile()

        when:
        File f2 = cut.findPathInTemporaryFolder('settings.gradle')
        f2 << "include 'project1'"

        then:
        new File(testProjectDir.root.absolutePath + "/settings.gradle").text.contains('include')
    }

    def "Create dir"() {
        when:
        File createdDir = cut.createPathInTemporaryFolder('dir/')

        then:
        createdDir.exists()
        createdDir.isDirectory()
    }

    @Unroll
    def "create #file"() {
        when:
        File createdFile = cut.createPathInTemporaryFolder(file)

        then:
        createdFile.exists()
        fileIsLocatedWhereExpected(file, createdFile)

        where:
        file << ['settings.gradle', 'a/b/c/file']
    }

    def "find file"() {
        given:
        testProjectDir.newFile('settings.gradle').createNewFile()

        when:
        File f = cut.findPathInTemporaryFolder('settings.gradle')

        then:
        f.exists()

    }

    def "find a file, or create it, if it does not exist"() {
        given:
        testProjectDir.newFile('settings.gradle').createNewFile()

        when:
        File wasThereBefore = cut.findOrCreatePathInTemporaryFolder('settings.gradle')
        File wasNotThereBefore = cut.findOrCreatePathInTemporaryFolder('nonexisting')

        then:
        wasNotThereBefore.exists()
        wasThereBefore.exists()
    }

    private void fileIsLocatedWhereExpected(String pathInTemporaryFolder, File createdFile) {
        File compareFile = new File("${testProjectDir.root.absolutePath}/${pathInTemporaryFolder}")
        assert compareFile.absolutePath == createdFile.absolutePath
    }
}
