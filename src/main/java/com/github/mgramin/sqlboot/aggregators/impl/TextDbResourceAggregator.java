/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.aggregators.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.mgramin.sqlboot.aggregators.AbstractDbResourceAggregator;
import com.github.mgramin.sqlboot.aggregators.DbResourceAggregator;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.template.TemplateEngine;
import com.github.mgramin.sqlboot.template.TemplateEngineFactory;

/**
 * Created by mgramin on 17.12.2016.
 */
public final class TextDbResourceAggregator extends AbstractDbResourceAggregator implements DbResourceAggregator {

    private final String template;
    private final TemplateEngineFactory templateEngineFactory;

    public TextDbResourceAggregator(String name, Boolean isDefault) {
        this.name = name;
        this.isDefault = isDefault;
        this.templateEngineFactory = null;
        this.template = null;
    }

    public TextDbResourceAggregator(String name, TemplateEngineFactory templateEngineFactory, String template) {
        this.name = name;
        this.templateEngineFactory = templateEngineFactory;
        this.template = template;
    }

    @Override
    public byte[] aggregate(List<DbResource> objects) throws BootException {
        if (objects == null) return null;
        if (template == null || template.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (DbResource o : objects) builder.append(o.body()).append("\n");
            return builder.toString().getBytes();
        }
        else {
            Map<String, Object> variables = new HashMap<>();
            variables.put("objects", objects);
            TemplateEngine templateEngine = templateEngineFactory.create(template);
            return templateEngine.process(variables).getBytes();
        }
    }

}
