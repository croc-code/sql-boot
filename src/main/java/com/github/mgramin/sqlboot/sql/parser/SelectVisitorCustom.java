package com.github.mgramin.sqlboot.sql.parser;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

import com.github.mgramin.sqlboot.sql.parser.SELECTParser.Select_statementContext;
import org.antlr.v4.runtime.RuleContext;
import java.util.LinkedHashMap;
import java.util.Map;

public class SelectVisitorCustom extends SELECTBaseVisitor {

    @Override
    public Object visitSelect_statement(final Select_statementContext ctx) {
        final Map<String, String> columns = ctx.select_row().stream().collect(
            toMap(v -> ofNullable(v.column_alias()).map(a -> a.ID().getText())
                    .orElse(v.column_name().ID().getText()),
                v -> ofNullable(v.column_comment())
                    .map(v1 -> v1.MULTIPLE_LINE_COMMENT().getText()
                        .replace("/*", "")
                        .replace("*/", "").trim())
                    .orElse(""),
                (a, b) -> a,
                LinkedHashMap::new));

        return new SelectStatement(ofNullable(ctx.table_name()).map(RuleContext::getText).orElse("TABLE NOT DEFINE"), columns);
    }

}
