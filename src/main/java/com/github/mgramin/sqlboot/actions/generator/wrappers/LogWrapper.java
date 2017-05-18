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

package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DbResourceCommand;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 29.04.17.
 */
public final class LogWrapper implements ActionGenerator {

    final private static Logger logger = Logger.getLogger(LogWrapper.class);
    final private ActionGenerator baseGenerator;

    public LogWrapper(ActionGenerator baseGenerator) {
        this.baseGenerator = baseGenerator;
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        StopWatch stopWatch = startLog(variables);
        String generate = baseGenerator.generate(variables);
        stopLog(stopWatch);
        return generate;
    }

    @Override
    public String generate(List<Object> variables) throws SqlBootException {
        StopWatch stopWatch = startLog(variables);
        String generate = baseGenerator.generate(variables);
        stopLog(stopWatch);
        return generate;
    }

    @Override
    public DbResourceCommand command() {
        return baseGenerator.command();
    }

    private StopWatch startLog(Object variables) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        logger.info("Start with variables: " + variables);
        return stopWatch;
    }

    private void stopLog(StopWatch stopWatch) {
        stopWatch.stop();
        logger.info("Time elapsed " + stopWatch.getTime()  + "ms");
    }

}
