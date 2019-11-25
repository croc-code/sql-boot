package com.github.mgramin.sqlboot.sql.select.wrappers

import com.github.mgramin.sqlboot.sql.select.SelectQuery

class GrafanaSelectQuery(
        private val origin: SelectQuery
) : SelectQuery {

    override fun name() = origin.name()

    override fun query() = origin.query()
            .replace("${"$"}__timeFilter(time)", "1=1")
            .replace("observable_endpoint in (${"$"}observable_endpoint)", "1=1")

    override fun execute(variables: Map<String, Any>) = origin.execute(variables)

    override fun properties() = origin.properties()

    override fun columns() = origin.columns()

}