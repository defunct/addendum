package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.Addendum.CANNOT_EXECUTE_SQL;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.dialect.Dialect;
import com.goodworkalan.danger.Danger;

/**
 * Unit tests for the {@link Execution} class.
 *
 * @author Alan Gutierrez
 */
public class ExecutionTest {
    /** Test SQL exception wrapping. */
    @Test(expectedExceptions = Danger.class)
    public void sqlException() {
        Execution execution = new Execution(new Executable() {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
                throw new SQLException();
            }
        });
        try {
            execution.execute(new Schema()).update(null, null);
        } catch (Danger e) {
            assertEquals(e.code, CANNOT_EXECUTE_SQL);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test SQL execution. */
    @Test
    public void update() {
        Execution execution = new Execution(new Executable() {
            public void execute(Connection connection, Dialect dialect)
            throws SQLException {
            }
        });
        execution.execute(new Schema()).update(null, null);
    }
}
