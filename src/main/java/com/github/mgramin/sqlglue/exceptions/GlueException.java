package com.github.mgramin.sqlglue.exceptions;

/**
 * Created by maksim on 12.06.16.
 */
public class GlueException extends Exception {

    public GlueException(String message) {
        super(message);
    }

    public GlueException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlueException(Throwable cause) {
        super(cause);
    }

}
