package com.github.mgramin.sqlboot.script.aggregators;

import java.util.Map;

/**
 * Created by mgramin on 08.01.2017.
 */
public abstract class AbstractAggregator implements IAggregator {

    protected String name;
    protected Boolean isDefault;
    protected Map<String, String> httpHeaders;


    public String getName() {
        return name;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

}
