package com.github.mgramin.sqlboot.tools.jdbc.impl;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObject;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectImpl;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.ToString;

/**
 * Created by MGramin on 13.07.2017.
 */
@ToString
public class Table extends AbstractJdbcObjectType implements JdbcDbObjectType {

    private static final String COLUMN_NAME_PROPERTY = "TABLE_NAME";
    private final DataSource dataSource;

    public Table(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * table catalog (may be null)
     */
    String tableCat;

    /**
     * table schema (may be null)
     */
    String tableSchem;

    /**
     * table name
     */
    String tableName;

    /**
     * table type.
     * Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY" "ALIAS", "SYNONYM".
     */
    String tableType;

    /**
     * explanatory comment on the table
     */
    String remarks;

    /**
     * the types catalog (may be null)
     */
    String typeCat;

    /**
     * the types schema (may be null)
     */
    String typeSchem;

    /**
     * type name (may be null)
     */
    String typeName;

    /**
     * name of the designated "identifier" column of a typed table (may be null)
     */
    String selfReferencingColName;

    /**
     * specifies how values in SELF_REFERENCING_COL_NAME are created.
     * Values are "SYSTEM", "USER", "DERIVED". (may be null)
     */
    String refGeneration;


    @Override
    public String name() {
        return "table";
    }

    @Override
    public List<JdbcDbObject> read(List<String> params) throws SQLException {
        ResultSet tables = dataSource.getConnection().getMetaData().
                getTables(null, "%", "%", new String[]{"TABLE"});
        return toMap(tables).stream()
                .map(v -> new JdbcDbObjectImpl(
                        asList(v.get("TABLE_SCHEMA"), v.get(COLUMN_NAME_PROPERTY)),
                        v))
                .collect(toList());
    }

    @Override
    public Map<String, String> medataData() {
        final Map<String, String> result = new LinkedHashMap<>();
        result.put("table_cat", "String");
        result.put("table_schem", "String");
        result.put("table_name", "String");
        result.put("table_type", "String");
        result.put("remarks", "String");
        result.put("type_cat", "String");
        result.put("type_schem", "String");
        result.put("type_name", "String");
        result.put("self_referencing_col_name", "String");
        result.put("ref_generation", "String");
        return result;
    }

}
