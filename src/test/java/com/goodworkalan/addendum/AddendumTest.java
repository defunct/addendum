package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.CREATE_DEFINITION;
import static com.goodworkalan.reflective.ReflectiveException.ILLEGAL_ACCESS;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.api.BlogDefinition;
import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

public class AddendumTest {
    @Test(expectedExceptions = AddendumException.class)
    public void newDefinitionInstnace() {
        ReflectiveFactory reflective = new ReflectiveFactory() {
            @Override
            public <T> T newInstance(Class<T> type) throws ReflectiveException {
                try {
                    throw new IllegalAccessException();
                } catch (IllegalAccessException e) {
                    throw new ReflectiveException(ILLEGAL_ACCESS, e);
                }
            }
        };
        try {
            Addendum.newInstance(reflective, BlogDefinition.class);
        } catch (AddendumException e) {
            assertEquals(e.getCode(), CREATE_DEFINITION);
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
