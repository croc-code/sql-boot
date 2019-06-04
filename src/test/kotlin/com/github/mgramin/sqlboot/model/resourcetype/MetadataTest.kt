package com.github.mgramin.sqlboot.model.resourcetype

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MetadataTest {

    @Test
    fun name() = assertEquals("name", metadata().name())

    @Test
    fun description() = assertEquals("""{ "key_1": "value_1" }""", metadata().description())

    @Test
    fun properties() = assertEquals(mapOf("key_1" to "value_1", "key" to "name"), metadata().properties())

    @Test
    fun compareTo() {
        assertEquals(1, metadata().compareTo(metadata()))
    }

    @Test
    fun toJson() = assertEquals(
            """{"name":"name","description":"{ \"key_1\": \"value_1\" }","value":"name","text":"name","properties":{"key_1":"value_1","key":"name"}}""",
            metadata().toJson().toString())

    private fun metadata() = Metadata("@name", "String", """{ "key_1": "value_1" }""")

}