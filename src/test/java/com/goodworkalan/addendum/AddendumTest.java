package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.ADDENDUM_ENTITY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.ADDENDUM_TABLE_EXISTS;
import static com.goodworkalan.addendum.AddendumException.COLUMN_EXISTS;
import static com.goodworkalan.addendum.AddendumException.CREATE_DEFINITION;
import static com.goodworkalan.addendum.AddendumException.ENTITY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.PRIMARY_KEY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.PROPERTY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.TABLE_EXISTS;
import static com.goodworkalan.addendum.AddendumException.TABLE_MISSING;
import static com.goodworkalan.reflective.ReflectiveException.ILLEGAL_ACCESS;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.api.MockConnector;
import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

/**
 * Unit tests for the {@link Addendum} class.
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
    
    /**
     * Test attempting to create a table that already exists when creating
     * absent entities.
     */
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
    
    /**
     * Test creating all of the entities defined in the addendum if they don't
     * already exist in the schema.
     */
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
    
    /** Test specifying a property that already exists. */
    @Test(expectedExceptions = AddendumException.class)
    public void propertyExists() {
        try {
            Addenda addenda = new Addenda(new MockConnector());
            addenda
                .addendum()
                    .create("b", "b")
                        .add("a", int.class).end()
                        .add("a", int.class).end();
        } catch (AddendumException e) {
            assertEquals(e.getCode(), PROPERTY_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test specifying a column that already exists. */
    @Test(expectedExceptions = AddendumException.class)
    public void columnExists() {
        try {
            Addenda addenda = new Addenda(new MockConnector());
            addenda
                .addendum()
                    .create("b", "b")
                        .add("b", java.sql.Types.INTEGER).end()
                        .add("a", "b", int.class).end();
        } catch (AddendumException e) {
            assertEquals(e.getCode(), COLUMN_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test specifying the primary key twice. */
    @Test(expectedExceptions = AddendumException.class)
    public void primaryKeyExists() {
        try {
            Addenda addenda = new Addenda(new MockConnector());
            addenda
                .addendum()
                    .create("b", "b")
                        .add("b", int.class).end()
                        .primaryKey("b")
                        .primaryKey("b");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), PRIMARY_KEY_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test creating new entities from addendum entity definitions. */
    @Test
    public void createDefinitions() {
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
                .createDefinitions("a")
                .commit();
    }
    
    /** Test renaming an entity that does not exist. */
    @Test(expectedExceptions = AddendumException.class)
    public void renameTableMissiong() {
        try {
            Addenda addenda = new Addenda(new MockConnector());
            addenda
                .addendum()
                    .rename("a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), TABLE_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test entity rename without a table rename. */
    @Test
    public void renameWithoutTable() {
        Addenda addenda = new Addenda(new MockConnector());
        addenda
            .addendum()
                .create("a", "c")
                    .add("a", int.class).end()
                    .end()
                .rename("a").to("b")
                    .commit();
    }
    
    
    /** Test entity rename to existing entity name. */
    @Test(expectedExceptions = AddendumException.class)
    public void renameExists() {
        try {
            Addenda addenda = new Addenda(new MockConnector());
            addenda
                .addendum()
                    .create("a")
                        .add("a", int.class).end()
                        .end()
                    .create("b")
                        .add("a", int.class).end()
                        .end()
                    .rename("a").to("b");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), ENTITY_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test execute. */
    @Test
    public void execute() {
        Addenda addenda = new Addenda(new MockConnector());
        addenda
            .addendum()
                .execute(new Executable() {
                    public void execute(Connection connection, Dialect dialect)
                    throws SQLException {
                    }
                })
                .commit();
    }
}
