package com.goodworkalan.addendum.mysql;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.dialect.Column;

/**
 * Unit tests for for the {@link MySQLDialect} class.
 *
 * @author Alan Gutierrez
 */
public class MySQLDialectTest {
    /** Test add column. */
    @Test
    public void alterColumn() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        new MySQLDialect().alterColumn(connection, "a", "b", new Column("a", int.class));
        verify(statement).execute("ALTER TABLE a CHANGE b a INTEGER");
    }
    
    /** Test crate addenda table. */
    @Test
    public void createAddendaTable() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        DatabaseMetaData meta = mock(DatabaseMetaData.class);
        when(connection.getMetaData()).thenReturn(meta);
        ResultSet results = mock(ResultSet.class);
        when(meta.getTables(null, null, "Addenda", null)).thenReturn(results);
        new MySQLDialect().createAddendaTable(connection);
        when(results.next()).thenReturn(true);
        new MySQLDialect().createAddendaTable(connection);
        verify(statement).execute("CREATE TABLE Addenda (addendum INTEGER)");
    }
    
    /** Test crate addenda table. */
    @Test
    public void addendaCount() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        ResultSet results = mock(ResultSet.class);
        when(results.getInt(1)).thenReturn(1);
        when(statement.executeQuery("SELECT COALESCE(MAX(addendum), 0) FROM Addenda")).thenReturn(results);
        assertEquals(new MySQLDialect().addendaCount(connection), 1);
    }
    
    /** Test addendum. */
    @Test
    public void addendum() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        new MySQLDialect().addendum(connection);
        verify(statement).execute("INSERT INTO Addenda SELECT COALESCE(MAX(addendum), 0) + 1 FROM Addenda");
    }
    
    /** Test can translate. */
    @Test
    public void canTranslate() throws SQLException {
        DatabaseMetaData meta = mock(DatabaseMetaData.class);
        Connection connection = mock(Connection.class);
        when(connection.getMetaData()).thenReturn(meta);
        when(meta.getDatabaseProductName()).thenReturn("MySQL");
        assertTrue(new MySQLDialect().canTranslate(connection));
    }
    
    /** Test rename table. */
    @Test
    public void renameTable() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        new MySQLDialect().renameTable(connection, "a", "b");
        verify(statement).execute("RENAME TABLE a TO b");
    }
}
