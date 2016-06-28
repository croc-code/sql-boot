package com.github.mgramin.sqlglue.exceptions;

/**
 * Created by maksim on 12.06.16.
 */
public class TemplateEngineException extends Exception {

    public TemplateEngineException(String message) {
        super(message);
    }

    public TemplateEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateEngineException(Throwable cause) {
        super(cause);
    }

}
