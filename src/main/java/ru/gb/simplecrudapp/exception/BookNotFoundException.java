package ru.gb.simplecrudapp.exception;

public class BookNotFoundException extends RuntimeException {
    private final String errorCode;

    public BookNotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
