package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendaTest.exceptional;
import static com.goodworkalan.addendum.Addendum.ADDENDUM_ENTITY_EXISTS;
import static com.goodworkalan.addendum.Addendum.ADDENDUM_TABLE_EXISTS;
import static com.goodworkalan.addendum.Addendum.COLUMN_EXISTS;
import static com.goodworkalan.addendum.Addendum.COLUMN_MISSING;
import static com.goodworkalan.addendum.Addendum.ENTITY_EXISTS;
import static com.goodworkalan.addendum.Addendum.ENTITY_MISSING;
import static com.goodworkalan.addendum.Addendum.INSERT_VALUES;
import static com.goodworkalan.addendum.Addendum.PRIMARY_KEY_EXISTS;
import static com.goodworkalan.addendum.Addendum.PROPERTY_EXISTS;
import static com.goodworkalan.addendum.Addendum.PROPERTY_MISSING;
import static com.goodworkalan.addendum.Addendum.TABLE_EXISTS;
import static com.goodworkalan.addendum.Addendum.TABLE_MISSING;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import com.goodworkalan.addendum.connector.MockConnector;
import com.goodworkalan.addendum.dialect.Dialect;
/**
 * Unit tests for the {@link Addendum} class.
 *
 * @author Alan Gutierrez
 */
