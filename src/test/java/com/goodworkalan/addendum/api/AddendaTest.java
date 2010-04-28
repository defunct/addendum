package com.goodworkalan.addendum.api;

import java.sql.SQLException;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.Addenda;


public class AddendaTest
{
    private void createPersonAndAddress(Addenda addenda)
    {
        addenda
            .addendum()
                .table("Person")
                    .column("firstName", String.class).length(64).end()
                    .column("lastName", String.class).length(64).end()
                    .end()
                .table("Address")
                    .column("address", String.class).length(64).end()
                    .column("city", String.class).length(64).end()
                    .column("state", String.class).length(64).end()
                    .column("zip", String.class).length(64).end()
                    .end()
               .insert("Person")
                   .columns("firstName", "lastName").values("Alan", "Gutierrez")
                   .end();
    }

    @Test
    public void addendum() throws ClassNotFoundException, SQLException
    {
        MockDialect dialect = new MockDialect();
        Addenda addenda = new Addenda(new MockConnector(), dialect);
        createPersonAndAddress(addenda);
        addenda.amend();
    }

    @Test
    public void addendaTableExists() throws ClassNotFoundException, SQLException
    {
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
        Addenda addenda = new Addenda(new MockConnector()) {
            @Override
            public void create() {
                addendum()
                    .table("Employee")
                        .column("firstName", String.class).end()
                        .column("lastName", String.class).end()
                        .column("city", String.class).end()
                        .end()
                    .table("Employee").create()
                    .commit();
                addendum(BlogDefinition.class)
                    .table("Post").create()
                    .table("Comment").create()
                    .commit();
                addendum()
                    .alter()
                        .rename("Employee").to("Person")
                        .end()
                    .commit();
            }
        };
        addenda.create();
    }
}

/* vim: set nowrap: */
