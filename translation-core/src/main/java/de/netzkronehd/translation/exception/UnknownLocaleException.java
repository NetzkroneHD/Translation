package de.netzkronehd.translation.exception;

public class UnknownLocaleException extends Exception {

    public UnknownLocaleException(String message) {
        super(message);
    }

    public UnknownLocaleException(Throwable cause) {
        super(cause);
    }
}
