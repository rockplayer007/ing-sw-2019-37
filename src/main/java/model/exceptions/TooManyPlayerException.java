package model.exceptions;

public class TooManyPlayerException extends RuntimeException {
    public TooManyPlayerException(String s) {
        super(s);
    }
}

