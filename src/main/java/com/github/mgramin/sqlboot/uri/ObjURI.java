package com.github.mgramin.sqlboot.uri;

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

    public ObjURI() {
    }

    public ObjURI(String type, List<String> objects) {
        this.type = type;
        this.objects = objects;
    }

    public ObjURI(String uri) {
        String pathString;
        String queryString = null;

        pathString = uri.split("[?]")[0];
        if (uri.split("[?]").length == 2) {
            queryString = uri.split("[?]")[1];
        }

        List<String> list = Arrays.asList(pathString.split("[/]"));
        type = list.get(0);
        objects = Arrays.asList(list.get(1).split("[.]"));
        if (list.size() == 3)
            action = list.get(2);
        else
            action = "create";
        recursive = pathString.charAt(pathString.length() - 1) == '/';

        if (queryString != null) {
            for (String s : queryString.split("&")) {
                params.put(s.split("=")[0], s.split("=")[1]);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(type + "/" + String.join(".", objects));
        if (!action.equals("create"))
            result.append("/").append(action);
        if (recursive && !action.equals("create"))
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
                ", action='" + action + '\'' +
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

}