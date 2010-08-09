package com.goodworkalan.addendum.h2;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.slf4j.LoggerFactory;

import com.goodworkalan.addendum.dialect.AbstractDialect;
import com.goodworkalan.addendum.dialect.Column;
import com.goodworkalan.addendum.dialect.Dialect;
import com.goodworkalan.notice.Notice;
import com.goodworkalan.notice.NoticeFactory;

/**
 * Perform addendum defined updates against an H2 database.
 * 
 * @author Alan Gutierrez
 */
public class H2Dialect extends AbstractDialect {
    /** The notice factory. */
    private final static NoticeFactory NOTICES = new NoticeFactory(LoggerFactory.getLogger(H2Dialect.class));

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

    /**
     * Get the notice factory in the context of the H2 dialect.
     * 
     * @return The notice factory.
     */
    @Override
    protected NoticeFactory getNoticeFactory() {
        return NOTICES;
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
    
    /**
     * Get the maximum update applied.
     * 
     * @param connection
     *            An SQL connection on the database.
     * 
     * @return The maximum update applied.
     * @throws SQLException
     *             For any SQL error.
     */
    public int addendaCount(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT COALESCE(MAX(addendum), 0) FROM Addenda");
        results.next();
        int max = results.getInt(1);
        results.close();
        return max;
    }
    
    /**
     * Increment the update number in the database.
     * 
     * @param connection
     *            An SQL connection on the database.
     * 
     * @throws SQLException
     *             For any SQL error.
     */
    public void addendum(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO Addenda SELECT COALESCE(MAX(addendum), 0) + 1 FROM Addenda");
        statement.close();
    }

    /**
     * Return this dialect if the given connection is an H2 connection and the
     * given candidate dialect is null.
     * 
     * @param conneciton
     *            The JDBC connection.
     * @param dialect
     *            The current dialect candidate.
     * @return This dialect if the connection is an H2 connection and candidate
     *         dialect is null.
     */
    public Dialect canTranslate(Connection connection, Dialect dialect) throws SQLException {
        if (connection.getMetaData().getDatabaseProductName().equals("H2") && dialect == null) {
            return this;
        }
        return dialect;
    }

    /**
     * Alter the column in the given table with the given exiting column name
     * according to the given column definition. This method can both rename and
     * redefine columns.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @param oldName
     *            The exiting column name.
     * @param column
     *            The column definition.
     * @throws SQLException
     *             For any reason, any reason at all.
     */
    public void alterColumn(Connection connection, String tableName, String oldName, Column column) throws SQLException {
        Notice debug = NOTICES.debug("alter.column");
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
        Notice info = getNoticeFactory().info("rename");

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