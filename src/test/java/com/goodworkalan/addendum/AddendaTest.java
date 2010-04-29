package com.goodworkalan.addendum;

import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDA_COUNT;
import static com.goodworkalan.addendum.AddendumException.SQL_ADDENDUM;
import static com.goodworkalan.addendum.AddendumException.SQL_CREATE_ADDENDA;
import static com.goodworkalan.addendum.AddendumException.SQL_GET_DIALECT;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.api.ExampleMigration;
import com.goodworkalan.addendum.api.MockConnector;
import com.goodworkalan.addendum.api.MockDialect;

public class AddendaTest {
    @Test(expectedExceptions = AddendumException.class)
    public void getDialect() {
        Addenda addenda = new Addenda(new MockConnector(), new DialectProvider() {
            public Dialect getDialect(Connection connection)
            throws SQLException {
                throw new SQLException();
            }
        });
        try {
            addenda.amend();
        } catch (AddendumException e) {
            assertEquals(e.getCode(), SQL_GET_DIALECT);
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    @Test(expectedExceptions = AddendumException.class)
    public void createAddenda() {
        Addenda addenda = new Addenda(new MockConnector(), new DialectProvider() {
            public Dialect getDialect(Connection connection) {
                return new MockDialect() {
                    @Override
                    public void createAddendaTable(Connection connection)
                    throws SQLException {
                        throw new SQLException();
                    }
                };
            }
        });
        try {
            addenda.amend();
        } catch (AddendumException e) {
            assertEquals(e.getCode(), SQL_CREATE_ADDENDA);
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Test(expectedExceptions = AddendumException.class)
    public void addedaCount() {
        Addenda addenda = new Addenda(new MockConnector(), new DialectProvider() {
           public Dialect getDialect(Connection connection)
           throws SQLException {
               return new MockDialect() {
                   @Override
                   public int addendaCount(Connection connection)
                   throws SQLException {
                       throw new SQLException();
                   }
               };
            } 
        });
        try {
            addenda.amend();
        } catch (AddendumException e) {
            assertEquals(e.getCode(), SQL_ADDENDA_COUNT);
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Test(expectedExceptions = AddendumException.class)
    public void addendum() {
        Addenda addenda = new Addenda(new MockConnector(), new DialectProvider() {
           public Dialect getDialect(Connection connection)
           throws SQLException {
               return new MockDialect() {
                   @Override
                   public void addendum(Connection connection)
                   throws SQLException {
                       throw new SQLException();
                   }
               };
            } 
        });
        addenda.addendum();
        try {
            addenda.amend();
        } catch (AddendumException e) {
            assertEquals(e.getCode(), SQL_ADDENDUM);
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private void createPersonAndAddress(Addenda addenda) {
        addenda
            .addendum()
                .define("Person")
                    .add("firstName", String.class).length(64).end()
                    .add("lastName", String.class).length(64).end()
                    .end()
                .define("Address")
                    .add("address", String.class).length(64).end()
                    .add("city", String.class).length(64).end()
                    .add("state", String.class).length(64).end()
                    .add("zip", String.class).length(64).end()
                    .end()
               .insert("Person")
                   .columns("firstName", "lastName").values("Alan", "Gutierrez")
                   .end();
    }

    @Test
    public void tiny() throws ClassNotFoundException, SQLException {
        MockDialect dialect = new MockDialect();
        Addenda addenda = new Addenda(new MockConnector(), dialect);
        createPersonAndAddress(addenda);
        addenda.amend();
    }

    @Test
    public void addendaTableExists()
    throws ClassNotFoundException, SQLException {
        MockDialect dialect = new MockDialect();
        Addenda addenda = new Addenda(new MockConnector(), dialect);
        createPersonAndAddress(addenda);
        addenda.amend();
        addenda.amend();
    }

    @Test
    public void alterPerson()  throws ClassNotFoundException, SQLException
    {
//        MockDialect dialect = new MockDialect();
//        Addenda addenda = new Addenda(new MockConnector(), dialect);
//        addenda
//            .addendum()
//                .alterTable("Person")
//                    .alterColumn("firstName").rename("first_name").end()
//                    .alterColumn("lastName").rename("last_name").end()
//                    .end()
//                .commit();
//        addenda.amend();
//        assertEquals(dialect.getAlterColumns().size(), 2);
//        AlterColumn alterColumn = dialect.getAlterColumns().get(0);
//        assertEquals(alterColumn.getTableName(), "Person");
//        assertEquals(alterColumn.getOldName(), "firstName");
//        assertEquals(alterColumn.getColumn().getName(), "first_name");
    }
    
    @Test
    public void addColumn() throws ClassNotFoundException, SQLException
    {
//        MockDialect dialect = new MockDialect();
//        Addenda addenda = new Addenda(new MockConnector(), dialect);
//        addenda
//            .addendum()
//                .alterTable("Person")
//                    .addColumn("firstName", String.class).length(64).end()
//                    .end()
//                .commit();
//        addenda.amend();
//        assertEquals(dialect.getAddColumns().size(), 1);
//        AddColumn addColumn = dialect.getAddColumns().get(0);
//        assertEquals(addColumn.getTableName(), "Person");
//        assertEquals(addColumn.getColumn().getName(), "firstName");
    }
    
    @Test
    public void basic() {
        Addenda addenda = new Addenda(new MockConnector());
        new ExampleMigration(addenda).create();
    }
}

/* vim: set nowrap: */