public class AddendumTest {
    /** Test duplicate definition of an entity name. */
 //  @Test(expectedExceptions = Danger.class)
    public void addendumEntityExists() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .define("a")
                            .add("a", int.class).end()
                            .end()
                        .define("a");
            }
        }, ADDENDUM_ENTITY_EXISTS, "An entity definition by the name of [a] already exists in this addendum.");
    }

    /** Test duplicate definition of an entity name. */
 //  @Test(expectedExceptions = Danger.class)
    public void addendumTableExists() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .define("a")
                            .add("a", int.class).end()
                            .end()
                        .define("b", "a");
            }
        }, ADDENDUM_TABLE_EXISTS, "An entity definition with a table by the name of [a] already exists in this addendum.");
    }

    /** Test duplicate definition of an entity name. */
 //   @Test(expectedExceptions = Danger.class)
    public void entityExists() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .create("a")
                            .add("a", int.class).end()
                            .end()
                        .create("a");
            }
        }, ENTITY_EXISTS, "An entity definition by the name of [a] already exists in the schema.");
    }

    /** Test duplicate definition of an entity name. */
 //  @Test(expectedExceptions = Danger.class)
    public void tableExists() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .create("a")
                            .add("a", int.class).end()
                            .end()
                        .create("b", "a");
            }
        },  TABLE_EXISTS, "An entity definition with a table by the name of [a] already exists in the schema.");
    }
    
    /**
     * Test attempting to create a table that already exists when creating
     * absent entities.
     */
 //   @Test(expectedExceptions = Danger.class)
    public void createIfAbsentTableExists() {
        exceptional(new Runnable() {
            public void run() {
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
            }
        },  TABLE_EXISTS, "An entity definition with a table by the name of [b] already exists in the schema.");
    }
    
    /**
     * Test creating all of the entities defined in the addendum if they don't
     * already exist in the schema.
     */
 //   @Test
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
 //   @Test(expectedExceptions = Danger.class)
    public void propertyExists() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .create("b", "b")
                            .add("a", int.class).end()
                            .add("a", int.class).end();
            }
        },  PROPERTY_EXISTS, "A property by the name of [a] already exists in the entity.");
    }
    
    /** Test specifying a column that already exists. */
 //  @Test(expectedExceptions = Danger.class)
    public void columnExists() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .create("b", "b")
                            .add("b", java.sql.Types.INTEGER).end()
                            .add("a", "b", int.class).end();
            }
        }, COLUMN_EXISTS, "A column by the name of [b] already exists in the table in the schema.");
    }
    
    /** Test specifying the primary key twice. */
 //   @Test(expectedExceptions = Danger.class)
    public void primaryKeyExists() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .create("b", "b")
                            .add("b", int.class).end()
                            .primaryKey("b")
                            .primaryKey("b");
            }
        }, PRIMARY_KEY_EXISTS, "The primary key has already been defined.");
    }
    
    /** Test creating new entities from addendum entity definitions. */
 //  @Test
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
 //  @Test(expectedExceptions = Danger.class)
    public void renameTableMissing() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .rename("a", "b");
            }
        }, ENTITY_MISSING, "The entity [a] cannot be found in the schema.");
    }
    
    /** Test entity rename without a table rename. */
 //   @Test
    public void renameWithoutTable() {
        Addenda addenda = new Addenda(new MockConnector());
        addenda
            .addendum()
                .create("a", "c")
                    .add("a", int.class).end()
                    .end()
                .rename("a", "b")
                .commit();
        addenda.amend();
        assertEquals(addenda.schema.aliases.get("b"), "c");
    }
    
    /** Test entity rename in alter. */
 //    @Test
    public void renameInAlter() {
        Addenda addenda = new Addenda(new MockConnector());
        addenda
            .addendum()
                .create("a")
                    .add("a", int.class).end()
                    .end()
                .alter("a")
                    .name("b")
                    .end()
                .commit();
        addenda.amend();
        assertEquals(addenda.schema.aliases.get("b"), "a");
    }
    
    /** Test rename. */
 //    @Test
    public void rename() {
        Addenda addenda = new Addenda(new MockConnector());
        addenda
            .addendum()
                .create("a")
                    .add("a", int.class).end()
                    .end()
                .rename("a", "b")
                .commit();
        addenda.amend();
        assertEquals(addenda.schema.aliases.get("b"), "b");
    }
    
    /** Test alias. */
 //   @Test
    public void alias() {
        Addenda addenda = new Addenda(new MockConnector());
        addenda
            .addendum()
                .create("a")
                    .add("a", int.class).end()
                    .end()
                .alias("a", "b")
                .commit();
        addenda.amend();
        assertEquals(addenda.schema.aliases.get("b"), "a");
    }
    
    /** Test alias without change. */
 //   @Test
    public void aliasWithoutChange() {
        Addenda addenda = new Addenda(new MockConnector());
        addenda
            .addendum()
                .create("a")
                    .add("a", int.class).end()
                    .end()
                .alias("a", "a")
                .commit();
        addenda.amend();
        assertEquals(addenda.schema.aliases.get("a"), "a");
    }

    /** Test table alias to existing entity name. */
 //    @Test(expectedExceptions = Danger.class)
    public void aliasExists() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .create("a")
                            .add("a", int.class).end()
                            .end()
                        .create("b")
                            .add("a", int.class).end()
                            .end()
                        .alias("b", "a");
            }
        }, ENTITY_EXISTS, "An entity definition by the name of [a] already exists in the schema.");
    }

    /** Test table alias of missing table. */
 //    @Test(expectedExceptions = Danger.class)
    public void aliasTableMissing() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .alias("b", "a");
            }
        }, TABLE_MISSING, "The table [b] cannot be found in the schema.");
    }
    
    /** Test entity rename to existing entity name. */
 //    @Test(expectedExceptions = Danger.class)
    public void renameExists() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .create("a")
                            .add("a", int.class).end()
                            .end()
                        .create("b")
                            .add("a", int.class).end()
                            .end()
                        .rename("a", "b");
            }
        }, ENTITY_EXISTS, "An entity definition by the name of [b] already exists in the schema.");
    }
    
    /** Test execute. */
 //    @Test
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
//    @Test
    public void renameProperty() {
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
                    .rename("a", "b")
                    .end()
                .commit();
        addenda.amend();
    }
    
    /** Test alias property. */
 //   @Test
    public void aliasProperty() {
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
                    .alias("a", "b")
                    .end()
                .commit();
        addenda.amend();
        assertEquals(addenda.schema.entities.get("a").properties.get("b"), "a");
    }
    
    /** Test alias property with no changes. */
 //   @Test
    public void aliasPropertyNoChange() {
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
                    .alias("a", "a")
                    .end()
                .commit();
        addenda.amend();
    }

    /** Test column alias with existing property name. */
 //    @Test(expectedExceptions = Danger.class)
    public void aliasPropertyExists() {
        exceptional(new Runnable() {
            public void run() {
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
                            .alias("a", "b");
            }
        }, PROPERTY_EXISTS, "A property by the name of [b] already exists in the entity.");
    }
    

    /** Test column alias with a missing column. */
 //   @Test(expectedExceptions = Danger.class)
    public void aliasPropertyColumnMissing() {
        exceptional(new Runnable() {
            public void run() {
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
                            .alias("b", "c");
            }
        }, COLUMN_MISSING, "The column [b] does not exist.");
    }

    /** Test rename property in alter. */
 //   @Test
    public void renamePropertyInAlter() {
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
                    .alter("a").name("b").end()
                    .end()
                .commit();
        addenda.amend();
        assertEquals(addenda.schema.entities.get("a").properties.get("b"), "a");
    }
    
    /** Test rename property without column rename. */
 //  @Test
    public void renamePropertyWithoutColumnRename() {
        Addenda addenda = new Addenda(new MockConnector());
        addenda
            .addendum()
                .create("a")
                     .add("a", "c", int.class).end()
                     .end()
                .commit();
        addenda
            .addendum()
                .alter("a")
                    .rename("a", "b")
                    .end()
                .commit();
        addenda.amend();
        assertEquals(addenda.schema.entities.get("a").properties.get("b"), "c");
    }
    
    /** Test entity add property with existing property name. */
 //   @Test(expectedExceptions = Danger.class)
    public void addPropertyExists() {
        exceptional(new Runnable() {
            public void run() {
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
            }
        }, PROPERTY_EXISTS, "A property by the name of [a] already exists in the entity.");
    }
    
    /** Test add property with existing column name. */
 //   @Test(expectedExceptions = Danger.class)
    public void addColumnExists() {
        exceptional(new Runnable() {
            public void run() {
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
            }
        }, COLUMN_EXISTS, "A column by the name of [b] already exists in the table in the schema.");
    }
    
    
    /** Test rename property. */
 //   @Test
    public void addProperty() {
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
                    .add("b", int.class).end()
                    .end()
                .commit();
        addenda.amend();
    }
    
    /** Test drop property. */
 //   @Test
    public void dropProperty() {
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
                    .drop("b")
                    .end()
                .commit();
        addenda.amend();
    }
    
    
    /** Test drop property. */
 //   @Test(expectedExceptions = Danger.class)
    public void dropMissingProperty() {
        exceptional(new Runnable() {
            public void run() {
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
                            .drop("b");
            }
        }, PROPERTY_MISSING, "The property [b] does not exist.");
    }
    
    /** Test drop property. */
 //   @Test
    public void setColumn() {
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
                    .alter("a").column("b").end()
                    .end()
                .commit();
        addenda.amend();
    }
    
    /** Test alter type. */
 //    @Test
    public void alterType() {
        Addenda addenda = new Addenda(new MockConnector());
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
 //   @Test
    public void alterNull() {
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
 //   @Test(expectedExceptions = Danger.class)
    public void insertMismatch() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .create("a")
                            .add("a", int.class).end()
                            .add("b", int.class).end()
                            .end()
                        .insert("a").columns("a", "b").values("1")
                        .commit();
                addenda.amend();
            }
        }, INSERT_VALUES, "Insert statement values count does not match column count.");
    }
    
    /** Test rename property to existing name. */
 //   @Test(expectedExceptions = Danger.class)
    public void propertyRenameExists() {
        exceptional(new Runnable() {
            public void run() {
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
                            .rename("a", "b");
            }
        }, PROPERTY_EXISTS, "A property by the name of [b] already exists in the entity.");
    }
    
    /** Test set table name.. */
 //   @Test(expectedExceptions = Danger.class)
    public void table() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .create("a")
                            .add("a", int.class).end()
                            .end()
                        .alter("a").table("b").end()
                        .create("b");
            }
        }, TABLE_EXISTS, "An entity definition with a table by the name of [b] already exists in the schema.");
    }
    
    /** Test set table name.. */
 //   @Test(expectedExceptions = Danger.class)
    public void columnRenameColumnExists() {
        exceptional(new Runnable() {
            public void run() {
                Addenda addenda = new Addenda(new MockConnector());
                addenda
                    .addendum()
                        .create("a")
                            .add("a", int.class).end()
                            .add("b", int.class).end()
                            .end()
                        .alter("a")
                            .alter("a").column("b").end();
            }
        }, COLUMN_EXISTS, "A column by the name of [b] already exists in the table in the schema.");
    }
}
