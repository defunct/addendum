/* Copyright Alan Gutierrez 2006 */
package com.agtrz.mix.unit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.testng.Assert.assertEquals;

import javax.naming.NamingException;

import org.testng.annotations.Test;

import com.agtrz.mix.mysql.SchemaDiff;

public class SchemaDiffTest
{
    @Test
    public void test() throws NamingException
    {
        String[] args = new String[]
        {
            "-c", "jdbc:mysql://localhost/mix",
            "-u", "mix",
            "-d", "java:comp/env/jdbc/mix"
        };
        run(args);
    }
    
    @Test(expectedExceptions = IllegalStateException.class)
    public void noUser() throws NamingException
    {
        String[] args = new String[]
        {
            "-c", "jdbc:mysql://localhost/mix",
            "-d", "java:comp/env/jdbc/mix"
        };
        run(args);
    }
    
    @Test(expectedExceptions = IllegalStateException.class)
    public void noWithPassword() throws NamingException
    {
        String[] args = new String[]
        {
            "-c", "jdbc:mysql://localhost/mix",
            "-u", "mix",
            "-p", "password",
            "-d", "java:comp/env/jdbc/mix"
        };
        run(args);
    }

    @Test
    public void badArguements() throws NamingException
    {
        PrintStream err = System.err;
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        System.setErr(new PrintStream(message));
        SchemaDiff.main(new String[] { "-x" });
        System.setErr(err);
        String expected = "\"-x\" is not a valid option\n" +
            "java -jar myprogram.jar [options...] arguments...\n" +
            " -U VAL : Persistance unit.\n" +
            " -c VAL : MySQL connection url.\n" +
            " -d VAL : Datasource named in persistence.xml\n" +
            " -p VAL : Password.\n" +
            " -u VAL : User name.\n";
        assertEquals(message.toString(), expected);
    }

    private void run(String[] args)  throws NamingException
    {
        PrintStream out = System.out;
        ByteArrayOutputStream script = new ByteArrayOutputStream();
        System.setOut(new PrintStream(script));
        SchemaDiff.main(args);
        assertEquals(script.toString(), "create table Configuration (id bigint not null auto_increment, primary key (id))\n");
        System.setOut(out);
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */