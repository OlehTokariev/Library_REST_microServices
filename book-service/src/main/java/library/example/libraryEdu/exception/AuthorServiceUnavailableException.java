package library.example.libraryEdu.exception;

public class AuthorServiceUnavailableException extends RuntimeException {
    public AuthorServiceUnavailableException(String message) {
        super(message);
    }
}
