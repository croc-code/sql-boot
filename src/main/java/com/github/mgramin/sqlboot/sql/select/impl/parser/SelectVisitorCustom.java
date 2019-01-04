/*
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2019 Maksim Gramin
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.sql.select.impl.parser;

import static java.util.Optional.ofNullable;

import com.github.mgramin.sqlboot.sql.select.impl.parser.SELECTParser.Select_statementContext;
import com.github.mgramin.sqlboot.sql.select.impl.parser.SelectStatement.Column;
import java.util.List;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.RuleContext;

public class SelectVisitorCustom extends SELECTBaseVisitor {

    @Override
    public Object visitSelect_statement(final Select_statementContext ctx) {
        final List<Column> columns = ctx.select_row().stream()
            .map(v -> new SelectStatement.Column(
                ofNullable(v.column_alias()).map(a -> a.ID().getText())
                    .orElse(ofNullable(v.column_name()).map(a -> a.ID().getText()).orElse("NULL")),
                ofNullable(v.column_comment())
                    .map(v1 -> v1.MULTIPLE_LINE_COMMENT().getText()
                        .replace("/*", "")
                        .replace("*/", "").trim())
                    .orElse(""), null))
            .collect(Collectors.toList());
        return new SelectStatement(
            ofNullable(ctx.table_name()).map(RuleContext::getText).orElse("TABLE NOT DEFINE"),
            columns);
    }

}
