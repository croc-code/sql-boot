package com.github.mgramin.sqlboot.tools.jdbc.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObject;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectImpl;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import lombok.ToString;
import static java.util.Arrays.asList;

/**
 * Created by MGramin on 13.07.2017.
 *
 * https://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html#getTables-java.lang.String-java.lang.String-java.lang.String-java.lang.String:A-
 *
 */
@ToString
public class Table extends AbstractJdbcObjectType implements JdbcDbObjectType {

    private final DataSource dataSource;

    public Table(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * "TABLE_CAT" - table catalog (may be null)
     */
    private String tableCat;

    /**
     * "TABLE_SCHEM" - table schema (may be null)
     */
    private String tableSchem;

    /**
     * "TABLE_NAME" - table name
     */
    private String tableName;

    /**
     * "TABLE_TYPE" - table type.
     * Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
     * "ALIAS", "SYNONYM".
     */
    private String tableType;

    /**
     * "REMARKS" - explanatory comment on the table
     */
    private String remarks;

    /**
     * "TYPE_CAT" - the types catalog (may be null)
     */
    private String typeCat;

    /**
     * "TYPE_SCHEM" - the types schema (may be null)
     */
    private String typeSchem;

    /**
     * "TYPE_NAME" - type name (may be null)
     */
    private String typeName;

    /**
     * "SELF_REFERENCING_COL_NAME" - name of the designated "identifier"
     * column of a typed table (may be null)
     */
    private String selfReferencingColName;

    /**
     * "REF_GENERATION" - specifies how values in SELF_REFERENCING_COL_NAME
     * are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
     */
    private String refGeneration;


    @Override
    public String name() {
        return "table";
    }

    @Override
    public List<JdbcDbObject> read(final List<String> params) throws SQLException {
        final List<JdbcDbObject> result = new ArrayList<>();

        final ResultSet tables = dataSource.getConnection().getMetaData()
                .getTables(null, params.get(0), "%", new String[]{"TABLE"});

        final ResultSetMetaData tableMetaData = tables.getMetaData();
        final int columnsCount = tableMetaData.getColumnCount();
        while (tables.next()) {
            tableCat = columnsCount >= 1 ? tables.getString(1) : null;
            tableSchem = columnsCount >= 2 ? tables.getString(2) : null;
            tableName = columnsCount >= 3 ? tables.getString(3) : null;
            tableType = columnsCount >= 4 ? tables.getString(4) : null;
            remarks = columnsCount >= 5 ? tables.getString(5) : null;
            typeCat = columnsCount >= 6 ? tables.getString(6) : null;
            typeSchem = columnsCount >= 7 ? tables.getString(7) : null;
            typeName = columnsCount >= 8 ? tables.getString(8) : null;
            selfReferencingColName = columnsCount >= 9 ? tables.getString(9) : null;
            refGeneration = columnsCount >= 10 ? tables.getString(10) : null;


            final Map<String, Object> map = new HashMap<>();
            for (int i = 1; i <= columnsCount; i++) {
                map.put(tableMetaData.getColumnName(i), tables.getString(i));
            }
            result.add(new JdbcDbObjectImpl(asList(tableSchem, tableName), map));
        }
        return result;
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
