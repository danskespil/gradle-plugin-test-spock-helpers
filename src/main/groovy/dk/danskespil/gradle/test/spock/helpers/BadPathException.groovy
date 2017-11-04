package dk.danskespil.gradle.test.spock.helpers

class BadPathException extends RuntimeException{
    BadPathException(String badInput) {
        super("You are trying to parse '${badInput}' as a path. Hm. Does not work. Try something like 'path/to/something'")
    }
}
