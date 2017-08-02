package com.github.mgramin.sqlboot.resource_type.wrappers.list;

import static java.util.stream.Collectors.toList;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.resource_type.ResourceType;
import com.github.mgramin.sqlboot.uri.Uri;
import java.util.List;

/**
 * Created by MGramin on 18.07.2017.
 */
public class WhereWrapper implements ResourceType {

    private final ResourceType origin;

    public WhereWrapper(ResourceType origin) {
        this.origin = origin;
    }

    @Override
    public String name() {
        return origin.name();
    }

    @Override
    public List<String> aliases() {
        return origin.aliases();
    }

    @Override
    public List<DbResource> read(Uri uri) throws BootException {
        final List<DbResource> resources = origin.read(uri);
        return resources.stream()
            .filter(resource -> {
                for (int i = 0; i < uri.objects().size(); i++) {
                    boolean contains = resource.dbUri().objects().get(i).toLowerCase()
                        .contains(uri.objects().get(i).toLowerCase());
                    if (!contains)
                        return false;
                }
                return true;
            })
            .collect(toList());
    }

}
