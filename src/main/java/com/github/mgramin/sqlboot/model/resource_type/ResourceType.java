/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.mgramin.sqlboot.model.resource_type;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.uri.Uri;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Resource type e.g. Table, Index, Stored function etc
 */
public interface ResourceType {

    /**
     * Name of resource type, e.g "table", "index", "stored procedure" etc
     */
    @JsonProperty
    String name();

    /**
     * Aliases of resource type, e.g. ["table", "tbl", "t", "tablo"]
     */
    @JsonProperty
    List<String> aliases();

    /**
     * Path of resource e.g. ["schema", "table", "column"] or ["schema", "table", "index",
     * "index_column"]
     */
    @JsonProperty
    List<String> path();

    /**
     * Retrieves a map that contains information about the resource metadata (properties) "name" ->
     * "type"
     */
    @JsonProperty
    Map<String, String> metaData();

    default List<Metadata> metaData(Uri uri) {
        return metaData().entrySet().stream()
            .map(e -> new ResourceType.Metadata(e.getKey(), e.getValue()))
            .collect(toList());
    }

    /**
     * Read resources by uri
     */
    Stream<DbResource> read(Uri uri) throws BootException;


    @JsonAutoDetect(fieldVisibility = Visibility.ANY)
    class Metadata {

        private final String name;
        private final String type;
        private final String description;
        private /*final*/ Map<String, Object> properties;

        public Metadata(String name, String description) {
            this(name, "String", description, null);
        }

        public Metadata(String name, String type, String description, Map<String, Object> properties) {
            try {
                this.properties = new JSONObject(description).toMap();
                this.properties.put("key", name.replace("@", ""));
            } catch (JSONException ignored) {
                final HashMap<String, Object> prop = new HashMap<>();
                prop.put("key", name.replace("@", ""));
                prop.put("label", name.replace("@", ""));
                prop.put("description", description);
                this.properties = prop;
            }
            this.name = name;
            this.type = type;
            this.description = description;
        }

        public String name() {
            return name;
        }

        public String description() {
            return description;
        }

        public Map<String, Object> properties() {
            return properties;
        }

    }

}
