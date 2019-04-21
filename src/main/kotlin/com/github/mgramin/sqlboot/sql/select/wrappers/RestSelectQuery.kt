package com.github.mgramin.sqlboot.sql.select.wrappers

import com.github.mgramin.sqlboot.sql.select.SelectQuery
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux


class RestSelectQuery(
        private val origin: SelectQuery,
        private val endpoint: String
) : SelectQuery {

    override fun properties() = origin.properties()

    override fun query() = origin.query()

    override fun columns() = origin.columns()

    override fun execute(variables: Map<String, Any>): Flux<Map<String, Any>> {
        val client: List<Map<String, Any>>? = WebClient
                .create(endpoint)
                .post()
                .uri("/exec?query=${origin.query()}".replace("{", "[").replace("}", "]"))
                .body(BodyInserters.fromObject(origin.query()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String::class.java)
                .map {
                    val resultType = object : TypeToken<List<Map<String, Any>>>() {}.type
                    return@map Gson().fromJson<List<Map<String, Any>>?>(it, resultType)
                }
                .block()
        return client!!.toFlux()
    }

}