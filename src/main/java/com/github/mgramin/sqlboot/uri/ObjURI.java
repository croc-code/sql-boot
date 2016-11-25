package com.github.mgramin.sqlboot.uri;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 12.06.16.
 */
public class ObjURI {

    private String type;
    private String action;
    private List<String> objects;
    private Boolean recursive;
    private Map<String, String> params = new HashMap<>();

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
        if (list.size()==3)
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
        return type + "/" + String.join(".", objects);
        /*return "ObjURI{" +
                "type='" + type + '\'' +
                ", action='" + action + '\'' +
                ", objects=" + objects +
                ", recursive=" + recursive +
                ", params=" + params +
                '}';*/
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