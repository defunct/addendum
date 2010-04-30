package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.ADDENDUM_ENTITY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.ADDENDUM_TABLE_EXISTS;
import static com.goodworkalan.addendum.AddendumException.COLUMN_EXISTS;
import static com.goodworkalan.addendum.AddendumException.CREATE_DEFINITION;
import static com.goodworkalan.addendum.AddendumException.ENTITY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.ENTITY_MISSING;
import static com.goodworkalan.addendum.AddendumException.INSERT_VALUES;
import static com.goodworkalan.addendum.AddendumException.PRIMARY_KEY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.PROPERTY_EXISTS;
import static com.goodworkalan.addendum.AddendumException.PROPERTY_MISSING;
import static com.goodworkalan.addendum.AddendumException.TABLE_EXISTS;
import static com.goodworkalan.reflective.ReflectiveException.ILLEGAL_ACCESS;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.connector.MockConnector;
import com.goodworkalan.addendum.dialect.Dialect;
import com.goodworkalan.addendum.dialect.MockDialect;
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
    public void renameTableMissing() {
        try {
            Addenda addenda = new Addenda(new MockConnector());
            addenda
                .addendum()
                    .rename("a");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), ENTITY_MISSING);
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
    
    /** Test rename. */
    @Test
    public void rename() {
        Addenda addenda = new Addenda(new MockConnector(), new MockDialect());
        addenda
            .addendum()
                .create("a")
                    .add("a", int.class).end()
                    .end()
                .rename("a").to("b")
                .commit();
        addenda.amend();
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
    
    /** Test rename property. */
    @Test
    public void renameProperty() {
        Addenda addenda = new Addenda(new MockConnector(), new MockDialect());
        addenda
            .addendum()
                .create("a")
                     .add("a", int.class).end()
                     .end()
                .commit();
        addenda
            .addendum()
                .alter("a")
                    .rename("a").to("b")
                    .end()
                .commit();
        addenda.amend();
    }
    
    /** Test rename property without column rename. */
    @Test
    public void renamePropertyWithoutColumnRename() {
        Addenda addenda = new Addenda(new MockConnector(), new MockDialect());
        addenda
            .addendum()
                .create("a")
                     .add("a", "c", int.class).end()
                     .end()
                .commit();
        addenda
            .addendum()
                .alter("a")
                    .rename("a").to("b")
                    .end()
                .commit();
        addenda.amend();
    }
    
    /** Test entity add property with existing property name. */
    @Test(expectedExceptions = AddendumException.class)
    public void addPropertyExists() {
        try {
            Addenda addenda = new Addenda(new MockConnector());
            addenda
                .addendum()
                    .create("a")
                        .add("a", int.class).end()
                        .end()
                    .commit();
            addenda
                .addendum()
                    .alter("a")
                        .add("a", Types.INTEGER);
        } catch (AddendumException e) {
            assertEquals(e.getCode(), PROPERTY_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test add property with existing column name. */
    @Test(expectedExceptions = AddendumException.class)
    public void addColumnExists() {
        try {
            Addenda addenda = new Addenda(new MockConnector());
            addenda
                .addendum()
                    .create("a")
                        .add("a", int.class).end()
                        .end()
                    .commit();
            addenda
                .addendum()
                    .alter("a")
                        .add("b", "a", Types.INTEGER);
        } catch (AddendumException e) {
            assertEquals(e.getCode(), COLUMN_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    
    /** Test rename property. */
    @Test
    public void addProperty() {
        Addenda addenda = new Addenda(new MockConnector(), new MockDialect());
        addenda
            .addendum()
                .create("a")
                     .add("a", int.class).end()
                     .end()
                .commit();
        addenda
            .addendum()
                .alter("a")
                    .add("b", int.class).end()
                    .end()
                .commit();
        addenda.amend();
    }
    
    /** Test drop property. */
    @Test
    public void dropProperty() {
        Addenda addenda = new Addenda(new MockConnector(), new MockDialect());
        addenda
            .addendum()
                .create("a")
                     .add("a", int.class).end()
                     .add("b", int.class).end()
                     .end()
                .commit();
        addenda
            .addendum()
                .alter("a")
                    .drop("b")
                    .end()
                .commit();
        addenda.amend();
    }
    
    
    /** Test drop property. */
    @Test(expectedExceptions = AddendumException.class)
    public void dropMissingProperty() {
        try {
            Addenda addenda = new Addenda(new MockConnector(), new MockDialect());
            addenda
                .addendum()
                    .create("a")
                         .add("a", int.class).end()
                         .end()
                    .commit();
            addenda
                .addendum()
                    .alter("a")
                        .drop("b");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), PROPERTY_MISSING);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test drop property. */
    @Test
    public void setColumn() {
        Addenda addenda = new Addenda(new MockConnector(), new MockDialect());
        addenda
            .addendum()
                .create("a")
                     .add("a", int.class).end()
                     .end()
                .commit();
        addenda
            .addendum()
                .alter("a")
                    .alter("a").column("b").end()
                    .end()
                .commit();
        addenda.amend();
    }
    
    /** Test alter type. */
    @Test
    public void alterType() {
        Addenda addenda = new Addenda(new MockConnector(), new MockDialect());
        addenda
            .addendum()
                .create("a")
                     .add("a", short.class).end()
                     .end()
                .commit();
        addenda
            .addendum()
                .alter("a")
                    .alter("a").type(Types.INTEGER).end()
                    .end()
                .commit();
        addenda
        .addendum()
            .alter("a")
                .alter("a").type(long.class).end()
                .end()
            .commit();
        addenda.amend();
    }

    /** Test alter not null. */
    @Test
    public void alterNull() {
        Addenda addenda = new Addenda(new MockConnector(), new MockDialect());
        addenda
            .addendum()
                .create("a")
                     .add("a", int.class).end()
                     .end()
                .commit();
        addenda
            .addendum()
                .alter("a")
                    .alter("a").notNull(1).end()
                    .end()
                .commit();
        addenda
        .addendum()
            .alter("a")
                .alter("a").nullable().end()
                .end()
            .commit();
        addenda.amend();
    }
    
    /** Test insert mismatch. */
    @Test(expectedExceptions = AddendumException.class)
    public void insertMismatch() {
        try {
            Addenda addenda = new Addenda(new MockConnector(), new MockDialect());
            addenda
                .addendum()
                    .create("a")
                        .add("a", int.class).end()
                        .add("b", int.class).end()
                        .end()
                    .insert("a").columns("a", "b").values("1")
                    .commit();
            addenda.amend();
        } catch (AddendumException e) {
            assertEquals(e.getCode(), INSERT_VALUES);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test rename property to existing name. */
    @Test(expectedExceptions = AddendumException.class)
    public void propertyRenameExists() {
        try {
            Addenda addenda = new Addenda(new MockConnector());
            addenda
                .addendum()
                    .create("a")
                        .add("a", int.class).end()
                        .add("b", int.class).end()
                        .end()
                    .commit();
            addenda
                .addendum()
                    .alter("a")
                        .rename("a").to("b");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), PROPERTY_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test set table name.. */
    @Test(expectedExceptions = AddendumException.class)
    public void table() {
        Addenda addenda = new Addenda(new MockConnector());
        try {
            addenda
                .addendum()
                    .create("a")
                        .add("a", int.class).end()
                        .end()
                    .alter("a").table("b").end()
                    .create("b");
        } catch (AddendumException e) {
            assertEquals(e.getCode(), TABLE_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    /** Test set table name.. */
    @Test(expectedExceptions = AddendumException.class)
    public void columnRenameColumnExists() {
        Addenda addenda = new Addenda(new MockConnector());
        try {
            addenda
                .addendum()
                    .create("a")
                        .add("a", int.class).end()
                        .add("b", int.class).end()
                        .end()
                    .alter("a")
                        .alter("a").column("b").end();
        } catch (AddendumException e) {
            assertEquals(e.getCode(), COLUMN_EXISTS);
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
