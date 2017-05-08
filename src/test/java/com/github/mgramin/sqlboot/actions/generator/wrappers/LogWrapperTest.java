package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Created by MGramin on 04.05.2017.
 */
public class LogWrapperTest {

    @Test
    // TODO add asserts for log messages
    public void generate() throws Exception {
        ActionGenerator baseActionGenerator = mock(ActionGenerator.class);
        when(baseActionGenerator.generate(any(Map.class))).thenReturn("create table ... ;");

        ActionGenerator logWrapper = new LogWrapper(baseActionGenerator);
        logWrapper.generate(of("var1", "val1", "var2", "val2"));
    }

}