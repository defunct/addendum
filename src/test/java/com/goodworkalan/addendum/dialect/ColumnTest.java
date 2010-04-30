package com.goodworkalan.addendum.dialect;

import static com.goodworkalan.addendum.AddendumException.UNMAPPABLE_TYPE;
import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Calendar;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.AddendumException;
import com.goodworkalan.addendum.dialect.Column;

import sun.nio.ByteBuffered;

/**
 * Unit tests for the {@link Column} class.
 *
 * @author Alan Gutierrez
 */
public class ColumnTest {
    /** Test Java type to SQL type conversion. */
    @Test
    public void getColumnType() {
        assertEquals(Column.getColumnType(boolean.class), Types.BIT);
        assertEquals(Column.getColumnType(short.class), Types.TINYINT);
        assertEquals(Column.getColumnType(char.class), Types.SMALLINT);
        assertEquals(Column.getColumnType(int.class), Types.INTEGER);
        assertEquals(Column.getColumnType(long.class), Types.BIGINT);
        assertEquals(Column.getColumnType(float.class), Types.FLOAT);
        assertEquals(Column.getColumnType(double.class), Types.DOUBLE);
        assertEquals(Column.getColumnType(BigDecimal.class), Types.NUMERIC);
        assertEquals(Column.getColumnType(BigInteger.class), Types.NUMERIC);
        assertEquals(Column.getColumnType(BigInteger.class), Types.NUMERIC);
        assertEquals(Column.getColumnType(Calendar.class), Types.TIMESTAMP);
        assertEquals(Column.getColumnType(String.class), Types.VARCHAR);
    }
    
    /** Test failed Java type to SQL type conversion. */
    @Test(expectedExceptions = AddendumException.class)
    public void noSuchColumnType() {
        try {
            Column.getColumnType(ByteBuffered.class);
        } catch (AddendumException e) {
            assertEquals(e.getCode(), UNMAPPABLE_TYPE);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test setting the column type from a Java type. */
    @Test
    public void setColumnTypeJava() {
        Column column = new Column("a", long.class);
        column.setColumnType(int.class);
        assertEquals(column.getColumnType(), Types.INTEGER);
    }
}
