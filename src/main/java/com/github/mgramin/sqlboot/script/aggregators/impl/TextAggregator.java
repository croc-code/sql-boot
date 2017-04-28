package com.github.mgramin.sqlboot.script.aggregators.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBResource;
import com.github.mgramin.sqlboot.script.aggregators.AbstractAggregator;
import com.github.mgramin.sqlboot.script.aggregators.IAggregator;
import com.github.mgramin.sqlboot.util.template_engine.TemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mgramin on 17.12.2016.
 */
public class TextAggregator extends AbstractAggregator implements IAggregator {

    public TextAggregator(String name, Boolean isDefault, Map<String, String> httpHeaders) {
        this.name = name;
        this.isDefault = isDefault;
        this.httpHeaders = httpHeaders;
    }

    public TextAggregator(String name, Map<String, String> httpHeaders, TemplateEngine templateEngine, String template) {
        this.name = name;
        this.httpHeaders = httpHeaders;
        this.templateEngine = templateEngine;
        this.template = template;
    }

    private String template;
    private TemplateEngine templateEngine;

    @Override
    public byte[] aggregate(List<DBResource> objects) throws SqlBootException {
        if (objects == null) return null;
        if (template == null || template.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (DBResource o : objects) builder.append(o.body).append("\n");
            return builder.toString().getBytes();
        }
        else {
            Map<String, Object> variables = new HashMap<>();
            variables.put("objects", objects);
            templateEngine.setTemplate(template); // TODO move to constructor
            return templateEngine.process(variables).getBytes();
        }
    }

}
