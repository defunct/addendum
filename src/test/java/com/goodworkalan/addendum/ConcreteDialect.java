package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import com.goodworkalan.notice.event.Logger;
import com.goodworkalan.notice.event.LoggerFactory;

public class ConcreteDialect extends AbstractDialect {
    private final static Logger logger = LoggerFactory.getLogger(ConcreteDialect.class);

    public ConcreteDialect() {
        super();
        setType(Types.INTEGER, "INTEGER");
        setType(Types.DECIMAL, "DECIMAL");
        setType(Types.VARCHAR, "VARCHAR(%1$d)", 65535);
        setType(Types.VARCHAR, "BLURDY(%1$d)", 70000);
        setType(Types.VARCHAR, "TEXT");
        setDefaultPrecisionScale(Types.DECIMAL, 10, 2);
    }

    @Override
    public int addendaCount(Connection connection) throws SQLException {
        return 0;
    }

    public void columnDefinition(StringBuilder sql, Column column, boolean canNull) {
        super.columnDefinition(sql, column, canNull);
    }

    @Override
    public void addendum(Connection connection) throws SQLException {
    }

    @Override
    public boolean canTranslate(Connection connection) throws SQLException {
        return false;
    }

    @Override
    public void createAddendaTable(Connection connection) throws SQLException {
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    public void alterColumn(Connection connection, String tableName, String oldName, Column column)
    throws SQLException {
    }

    @Override
    public void renameTable(Connection connection, String oldName,
            String newName) throws SQLException {
        // TODO Auto-generated method stub
        
    }
}
