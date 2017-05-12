/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 mgramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.github.mgramin.sqlboot.template_engine.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.template_engine.TemplateEngine;
import groovy.text.Template;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MGramin on 26.02.2017.
 */
public abstract class AbstractGroovyTemplateEngine implements TemplateEngine {

    protected groovy.text.TemplateEngine engine;
    protected Template template;
    protected String templateText;

    @Deprecated
    protected void setTemplate(String template) {
        try {
            this.templateText = template;
            this.template = engine.createTemplate(template.replace("!{", "${"));
        } catch (ClassNotFoundException | IOException e) {
            throw new SqlBootException(e);
        }
    }

    @Override
    public String process(Map<String, Object> variables) {
        return this.template.make(variables).toString();
    }

    @Override
    public List<String> getAllProperties() throws SqlBootException {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$\\s*(\\w+)");
        Matcher matcher = pattern.matcher(templateText.replace("!", "$"));
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

}
