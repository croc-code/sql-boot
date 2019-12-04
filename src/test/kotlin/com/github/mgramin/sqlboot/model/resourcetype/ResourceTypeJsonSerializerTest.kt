package com.github.mgramin.sqlboot.model.resourcetype

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mgramin.sqlboot.Application
import com.github.mgramin.sqlboot.model.resourcetype.impl.FakeResourceType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ResourceTypeJsonSerializerTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun serialize() {
        val fakeResourceType = FakeResourceType()
//        val writeValueAsString = objectMapper.writeValueAsString(fakeResourceType.toJson())
//        println(writeValueAsString)
    }

}