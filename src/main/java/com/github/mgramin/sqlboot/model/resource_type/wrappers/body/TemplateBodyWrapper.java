/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 Maksim Gramin
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.model.resource_type.wrappers.body;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource.wrappers.DbResourceBodyWrapper;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.template.generator.TemplateGenerator;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by MGramin on 18.07.2017.
 */
public class TemplateBodyWrapper implements ResourceType {

    private final ResourceType origin;
    private final TemplateGenerator templateGenerator;

    public TemplateBodyWrapper(final ResourceType origin, final TemplateGenerator templateGenerator) {
        this.origin = origin;
        this.templateGenerator = templateGenerator;
    }

    @Override
    public String name() {
        return origin.name();
    }

    @Override
    public List<String> aliases() {
        return origin.aliases();
    }

    @Override
    public List<DbResource> read(final Uri uri) throws BootException {
        List<DbResource> resources = origin.read(uri);
        return resources.stream()
                .map(v -> new DbResourceBodyWrapper(v,
                        templateGenerator.generate(
                                v.headers().entrySet().stream()
                                        .collect(toMap(Entry::getKey, e -> Optional.ofNullable(e.getValue()).map(o -> (Object) o).orElse("")))
                        )))
                .collect(toList());
    }

}
