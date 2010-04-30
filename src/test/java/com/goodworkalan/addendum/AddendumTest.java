package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.*;
import static com.goodworkalan.reflective.ReflectiveException.ILLEGAL_ACCESS;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.api.MockConnector;
import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

/**
 * Unit tests for {@link Addendum}.
 *
 * @author Alan Gutierrez
 */
public class AddendumTest {
    /** Test the failure of the creation of a {@link Definition}. */
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
    
    /** Test duplicate definition of an entity name. */
    @Test(expectedExceptions = AddendumException.class)
    public void addendumEntityExists() {
        Addenda addenda = new Addenda(new MockConnector());
        try {
            addenda
                .addendum()
                    .define("a")
                        .add("a", int.class).end()
                        .end()
                    .define("a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), ADDENDUM_ENTITY_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /** Test duplicate definition of an entity name. */
    @Test(expectedExceptions = AddendumException.class)
    public void addendumTableExists() {
        Addenda addenda = new Addenda(new MockConnector());
        try {
            addenda
                .addendum()
                    .define("a")
                        .add("a", int.class).end()
                        .end()
                    .define("b", "a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), ADDENDUM_TABLE_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /** Test duplicate definition of an entity name. */
    @Test(expectedExceptions = AddendumException.class)
    public void entityExists() {
        Addenda addenda = new Addenda(new MockConnector());
        try {
            addenda
                .addendum()
                    .create("a")
                        .add("a", int.class).end()
                        .end()
                    .create("a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), ENTITY_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /** Test duplicate definition of an entity name. */
    @Test(expectedExceptions = AddendumException.class)
    public void tableExists() {
        Addenda addenda = new Addenda(new MockConnector());
        try {
            addenda
                .addendum()
                    .create("a")
                        .add("a", int.class).end()
                        .end()
                    .create("b", "a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), TABLE_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    @Test(expectedExceptions = AddendumException.class)
    public void createIfAbsentTableExists() {
        try {
            Addenda addenda = new Addenda(new MockConnector());
            addenda
                .addendum()
                    .create("b", "b")
                        .add("a", int.class).end()
                        .end()
                    .define("a", "b")
                        .add("a", int.class).end()
                        .end()
                    .createIfAbsent()
                    .commit();
        } catch (AddendumException e) {
            assertEquals(e.getCode(), TABLE_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    @Test
    public void createIfAbsent() {
        Addenda addenda = new Addenda(new MockConnector());
        addenda
            .addendum()
                .create("b", "b")
                    .add("a", int.class).end()
                    .end()
                .commit();
        addenda
            .addendum()
                .define("b", "b")
                    .add("a", int.class).end()
                    .end()
                .define("a", "a")
                    .add("a", int.class).end()
                    .end()
                .createIfAbsent()
                .commit();
    }
}
