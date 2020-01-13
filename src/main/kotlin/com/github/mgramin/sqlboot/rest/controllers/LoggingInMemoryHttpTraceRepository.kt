package com.github.mgramin.sqlboot.rest.controllers

import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.trace.http.HttpTrace
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository
import org.springframework.stereotype.Repository
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Repository
open class LoggingInMemoryHttpTraceRepository : InMemoryHttpTraceRepository() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun add(trace: HttpTrace) {
        super.add(trace)
        logger.info("Time taken ${URLDecoder.decode(trace.request.uri.toASCIIString(), StandardCharsets.UTF_8.toString())} = ${trace.timeTaken}")
    }

}