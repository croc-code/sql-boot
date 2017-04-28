package com.github.mgramin.sqlboot.util.template_engine;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import java.util.List;
import java.util.Map;

/**
 * Template engine
 */
public interface TemplateEngine {

    /**
     * Generate text from template
     *
     * @param variables
     * @return
     */
    String process(Map<String, Object> variables) throws SqlBootException;

    /**
     * Get all user variables from template in sequence order
     *
     * @return
     * @throws SqlBootException
     */
    List<String> getAllProperties() throws SqlBootException;

    /**
     * Set and prepare template text
     *
     * @param template
     */
    @Deprecated // TODO set template in constructor
    void setTemplate(String template);

}
