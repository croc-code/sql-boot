package com.github.mgramin.sqlboot.model.resourcetypelist

import com.github.mgramin.sqlboot.model.connection.SimpleEndpointList
import com.github.mgramin.sqlboot.model.dialect.DbDialectList
import com.github.mgramin.sqlboot.model.resourcetypelist.impl.FsResourceTypeList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("ResourceTypeRestWrapper")
@ComponentScan(basePackages = ["com.github.mgramin.sqlboot"])
@CrossOrigin
class RestWrapper : ResourceTypeList {

    @Autowired
    private lateinit var endpointList: SimpleEndpointList

    @Autowired
    private lateinit var dbDialectList: DbDialectList

    @RequestMapping(value = ["/api/types"])
    override fun types() = FsResourceTypeList(endpointList.getAll(), dbDialectList.dialects).types()

    @RequestMapping(value = ["/api/{connectionName}/types"])
    fun types(@PathVariable connectionName: String) =
            FsResourceTypeList(endpointList.getByMask(connectionName), dbDialectList.dialects).types()

    @RequestMapping(value = ["/api/{connectionName}/types/{typeMask}"])
    fun types(@PathVariable connectionName: String, @PathVariable typeMask: String) =
            FsResourceTypeList(endpointList.getByMask(connectionName), dbDialectList.dialects).types()
                    .filter { it.name().matches(wildcardToRegex(typeMask)) }

    private fun wildcardToRegex(name: String) = name.replace("?", ".?").replace("*", ".*?").toRegex()

}