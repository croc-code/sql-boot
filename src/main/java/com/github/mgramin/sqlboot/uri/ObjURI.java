package com.github.mgramin.sqlboot.uri;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import java.net.URI;
import java.util.*;

/**
 * Created by maksim on 12.06.16.
 */
public class ObjURI {

    private String type;
    private String action;
    private List<String> objects;
    private Boolean recursive;
    private Map<String, String> params = new LinkedHashMap<>();
    private Map<String, String> filters = new HashMap<>();

    public ObjURI() {
    }

    public ObjURI(String type, List<String> objects) {
        this.type = type;
        this.objects = objects;
    }

    public ObjURI(String uriString) throws SqlBootException {
        try {
            URI uri = new URI(uriString);
            String pathString = uri.getPath();
            String queryString = uri.getQuery();

            List<String> list = Arrays.asList(pathString.split("[/]"));
            type = list.get(0);
            objects = Arrays.asList(list.get(1).split("[.]"));
            if (list.size() == 3) action = list.get(2);
            recursive = pathString.charAt(pathString.length() - 1) == '/';

            if (queryString != null)
                for (String s : queryString.split("&")) {
                    params.put(s.split("=")[0], s.split("=")[1]);
            }
        } catch (Exception e) {
            throw new SqlBootException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(type + "/" + String.join(".", objects));
        if (action != null && !action.equals("create"))
            result.append("/").append(action);
        if (recursive != null && recursive)
            result.append("/");
        if (!params.isEmpty()) {
            result.append("?");
            int i = 0;
            for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
                if (++i!=1)
                    result.append("&");
                result.append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue());
            }
        }
        return result.toString();
    }

    public String toJson() {
        return "ObjURI{" +
                "type='" + type + '\'' +
                ", DBSchemaObjectCommand='" + action + '\'' +
                ", objects=" + objects +
                ", recursive=" + recursive +
                ", params=" + params +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getObjects() {
        return objects;
    }

    public void setObjects(List<String> objects) {
        this.objects = objects;
    }

    public Boolean getRecursive() {
        return recursive;
    }

    public void setRecursive(Boolean recursive) {
        this.recursive = recursive;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }
}