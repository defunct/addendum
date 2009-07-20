package com.goodworkalan.addendum.jpa.api;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.jpa.AlterEntity;
import com.goodworkalan.addendum.jpa.CreateEntity;

public class AddendumJpaPublic
{
    public void test(Addenda addenda)
    {
        addenda
            .addendum()
                .create(new CreateEntity(Person.class)).end()
                .commit();
        addenda
            .addendum()
                .alter(new AlterEntity(Person.class)).renameFrom("Employee").end()
                .commit();
        addenda
            .addendum()
                .alter(new AlterEntity(Person.class)).addProperty("socialSecurityNumber").end()
                .commit();
    }
}
