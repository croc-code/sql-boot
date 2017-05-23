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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.actions.generator.impl;

import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.IDbResourceCommand;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version 0.1.0
 * @since 0.1.0
 */
public final class PlainTextGenerator implements ActionGenerator {

    private final String baseText;
    private final IDbResourceCommand IDbResourceCommand;
    private final String aggregators;

    public PlainTextGenerator(String baseText, IDbResourceCommand command, String aggregators) {
        this.baseText = baseText;
        this.IDbResourceCommand = command;
        this.aggregators = aggregators;
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        return baseText;
    }

    @Override
    public String generate(List<Object> variables) throws SqlBootException {
        return baseText;
    }

    @Override
    public IDbResourceCommand command() {
        return this.IDbResourceCommand;
    }

    @Override
    public String aggregators() {
        return this.aggregators;
    }

}
