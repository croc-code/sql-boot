package com.github.mgramin.sqlboot.resource_type.impl.file;

import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.model.IDbResourceCommand;
import com.github.mgramin.sqlboot.model.Uri;
import com.github.mgramin.sqlboot.resource_type.ResourceType;
import java.util.List;

/**
 * Created by MGramin on 12.07.2017.
 */
public class FileResourceType implements ResourceType {

    @Override
    public String name() {
        return null;
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public List<ActionGenerator> generators() {
        return null;
    }

    @Override
    public List<DbResource> read(Uri uri, IDbResourceCommand command, String aggregatorName)
        throws BootException {
        return null;
    }
}