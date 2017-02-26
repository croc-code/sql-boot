package com.github.mgramin.sqlboot.util.template_engine;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;

import java.util.List;
import java.util.Map;

/**
 * Template engine
 */
public interface ITemplateEngine {

    /**
     * Set and prepare template text
     *
     * @param template
     */
    void setTemplate(String template);

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
     * @param templateText
     * @return
     * @throws SqlBootException
     */
    List<String> getAllProperties(@Deprecated String templateText) throws SqlBootException;

}
