package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import lombok.ToString;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

/**
 * Created by maksim on 05.04.16.
 */
@ToString
public class SQLWrapper implements IActionGenerator {

    public SQLWrapper(IActionGenerator baseGenerator, ISqlHelper sqlHelper) {
        this.baseGenerator = baseGenerator;
        this.sqlHelper = sqlHelper;
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        List<Map<String, String>> maps = sqlHelper.selectBatch(singletonList(baseGenerator.generate(variables)));
        return maps.get(0).entrySet().iterator().next().getValue();
    }

    @Override
    public DBSchemaObjectCommand command() {
        return baseGenerator.command();
    }

    final private IActionGenerator baseGenerator;
    final private ISqlHelper sqlHelper;

}