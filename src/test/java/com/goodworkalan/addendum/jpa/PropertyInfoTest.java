package com.goodworkalan.addendum.jpa;

import org.testng.annotations.Test;
import static org.testng.Assert.*;
public class PropertyInfoTest {
    @Test
    public void getId() {
        assertEquals(PropertyInfo.getId(GetterId.class), int.class);
        assertEquals(PropertyInfo.getId(SetterId.class), int.class);
        assertEquals(PropertyInfo.getId(FieldId.class), int.class);
        assertNull(PropertyInfo.getId(String.class));
        assertNull(PropertyInfo.getId(SetOnly.class));
    }
}
