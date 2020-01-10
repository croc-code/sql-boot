package com.github.mgramin.sqlboot.rest.controllers

import org.apache.commons.lang3.builder.ToStringBuilder
import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.trace.http.HttpTrace
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository
import org.springframework.stereotype.Repository

@Repository
open class LoggingInMemoryHttpTraceRepository : InMemoryHttpTraceRepository() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun add(trace: HttpTrace) {
        super.add(trace)
        logger.trace("Trace:" + ToStringBuilder.reflectionToString(trace))
        logger.info("Request:" + ToStringBuilder.reflectionToString(trace.request))
        logger.trace("Response:" + ToStringBuilder.reflectionToString(trace.response))
    }

}