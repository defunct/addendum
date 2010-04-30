package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.DIALECT_DOES_NOT_SUPPORT_CONNECTION;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.dialect.MockDialect;

/**
 * Unit tests for the {@link DialectInstance} class.
 *
 * @author Alan Gutierrez
 */
public class DialectInstanceTest {
    /** Test an unsupported connection. */
    @Test(expectedExceptions = AddendumException.class)
    public void notAppicable() throws SQLException {
        DatabaseMetaData meta = mock(DatabaseMetaData.class);
        Connection connection = mock(Connection.class);
        when(connection.getMetaData()).thenReturn(meta);
        when(meta.getDatabaseProductName()).thenReturn("BLURDY");
        try {
            new DialectInstance(new MockDialect()).getDialect(connection);
        } catch (AddendumException e) {
            assertEquals(e.getCode(), DIALECT_DOES_NOT_SUPPORT_CONNECTION);
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
