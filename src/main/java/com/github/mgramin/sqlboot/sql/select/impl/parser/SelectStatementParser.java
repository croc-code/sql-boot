package com.github.mgramin.sqlboot.sql.select.impl.parser;

import com.github.mgramin.sqlboot.sql.parser.SELECTLexer;
import com.github.mgramin.sqlboot.sql.parser.SELECTParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class SelectStatementParser {

    private final String sql;

    public SelectStatementParser(final String sql) {
        this.sql = sql;
    }

    public SelectStatement parse() {
        final SELECTParser parser = new SELECTParser(
            new CommonTokenStream(
                new SELECTLexer(
                    new ANTLRInputStream(sql))));
        final Object visit = new SelectVisitorCustom().visit(parser.select_statement());
        return (SelectStatement) visit;
    }

}
