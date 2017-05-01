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
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import lombok.ToString;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

/**
 * Created by maksim on 05.04.16.
 */
@ToString
public class SQLWrapper implements ActionGenerator {

    public SQLWrapper(ActionGenerator baseGenerator, ISqlHelper sqlHelper) {
        this.baseGenerator = baseGenerator;
        this.sqlHelper = sqlHelper;
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        List<Map<String, String>> maps = sqlHelper.selectBatch(singletonList(baseGenerator.generate(variables)));
        return maps.get(0).entrySet().iterator().next().getValue();
    }

    @Override
    public DBSchemaObjectCommand command() {
        return baseGenerator.command();
    }

    final private ActionGenerator baseGenerator;
    final private ISqlHelper sqlHelper;

}