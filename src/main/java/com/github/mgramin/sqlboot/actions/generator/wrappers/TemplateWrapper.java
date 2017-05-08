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

package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DbResourceCommand;
import com.github.mgramin.sqlboot.template_engine.TemplateEngine;
import com.github.mgramin.sqlboot.template_engine.TemplateEngineFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 19.04.17.
 */
public class TemplateWrapper implements ActionGenerator {

    private final ActionGenerator origin;
    private final TemplateEngineFactory templateEngineFactory;

    public TemplateWrapper(ActionGenerator origin, TemplateEngineFactory templateEngineFactory) {
        this.origin = origin;
        this.templateEngineFactory = templateEngineFactory;
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        final String baseText = origin.generate(variables);
        final TemplateEngine templateEngine = templateEngineFactory.create(baseText);
        return templateEngine.process(variables);
    }

    @Override
    public String generate(List<Object> variables) throws SqlBootException {
        final String baseText = origin.generate(variables);
        final TemplateEngine templateEngine = templateEngineFactory.create(baseText);
        final Map<String, Object> data = new HashMap();
        // TODO clean this code
        int i = 0;
        for (String prop : templateEngine.getAllProperties()) {
            try {
                data.put(prop, variables.get(i++));
            } catch (Throwable t) {
                data.put(prop, '%');
            }
        }
        return templateEngine.process(data);
    }


    @Override
    public DbResourceCommand command() {
        return origin.command();
    }

}
