package com.github.mgramin.sqlboot.model.resourcetypelist

import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import javax.cache.Cache
import javax.cache.CacheManager
import javax.cache.Caching
import javax.cache.configuration.MutableConfiguration

class CacheWrapper(private val origin: ResourceTypeList) : ResourceTypeList {

    private val cacheManager: CacheManager = Caching.getCachingProvider().cacheManager
    private val cache: Cache<String, List<ResourceType>> =
            cacheManager.getCache("cachedResourceTypes")
                    ?: cacheManager.createCache("cachedResourceTypes", MutableConfiguration())

    override fun types(): List<ResourceType> {
        val get = cache.get("allTypes")
//        return (if (get != null) get else origin.types())!!
        if (get != null) {
            return get
        } else {
            val types = origin.types()
            cache.put("allTypes", types)
            return types
        }
    }


}