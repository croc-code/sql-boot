package com.github.mgramin.sqlboot.model.resource_type.wrappers.body;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.uri.Uri;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DbStoredFuncBodyWrapper implements ResourceType {

    @Override
    public String name() {
        return null;
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public Stream<DbResource> read(Uri uri) throws BootException {
        return null;
    }

    @Override
    public Map<String, String> metaData() {
        return null;
    }

}
