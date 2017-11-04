package dk.danskespil.gradle.test.spock.helpers

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class TemporaryFolderFileHelperTest extends Specification {
    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    TemporaryFolderFileHelper cut

    def setup() {
        cut = new TemporaryFolderFileHelper()
        cut.testProjectDir = testProjectDir
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
        File f2 = cut.fileInTemporaryFolder('settings.gradle')
        f2 << "include 'project1'"

        then:
        new File(testProjectDir.root.absolutePath + "/settings.gradle").text.contains('include')
    }

    def "create file"() {
        when:
        File createdFile = cut.createPathInTemporaryFolder('settings.gradle')

        then:
        createdFile.exists()
        fileIsLocatedWhereExpected(createdFile)
    }

    def "find file"() {
        given:
        testProjectDir.newFile('settings.gradle').createNewFile()

        when:
        File f = cut.fileInTemporaryFolder('settings.gradle')

        then:
        f.exists()
    }

    private void fileIsLocatedWhereExpected(File createdFile) {
        File compareFile = new File("${testProjectDir.root.absolutePath}/settings.gradle")
        assert compareFile.absolutePath == createdFile.absolutePath
    }
}
