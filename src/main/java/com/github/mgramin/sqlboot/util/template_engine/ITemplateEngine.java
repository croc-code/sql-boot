package com.github.mgramin.sqlboot.util.template_engine;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import freemarker.template.TemplateModelException;

import java.util.List;
import java.util.Map;

/**
 * Template engine
 */
public interface ITemplateEngine {

    /**
     * Generate text from template
     * @param variables
     * @param template
     * @return
     */
    String process(Map<String, Object> variables, String template) throws SqlBootException;

    /**
     * Get all user variables from template in sequence order
     * @param templateText
     * @return
     * @throws TemplateModelException
     */
    List<String> getAllProperties(String templateText) throws SqlBootException;

}
