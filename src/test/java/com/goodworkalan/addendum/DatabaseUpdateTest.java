package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.CANNOT_ADD_COLUMN;
import static com.goodworkalan.addendum.AddendumException.CANNOT_ALTER_COLUMN;
import static com.goodworkalan.addendum.AddendumException.CANNOT_CREATE_TABLE;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link DatabaseUpdate} class.
 * 
 * @author AlanGutierrez
 */
public class DatabaseUpdateTest {
    /** Test the cannot create table error message. */
    @Test(expectedExceptions = AddendumException.class)
    public void cannotCreateTableMessage() {
        try {
            new DatabaseUpdate(CANNOT_CREATE_TABLE, "a", "a") {
                public void execute(Connection connection, Dialect dialect)
                        throws SQLException {
                    throw new SQLException();
                }
            }.update(null, null);
        } catch (AddendumException e) {
            assertEquals(e.getCode(), CANNOT_CREATE_TABLE);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test the cannot alter column error message. */
    @Test(expectedExceptions = AddendumException.class)
    public void cannotAlterColumnMessage() {
        try {
            new DatabaseUpdate(CANNOT_ALTER_COLUMN, "a", "a") {
                public void execute(Connection connection, Dialect dialect)
                        throws SQLException {
                    throw new SQLException();
                }
            }.update(null, null);
        } catch (AddendumException e) {
            assertEquals(e.getCode(), CANNOT_ALTER_COLUMN);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test the cannot add column error message. */
    @Test(expectedExceptions = AddendumException.class)
    public void cannotAddColumnMessage() {
        try {
            new DatabaseUpdate(CANNOT_ADD_COLUMN, "a", "a") {
                public void execute(Connection connection, Dialect dialect)
                        throws SQLException {
                    throw new SQLException();
                }
            }.update(null, null);
        } catch (AddendumException e) {
            assertEquals(e.getCode(), CANNOT_ADD_COLUMN);
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
