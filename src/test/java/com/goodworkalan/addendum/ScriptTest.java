package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.SQL_GET_DIALECT;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.testng.annotations.Test;


/**
 * Unit tests for the {@link Script} class.
 *
 * @author Alan Gutierrez
 */
public class ScriptTest {
    /** Test dialect sql error. */
    @Test(expectedExceptions = AddendumException.class)
    public void dialectSqlError() {
        try {
            new Script(new MockConnector(), new DialectProvider() {
                public Dialect getDialect(Connection connection)
                throws SQLException {
                    throw new SQLException();
                }
            }, new ArrayList<DatabaseUpdate>()).execute();
        } catch (AddendumException e) {
            assertEquals(e.getCode(), SQL_GET_DIALECT);
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
