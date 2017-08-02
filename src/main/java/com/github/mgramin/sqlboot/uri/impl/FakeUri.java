package com.github.mgramin.sqlboot.uri.impl;

import com.github.mgramin.sqlboot.uri.Uri;
import java.util.List;
import java.util.Map;

public class FakeUri implements Uri {

    @Override
    public String type() {
        return null;
    }

    @Override
    public String action() {
        return null;
    }

    @Override
    public List<String> objects() {
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
    public Map<String, String> filters() {
        return null;
    }

}
