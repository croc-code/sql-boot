package com.github.mgramin.sqlboot.sql.parser

import com.github.mgramin.sqlboot.sql.select.impl.parser.SelectStatementParser
import org.junit.Assert.assertEquals
import org.junit.Test

class SelectStatementParserTest {

    @Test
    fun parse() {
        val selectStatement = SelectStatementParser("""
            select "name"    as "persons_name"   /* name of person {key:value, key2:value2} */
                 , p.age     as persons_age      /* age of person */
                 , address   as "address"        /* address of person */
                 , case when 1=1 then 1 else 0 end as "case_expression_value" /* case column */
              from persons p where 1=1""").parse()

        assertEquals("persons", selectStatement.tableName())

        val iterator = selectStatement.columns().iterator()

        val columnName = iterator.next()
        assertEquals("persons_name", columnName.name())
        assertEquals("name of person {key:value, key2:value2}", columnName.comment())

        val columnAge = iterator.next()
        assertEquals("persons_age", columnAge.name())
        assertEquals("age of person", columnAge.comment())

        val columnAddress = iterator.next()
        assertEquals("address", columnAddress.name())
        assertEquals("address of person", columnAddress.comment())
    }

}