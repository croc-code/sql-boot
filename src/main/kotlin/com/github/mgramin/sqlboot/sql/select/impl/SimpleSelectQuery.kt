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

package com.github.mgramin.sqlboot.sql.select.impl

import com.github.mgramin.sqlboot.sql.select.SelectQuery
import com.github.mgramin.sqlboot.sql.select.impl.parser.SELECTBaseVisitor
import com.github.mgramin.sqlboot.sql.select.impl.parser.SELECTLexer
import com.github.mgramin.sqlboot.sql.select.impl.parser.SELECTParser
import com.github.mgramin.sqlboot.template.generator.TemplateGenerator
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream

class SimpleSelectQuery(private val templateGenerator: TemplateGenerator) : SelectQuery {

    override fun query(): String {
        return templateGenerator.template()
    }

    override fun columns(): Map<String, String> {
        return SelectStatementParser(templateGenerator.template())
                .parse()
                .map { it.name() to it.comment() }
                .toMap()
    }

    override fun execute(variables: Map<String, Any>): Sequence<Map<String, Any>> {
        throw RuntimeException("Not allow here")
    }


    private class SelectStatementParser(private val sql: String) {

        fun parse(): List<SelectQuery.Column> {
            val parser = SELECTParser(
                    CommonTokenStream(
                            SELECTLexer(
                                    ANTLRInputStream(sql))))
            val visit = SelectVisitorCustom().visit(parser.select_statement())
            return visit as List<SelectQuery.Column>
        }
    }


    private class SelectVisitorCustom : SELECTBaseVisitor<Any>() {

        override fun visitSelect_statement(ctx: SELECTParser.Select_statementContext): Any {
            val columns = ctx.select_row()
                    .map { v ->
                        SelectQuery.Column(
                                v.column_alias()?.ID()?.text
                                        ?: (v.column_name()?.ID()?.text ?: "NULL"),
                                v.column_comment()?.let { v1 ->
                                    v1.MULTIPLE_LINE_COMMENT().text.replace("/*", "").replace("*/", "").trim { it <= ' ' }
                                } ?: (""), hashMapOf())
                    }
                    .toList()
            return columns
        }
    }

}