package dk.danskespil.gradle.test.spock.helpers

import org.gradle.api.Project

class TemporaryFolderSpecificationTest extends TemporaryFolderSpecification {
    def "easy access to projectbuilder"() {
        given:
        buildFile << """
        task cut() {}
        """

        when:
        Project project = project()

        then:
        project
        project.task('cut')
    }
}
