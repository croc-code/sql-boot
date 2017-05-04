package com.github.mgramin.sqlboot.actions.generator.wrappers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static com.google.common.collect.ImmutableMap.of;
/**
 * Created by MGramin on 04.05.2017.
 */
public class LogWrapperTest {


    @Mock AppenderSkeleton appender;
    @Captor
    ArgumentCaptor<LoggingEvent> logCaptor;

    @Test
    public void generate() throws Exception {
        ActionGenerator baseActionGenerator = mock(ActionGenerator.class);
        when(baseActionGenerator.generate(any())).thenReturn("create table ... ;");

        ActionGenerator logWrapper = new LogWrapper(baseActionGenerator);
        logWrapper.generate(of("var1", "val1", "var2", "val2"));

/*        verify(appender).doAppend(logCaptor.capture());
        assertEquals("Start with variables: {var1=val1, var2=val2}", "Time elapsed 21ms",
            logCaptor.getValue().getRenderedMessage());*/

    }

}