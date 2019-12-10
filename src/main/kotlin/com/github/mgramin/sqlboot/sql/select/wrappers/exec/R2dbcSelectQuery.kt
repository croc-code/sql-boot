package com.github.mgramin.sqlboot.sql.select.wrappers.exec

import com.github.mgramin.sqlboot.sql.select.SelectQuery
import reactor.core.publisher.Flux

class R2dbcSelectQuery(private val origin: SelectQuery) : SelectQuery {

    override fun name() = origin.name()

    override fun query() = origin.query()

    override fun properties() = origin.properties()

    override fun columns() = origin.columns()

    override fun execute(variables: Map<String, Any>): Flux<Map<String, Any>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}