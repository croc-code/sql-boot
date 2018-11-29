package com.github.mgramin.sqlboot.model.uri.wrappers;

import com.github.mgramin.sqlboot.model.uri.Uri;
import java.util.List;
import java.util.Map;

/**
 * Parse object uri from full http-request
 */
public class HttpParserWrapper implements Uri {

    @Override
    public String type() {
        return null;
    }

    @Override
    public List<String> path() {
        return null;
    }

    @Override
    public String path(Integer index) {
        return null;
    }

    @Override
    public Boolean recursive() {
        return null;
    }

    @Override
    public Map<String, String> params() {
        return null;
    }

    @Override
    public String action() {
        return null;
    }

}
