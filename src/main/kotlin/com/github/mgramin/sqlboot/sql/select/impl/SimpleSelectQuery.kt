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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream

class SimpleSelectQuery(private val templateGenerator: TemplateGenerator) : SelectQuery {

    override fun query() = templateGenerator.template()

    override fun properties(): Map<String, String> {
        val comment: String = SelectStatementParser(templateGenerator.template()).comment().replace("/*", "").replace("*/", "")
        return try {
            Gson().fromJson(comment, object : TypeToken<Map<String, String>>() {}.type)
        } catch (e: Exception) {
            emptyMap()
        }
    }

    override fun columns() =
            SelectStatementParser(templateGenerator.template())
                    .parse()
                    .map { it.name() to it.comment() }
                    .toMap()

    override fun execute(variables: Map<String, Any>) = throw RuntimeException("Not allow here")


    private class SelectStatementParser(sql: String) {

        val parser = SELECTParser(CommonTokenStream(SELECTLexer(ANTLRInputStream(sql))))
        val selectVisitorCustom = SelectVisitorCustom()
        val visit = selectVisitorCustom.visit(parser.select_statement())

        fun comment() = selectVisitorCustom.queryComment

        fun parse(): List<SelectQuery.Column> {
            return visit as List<SelectQuery.Column>
        }
    }


    private class SelectVisitorCustom : SELECTBaseVisitor<Any>() {

        var queryComment: String = ""

        override fun visitSelect_statement(ctx: SELECTParser.Select_statementContext): Any {
            queryComment = ctx.query_comment().text
            return ctx.select_row()
                    .map { v ->
                        SelectQuery.Column(
                                v.column_alias()?.ID()?.text
                                        ?: (v.column_name()?.ID()?.text ?: "NULL"),
                                v.column_comment()?.let { v1 ->
                                    v1.MULTIPLE_LINE_COMMENT().text.replace("/*", "").replace("*/", "").trim { it <= ' ' }
                                } ?: (""), hashMapOf())
                    }
                    .toList()
        }
    }

}