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

package com.github.mgramin.sqlboot.template.generator.impl;

import com.github.mgramin.sqlboot.template.TemplateGenerator;
import groovy.text.Template;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MGramin on 26.02.2017.
 */
public abstract class AbstractGroovyTemplateGenerator implements TemplateGenerator {

    protected Template template;
    protected String templateText;

    @Override
    public String generate(Map<String, Object> variables) {
        try {
            return this.template.make(variables).toString();
        } catch (Exception e) {
            System.out.println(templateText);
            System.out.println(variables);
            throw e;
        }
    }

    @Override
    public List<String> properties() {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$\\s*(\\w+)");
        Matcher matcher = pattern.matcher(templateText);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

}
