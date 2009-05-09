package com.goodworkalan.addendum.api;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.NamingConnector;

public class AddendaAPITest
{
    public void addendum()
    {
        Addenda addenda = new Addenda(new NamingConnector(""));
        
        addenda
            .addendum()
                .createTable("Person")
                    .column("firstName", String.class).length(64).end()
                    .column("lastName", String.class).length(64).end()
                    .end()
                .createTable("Address")
                    .column("address", String.class).length(64).end()
                    .column("city", String.class).length(64).end()
                    .column("state", String.class).length(64).end()
                    .column("zip", String.class).length(64).end()
                    .end()
               .insert("Person")
                   .columns("firstName", "lastName").values("Alan", "Gutierrez")
                   .end();
    }
}
