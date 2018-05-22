package com.github.mgramin.sqlboot.sql.select.impl.parser;

import static java.util.Optional.ofNullable;

import com.github.mgramin.sqlboot.sql.select.impl.parser.SELECTBaseVisitor;
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
                    .orElse(/*v.column_name().ID().getText()*/"NULL"),
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
