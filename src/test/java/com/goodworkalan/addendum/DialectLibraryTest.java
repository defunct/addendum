package com.goodworkalan.addendum;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.dialect.MockDialect;


/**
 * Unit tests for the {@link DialectLibrary} class.
 *
 * @author Alan Gutierrez
 */
public class DialectLibraryTest {
    /** Test successful find. */
    @Test
    public void find() throws SQLException {
        DialectLibrary library = DialectLibrary.getInstance();
        Connection connection = mock(Connection.class);
        DatabaseMetaData meta = mock(DatabaseMetaData.class);
        when(connection.getMetaData()).thenReturn(meta);
        when(meta.getDatabaseProductName()).thenReturn("MOCK");
        assertEquals(library.getDialect(connection).getClass(), MockDialect.class);
    }
    
    /** Test not found. */
    @Test
    public void notFound() throws SQLException {
        DialectLibrary library = DialectLibrary.getInstance();
        Connection connection = mock(Connection.class);
        DatabaseMetaData meta = mock(DatabaseMetaData.class);
        when(connection.getMetaData()).thenReturn(meta);
        when(meta.getDatabaseProductName()).thenReturn("BLURY");
        assertNull(library.getDialect(connection));
    }
}
