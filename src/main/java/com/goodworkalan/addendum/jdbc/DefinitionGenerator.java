package com.goodworkalan.addendum.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Generate an Addendum definition from an existing JDBC data source.
 *
 * @author Alan Gutierrez
 */
public class DefinitionGenerator {
    /**
     * Generate the Java source code an Addendum definition of a database. The
     * source code define a class with the <code></code> that implements
     * <code>Definition</code> with the.
     * 
     * @param className
     *            The class name.
     * @param connection
     *            The connection.
     * @throws SQLException
     *             For any SQL error.
     */
    public static void generate(String className, Connection connection) throws SQLException {
        List<Table> tables = new ArrayList<Table>();
        DefinitionDocument definitionDocument = new DefinitionDocument(className, tables);
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet results = meta.getTables(null, null, null, null);
        while (results.next()) {
            String catalog = results.getString("TABLE_CAT");
            String schema = results.getString("TABLE_SCHEM");
            String name = results.getString("TABLE_NAME");
            String type = results.getString("TABLE_TYPE");
            if (type.equals("TABLE") && (schema == null || schema.equals("PUBLIC")) && !name.toUpperCase().equals("ADDENDA")) {
                ResultSetMetaData resultMeta = connection.createStatement(
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM " + name + " WHERE 1 = 0").getMetaData();
                Table table = new Table();
                tables.add(table);
                table.name = results.getString("TABLE_NAME");
                ResultSet columns = meta.getColumns(catalog, schema, table.name, null);
                TreeMap<Integer, Column> ordered = new TreeMap<Integer, Column>();
                while (columns.next()) {
                    Column column = new Column();
                    column.name = columns.getString("COLUMN_NAME");
                    column.nullable = columns.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls;
                    column.type = columns.getInt("DATA_TYPE");
                    column.size = columns.getInt("COLUMN_SIZE");
                    column.scale = columns.getInt("DECIMAL_DIGITS");
                    int order = columns.getInt("ORDINAL_POSITION");
                    column.autoIncrement = resultMeta.isAutoIncrement(order);
                    if (column.autoIncrement) {
                        definitionDocument.needsGeneratorType = true;
                    }
                    ordered.put(columns.getInt("ORDINAL_POSITION"), column);
                }
                table.columns.addAll(ordered.values());
                ResultSet primaryKey = meta.getPrimaryKeys(catalog, schema, name);
                Map<Integer, String> orderedPrimaryKey = new TreeMap<Integer, String>();
                while (primaryKey.next()) {
                    orderedPrimaryKey.put(primaryKey.getInt("KEY_SEQ"), primaryKey.getString("COLUMN_NAME"));
                }
                table.primaryKey.addAll(orderedPrimaryKey.values());
            }
        }
        definitionDocument.print();
    }
}
