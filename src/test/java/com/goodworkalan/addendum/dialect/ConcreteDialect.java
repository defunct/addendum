package com.goodworkalan.addendum.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.LoggerFactory;

import com.goodworkalan.notice.NoticeFactory;

public class ConcreteDialect extends AbstractDialect {
    private final static NoticeFactory logger = new NoticeFactory(LoggerFactory.getLogger(ConcreteDialect.class));

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

    public int addendaCount(Connection connection) throws SQLException {
        return 0;
    }

    public void columnDefinition(StringBuilder sql, Column column, boolean canNull) {
        super.columnDefinition(sql, column, canNull);
    }

    public void addendum(Connection connection) throws SQLException {
    }

    public boolean canTranslate(Connection connection) throws SQLException {
        return false;
    }

    public void createAddendaTable(Connection connection) throws SQLException {
    }

    @Override
    protected NoticeFactory getNoticeFactory() {
        return logger;
    }

    public void alterColumn(Connection connection, String tableName, String oldName, Column column)
    throws SQLException {
    }

    public void renameTable(Connection connection, String oldName, String newName)
    throws SQLException {
    }
}
