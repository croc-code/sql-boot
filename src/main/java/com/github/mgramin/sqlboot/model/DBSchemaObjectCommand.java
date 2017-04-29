/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 mgramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.github.mgramin.sqlboot.model;

import lombok.ToString;

import java.util.Arrays;
import java.util.List;

/**
 * Command for db-object, e.g. "create", "drop", "exists", "rebuild", "gather"(statistics),
 * "compile"(procedure, function, package), etc
 */
@ToString
public class DBSchemaObjectCommand {

    public String name;
    public List<String> aliases;
    public Boolean isDefault;


    public void setName(String name) {
        this.name = name;
    }

    public void setAliases(String[] aliases) {
        this.aliases = Arrays.asList(aliases);
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

}