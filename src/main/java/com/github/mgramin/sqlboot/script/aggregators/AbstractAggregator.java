package com.github.mgramin.sqlboot.script.aggregators;

import java.util.Map;

/**
 * Created by mgramin on 08.01.2017.
 */
public abstract class AbstractAggregator implements IAggregator {

    private String name;
    private Boolean isDefault;
    private Map<String, String> httpHeaders;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

}
