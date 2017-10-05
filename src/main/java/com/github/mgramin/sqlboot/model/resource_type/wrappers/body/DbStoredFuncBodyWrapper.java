package com.github.mgramin.sqlboot.model.resource_type.wrappers.body;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.uri.Uri;
import java.util.List;

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
    public List<DbResource> read(Uri uri) throws BootException {
        return null;
    }

}
