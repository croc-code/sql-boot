package com.github.mgramin.sqlboot.model.resourcetypelist

import com.github.mgramin.sqlboot.model.resourcetype.ResourceType

interface ResourceTypeList {

    fun types() :List<ResourceType>

}