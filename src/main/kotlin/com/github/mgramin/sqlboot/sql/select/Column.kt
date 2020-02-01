package com.github.mgramin.sqlboot.sql.select

open class Column(
        private val name: String,
        private val datatype: String,
        private val comment: String,
        private val properties: Map<String, String> = emptyMap()) {

    open fun name() = name

    open fun datatype() = datatype

    open fun comment() = comment

    open fun properties() = properties

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Column

        if (name != other.name) return false
        if (datatype != other.datatype) return false
        if (comment != other.comment) return false
        if (properties != other.properties) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + datatype.hashCode()
        result = 31 * result + comment.hashCode()
        result = 31 * result + properties.hashCode()
        return result
    }

}