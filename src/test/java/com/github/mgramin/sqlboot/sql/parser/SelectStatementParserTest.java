package com.github.mgramin.sqlboot.sql.parser;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Map.Entry;
import org.junit.Test;

public class SelectStatementParserTest {

    @Test
    public void parse() throws Exception {

        final SelectStatementParser selectStatementParser = new SelectStatementParser(
            "select \"name\" as \"persons_name\" /* name of person */"
             + "      , p.age as persons_age /* age of person */"
             + "      , address as \"address\" /* address of person */"
             + "      , case when 1=1 then 1 else 0 end as \"case_expression_value\" /* case column */"
             + " from persons p where 1=1");
        final SelectStatement selectStatement = selectStatementParser.parse();

        assertEquals("persons", selectStatement.tableName());

        final Iterator<Entry<String, String>> iterator = selectStatement.columns().entrySet().iterator();

        final Entry<String, String> columnName = iterator.next();
        assertEquals("persons_name", columnName.getKey());
        assertEquals("name of person", columnName.getValue());

        final Entry<String, String> columnAge = iterator.next();
        assertEquals("persons_age", columnAge.getKey());
        assertEquals("age of person", columnAge.getValue());

        final Entry<String, String> columnAddress = iterator.next();
        assertEquals("address", columnAddress.getKey());
        assertEquals("address of person", columnAddress.getValue());

        final Entry<String, String> caseExp = iterator.next();
//        assertEquals("address", caseExp.getKey());
//        assertEquals("case_expression_value", caseExp.getValue());
        System.out.println(caseExp);
    }

}