package com.goodworkalan.addendum.api;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.ColumnType;
import com.goodworkalan.addendum.NamingConnector;

public class AddendaAPITest
{
    public void addendum()
    {
        Addenda addenda = new Addenda(new NamingConnector(""));
        
        addenda
            .addendum()
                .createTable("Person")
                    .column("firstName", ColumnType.VARCHAR).length(64).end()
                    .column("lastName", ColumnType.VARCHAR).length(64).end()
                    .end()
                .createTable("Address")
                    .column("address", ColumnType.VARCHAR).length(64).end()
                    .column("city", ColumnType.VARCHAR).length(64).end()
                    .column("state", ColumnType.VARCHAR).length(64).end()
                    .column("zip", ColumnType.VARCHAR).length(64).end()
                    .end()
               .insert("Person")
                   .columns("firstName", "lastName").values("Alan", "Gutierrez")
                   .end();
    }
}
