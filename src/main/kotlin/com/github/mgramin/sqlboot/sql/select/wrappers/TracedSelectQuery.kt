package com.github.mgramin.sqlboot.sql.select.wrappers

import com.github.mgramin.sqlboot.sql.select.SelectQuery
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux

class TracedSelectQuery(
        private val origin: SelectQuery,
        private val enable: Boolean
) : SelectQuery {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun name() = origin.name()

    override fun query() = origin.query()

    override fun execute(variables: Map<String, Any>): Flux<Map<String, Any>> {
        if (enable) logger.info(origin.query())
        return origin.execute(variables)
    }

    override fun properties() = origin.properties()

    override fun columns() = origin.columns()

}