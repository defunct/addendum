package com.goodworkalan.addendum;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.dialect.Column;

/**
 * Unit tests for {@link SpecifyProperty}.
 * 
 * @author Alan Gutierrez
 */
public class SpecifyPropertyTest {
    /** Test scale. */
 //   @Test
    public void scale() {
        Column column = new Column("a", int.class);
        CreateProperty add = new CreateProperty(null, column);
        assertSame(add, add.scale(1));
        assertEquals(column.getScale(), new Integer(1));
    }
    
    /** Test precision. */
 //   @Test
    public void precision() {
        Column column = new Column("a", int.class);
        CreateProperty add = new CreateProperty(null, column);
        assertSame(add, add.precision(1));
        assertEquals(column.getPrecision(), new Integer(1));
    }
}
