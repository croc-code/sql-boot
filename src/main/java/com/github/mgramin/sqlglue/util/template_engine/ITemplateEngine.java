package com.github.mgramin.sqlglue.util.template_engine;

import com.github.mgramin.sqlglue.exceptions.GlueException;
import freemarker.template.TemplateModelException;

import java.util.List;
import java.util.Map;

/**
 * Created by home on 25.01.2015.
 */
public interface ITemplateEngine {

    /**
     * Generate text from template and variables
     * @param variables
     * @param template
     * @return
     */
    String process(Map<String, Object> variables, String template) throws GlueException;

    /**
     * Get all variables from template in order
     * @param templateText
     * @return
     * @throws TemplateModelException
     */
    List<String> getAllProperties(String templateText) throws GlueException;

}
