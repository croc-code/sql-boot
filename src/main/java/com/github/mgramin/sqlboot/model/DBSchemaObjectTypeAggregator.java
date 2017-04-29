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

package com.github.mgramin.sqlboot.model;

import static java.util.Arrays.asList;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by MGramin on 09.01.2017.
 */
public class DBSchemaObjectTypeAggregator {

    private List<String> aggregatorName;
    private List<IActionGenerator> commands;


    public List<String> getAggregatorName() {
        return aggregatorName;
    }

    public void setAggregatorName(String[] aggregatorName) {
        this.aggregatorName = asList(aggregatorName);
    }

    public List<IActionGenerator> getCommands() {
        return commands;
    }

    public void setCommands(List<IActionGenerator> commands) {
        this.commands = commands;
    }
}