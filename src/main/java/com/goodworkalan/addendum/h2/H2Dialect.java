package com.goodworkalan.addendum.h2;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.goodworkalan.addendum.dialect.AbstractDialect;
import com.goodworkalan.addendum.dialect.Column;
import com.goodworkalan.notice.event.Entry;
import com.goodworkalan.notice.event.Logger;
import com.goodworkalan.notice.event.LoggerFactory;

public class H2Dialect extends AbstractDialect {
    private final static Logger logger = LoggerFactory.getLogger(H2Dialect.class);

    /**
     * Create a new MySQL dialect.
     */
    public H2Dialect() {
        super();
        setType(Types.BIT, "BIT");
        setType(Types.BOOLEAN, "BIT");
        setType(Types.TINYINT, "TINYINT");
        setType(Types.SMALLINT, "SMALLINT");
        setType(Types.INTEGER, "INTEGER");
        setType(Types.BIGINT, "BIGINT");
        setType(Types.FLOAT, "FLOAT");
        setType(Types.DOUBLE, "DOUBLE");
        setType(Types.VARCHAR, "VARCHAR(%1$d)", 65535);
        setType(Types.VARCHAR, "TEXT");
        setType(Types.TIMESTAMP, "TIMESTAMP");
        setType(Types.NUMERIC, "NUMERIC(%2$d, %3$d)");
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    /**
     * Create the table used to store the applied addenda.
     * 
     * @param connection
     *            An SQL connection on the database.
     * 
     * @throws SQLException
     *             For any SQL error.
     */
    public void createAddendaTable(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet results = metaData.getTables(null, null, "ADDENDA", null);
        boolean build = !results.next();
        results.close();
        if (build) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE Addenda (addendum INTEGER)");
            statement.close();
        }
    }
    
    public int addendaCount(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT COALESCE(MAX(addendum), 0) FROM Addenda");
        results.next();
        int max = results.getInt(1);
        results.close();
        return max;
    }
    
    public void addendum(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO Addenda SELECT COALESCE(MAX(addendum), 0) + 1 FROM Addenda");
        statement.close();
    }
    
    /**
     * Return true if the database is a MySQL database.
     * 
     * @return True if the database is a MySQL database.
     */
    public boolean canTranslate(Connection connection) throws SQLException {
        return connection.getMetaData().getDatabaseProductName().equals("H2");
    }

    public void alterColumn(Connection connection, String tableName, String oldName, Column column) throws SQLException {
        Entry debug = logger.debug("alter.column");
        try {
            debug.put("tableName", tableName).put("oldName", oldName).put("column", column);

            if (!oldName.equals(column.getName())) {
                String sql = "ALTER TABLE " + tableName + " ALTER COLUMN " + oldName + " RENAME TO " + column.getName();
                
                debug.put("rename", sql);
                
                Statement statement = connection.createStatement();
                statement.execute(sql);
                statement.close();
            }
            
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE ").append(tableName).append(" ALTER ");
            columnDefinition(sql, column, true);
            
            debug.put("alter", sql);
            
            Statement statement = connection.createStatement();
            statement.execute(sql.toString());
            statement.close();
        } finally {
            debug.send();
        }
        
    }
    
    /**
     * Rename a table form the given old name to the given new name.
     * 
     * @param connection
     *            The JDBC connection.
     * @param oldName
     *            The old table name.
     * @param newName
     *            The new table name.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void renameTable(Connection connection, String oldName, String newName) throws SQLException {
        Entry info = getLogger().info("rename");

        info.put("oldName", oldName).put("newName", newName);

        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(oldName).append(" RENAME TO ").append(newName);
        
        info.put("sql", sql);
        
        info.send();

        Statement statement = connection.createStatement();
        statement.execute(sql.toString());
        statement.close();
    }
}