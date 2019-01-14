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

package com.github.mgramin.sqlboot.sql.select.impl.parser

import com.github.mgramin.sqlboot.sql.select.impl.parser.SELECTParser.Select_statementContext
import java.util.HashMap
import java.util.Optional.ofNullable

class SelectVisitorCustom : SELECTBaseVisitor<Any>() {

    override fun visitSelect_statement(ctx: Select_statementContext): Any {
        val columns = ctx.select_row()
                .map { v ->
                    SelectStatement.Column(
                            ofNullable(v.column_alias()).map<String> { a -> a.ID().text }
                                    .orElse(ofNullable(v.column_name()).map<String> { a -> a.ID().text }.orElse("NULL")),
                            ofNullable(v.column_comment())
                                    .map<String> { v1 ->
                                        v1.MULTIPLE_LINE_COMMENT().text
                                                .replace("/*", "")
                                                .replace("*/", "").trim { it <= ' ' }
                                    }
                                    .orElse(""), HashMap())
                }
                .toList()
        return SelectStatement(
                ofNullable(ctx.table_name()).map<String> { it.text }.orElse("TABLE NOT DEFINE"),
                columns)
    }
}
