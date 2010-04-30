package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.SQL_GET_DIALECT;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.api.MockConnector;

/**
 * Unit tests for the {@link ApplyAddendum} class.
 *
 * @author Alan Gutierrez
 */
public class ScriptTest {
    /** Test dialect sql error. */
    @Test(expectedExceptions = AddendumException.class)
    public void dialectSqlError() {
        try {
            new ApplyAddendum(new MockConnector(), new DialectProvider() {
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
