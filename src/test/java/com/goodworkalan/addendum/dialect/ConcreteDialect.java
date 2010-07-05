package com.goodworkalan.addendum.dialect;

import java.sql.Connection;
import java.sql.Types;

import org.slf4j.LoggerFactory;

import com.goodworkalan.notice.NoticeFactory;

/**
 * A concrete extension of {@link AbstractDialect} for unit testing of
 * <code>AbstractDialect</code> specifically.
 * 
 * @author Alan Gutierrez
 */
public class ConcreteDialect extends AbstractDialect {
    /** The notice factory specifically for the <code>ConcreteDialect</code>. */
    private final static NoticeFactory NOTICES = new NoticeFactory(LoggerFactory.getLogger(ConcreteDialect.class));

    /**
     * Create a concrete dialect.
     */
    public ConcreteDialect() {
        super();
        setType(Types.INTEGER, "INTEGER");
        setType(Types.NUMERIC, "NUMERIC(%2$d, %3$d)");
        setType(Types.VARCHAR, "VARCHAR(%1$d)", 65535);
        setType(Types.VARCHAR, "BLURDY(%1$d)", 70000);
        setType(Types.CHAR, "CHAR(%1$d)", 255);
        setType(Types.VARCHAR, "TEXT");
        setDefaultLength(Types.VARCHAR, 225);
        setDefaultPrecisionScale(Types.NUMERIC, 10, 2);
    }

    /**
     * Does nothing and always returns zero.
     * 
     * @param connection
     *            The JDBC connection.
     * @return Zero.
     */
    public int addendaCount(Connection connection) {
        return 0;
    }

    /**
     * Calls the protected <code>columnDefintion</code> method of
     * <code>AbstractDialect</code> for testing.
     * 
     * @param sql
     *            The sql buffer.
     * @param column
     *            The column.
     * @param nullable
     *            Whether the column can be null.
     */
    public void columnDefinition(StringBuilder sql, Column column, boolean nullable) {
        super.columnDefinition(sql, column, nullable);
    }

    /**
     * Does nothing.
     * 
     * @param connection
     *            The JDBC connection.
     */
    public void addendum(Connection connection) {
    }

    /**
     * Return the given dialect if it can translate addenda for the given JDBC
     * connection.
     * 
     * @param connection
     *            The JDBC connection.
     * @param dialect
     *            The dialect.
     */
    public Dialect canTranslate(Connection connection, Dialect dialect) {
        return dialect;
    }

    /**
     * Does nothing.
     * 
     * @param connection
     *            The JDBC connection.
     */
    public void createAddendaTable(Connection connection) {
    }

    /**
     * Get the notice factory specifically for the <code>ConcreteDialect</code>.
     * 
     * @return The notice factory.
     */
    @Override
    protected NoticeFactory getNoticeFactory() {
        return NOTICES;
    }

    /**
     * Does nothing.
     * 
     * @param connection
     *            The JDBC connection.
     * @param tableName
     *            The table name.
     * @param oldName
     *            The exiting column name.
     * @param column
     *            The column definition.
     */
    public void alterColumn(Connection connection, String tableName, String oldName, Column column) {
    }

    /**
     * Does nothing.
     * 
     * @param connection
     *            The JDBC connection.
     * @param oldName
     *            The old table name.
     * @param newName
     *            The new table name.
     */
    public void renameTable(Connection connection, String oldName, String newName) {
    }
}
