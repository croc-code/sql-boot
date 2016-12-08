package com.github.mgramin.sqlboot.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 10.07.16.
 */
public class ObjectService {

    Map<String, DBSchemaObject> objects;
    private String baseURI;

    public ObjectService(Map<String, DBSchemaObject> objects, String baseURI) {
        this.objects = objects;
        this.baseURI = baseURI;
    }

    public List<DBSchemaObject> get(String type) {
        List<DBSchemaObject> result = new ArrayList<>();
        for (Map.Entry<String, DBSchemaObject> entry : objects.entrySet()) {
            if (entry.getKey().startsWith(type + "/" + baseURI)) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    public Integer getMaxLength(String type, String name) {
        DBSchemaObject column = get(type).stream()/*.filter(o -> o.paths.get("table").equalsIgnoreCase(name))*/.max(Comparator.comparing(i -> i.paths.get(type))).get();
        return column.paths.get("column").length() + 7;
    }

}
