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

package com.github.mgramin.sqlboot.actions.generator.prepared;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.impl.PlainTextGenerator;
import com.github.mgramin.sqlboot.actions.generator.wrappers.TemplateWrapper;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.util.template_engine.TemplateEngine;

import java.util.Map;

/**
 * Created by maksim on 27.04.17.
 */
public class PlainTextTemplateGenerator implements IActionGenerator {

    public PlainTextTemplateGenerator(String baseText, DBSchemaObjectCommand command, TemplateEngine templateEngine) {
        baseGenerator =
                new TemplateWrapper(
                        new PlainTextGenerator(baseText, command),
                        templateEngine);
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        return baseGenerator.generate(variables);
    }

    @Override
    public DBSchemaObjectCommand command() {
        return baseGenerator.command();
    }

    private final IActionGenerator baseGenerator;

}