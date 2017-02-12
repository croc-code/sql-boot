package com.github.mgramin.sqlboot.exceptions;

/**
 * Created by maksim on 12.06.16.
 */
public class SqlBootException extends RuntimeException {

    public SqlBootException(String message) {
        super(message);
    }

    public SqlBootException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlBootException(Throwable cause) {
        super(cause);
    }

}
