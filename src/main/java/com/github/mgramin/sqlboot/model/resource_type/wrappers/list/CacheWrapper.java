/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.model.resource_type.wrappers.list;

import static java.util.Optional.ofNullable;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.uri.Uri;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class CacheWrapper implements ResourceType {

    private final ResourceType origin;

    private final CachingProvider cachingProvider;
    private final CacheManager cacheManager;
    private final MutableConfiguration<String, List<DbResource>> config;
    private /*final*/ Cache<String, List<DbResource>> cache;

    public CacheWrapper(ResourceType origin) {
        cachingProvider = Caching.getCachingProvider();
        cacheManager = cachingProvider.getCacheManager();
        config = new MutableConfiguration<>();
        cache = cacheManager.getCache("simpleCache");
        if (cache == null) {
            cache = cacheManager.createCache("simpleCache", config);
        }
        this.origin = origin;
    }

    @Override
    public List<String> aliases() {
        return origin.aliases();
    }

    @Override
    public List<String> path() {
        return origin.path();
    }

    @Override
    public Stream<DbResource> read(Uri uri) throws BootException {
        final String cache = ofNullable(uri.params().get("cache")).orElse("true");
        List<DbResource> cachedResources = this.cache.get(uri.toString());
        if (cachedResources == null || cache.equalsIgnoreCase("false")) {
            cachedResources = origin.read(uri).collect(Collectors.toList());
            this.cache.put(uri.toString(), cachedResources);
        }
        return cachedResources.stream();
    }

    @Override
    public Map<String, String> metaData() {
        return origin.metaData();
    }

}
