package com.goodworkalan.addendum.jpa.api;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.NamingConnector;
import com.goodworkalan.addendum.jpa.AddendumJpaException;
import com.goodworkalan.addendum.jpa.AlterEntity;
import com.goodworkalan.addendum.jpa.CreateEntity;

public class AddendumEntityTest
{
    @Test
    public void testAddPropertyNotFound()
    {
        Addenda addenda = new Addenda(new NamingConnector(""));

        try
        {
            addenda.addendum().alter(new AlterEntity(Person.class))
                    .addProperty("middleName").end().end();
        }
        catch (AddendumJpaException e)
        {
            assertEquals(e.getMessage(),
                    "The property middleName does not exist in entity Person.");
            return;
        }

        fail("Exepected exception not thrown.");
    }

    public void test(Addenda addenda)
    {
        addenda
            .addendum()
                .create(new CreateEntity(Person.class))
                    .property("hasSize").defaultValue(7).end()
                    .end()
                .commit();
        addenda
            .addendum()
                .alter(new AlterEntity(Person.class))
                    .renameFrom("Employee")
                    .end()
                .commit();
        addenda
            .addendum()
                .alter(new AlterEntity(Person.class))
                    .addProperty("socialSecurityNumber").end()
                    .end()
                .commit();
    }
}
