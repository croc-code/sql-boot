package com.github.mgramin.sqlboot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MGramin on 28.11.2016.
 */
public class DBSchemaObjectTypeContainer {

    public List<DBSchemaObjectType> types = new ArrayList<>();

    public void setTypes(List<DBSchemaObjectType> types) {
        this.types = types;
    }

}
