/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.actions.generator.prepared;

import java.util.List;
import java.util.Map;
import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.impl.PlainTextGenerator;
import com.github.mgramin.sqlboot.actions.generator.wrappers.SqlWrapper;
import com.github.mgramin.sqlboot.actions.generator.wrappers.TemplateWrapper;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.IDbResourceCommand;
import com.github.mgramin.sqlboot.tools.sql.ISqlHelper;
import com.github.mgramin.sqlboot.tools.template_engine.TemplateEngineFactory;

/**
 *
 *
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class PlainTextTemplateSqlGenerator implements ActionGenerator {

    /**
     * Original generator.
     */
    private final ActionGenerator origin;

    /**
     * Ctor.
     *
     * @param baseText  base text
     * @param command   command type
     * @param templateEngineFactory template engine factory
     * @param sqlHelper sql helper
     * @param aggregators   aggregator labels
     */
    public PlainTextTemplateSqlGenerator(
            final String baseText, final IDbResourceCommand command,
            final TemplateEngineFactory templateEngineFactory,
            final ISqlHelper sqlHelper, final String aggregators) {
        origin = new SqlWrapper(
                    new TemplateWrapper(
                        new PlainTextGenerator(baseText, command, aggregators),
                        templateEngineFactory),
                    sqlHelper);
    }

    @Override
    public String generate(final Map<String, Object> variables)
            throws BootException {
        return origin.generate(variables);
    }

    @Override
    public String generate(final List<Object> variables)
            throws BootException {
        return origin.generate(variables);
    }

    @Override
    public IDbResourceCommand command() {
        return origin.command();
    }

    @Override
    public String aggregators() {
        return origin.aggregators();
    }

}
