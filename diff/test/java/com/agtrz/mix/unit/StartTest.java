/* Copyright Alan Gutierrez 2006 */
package com.agtrz.mix.unit;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.naming.NamingException;

import org.testng.annotations.Test;

import com.agtrz.mix.jetty.Start;

public class StartTest
{
    @Test
    public void test() throws IOException, InterruptedException
    {
        String[] args = new String[]
        {
            "-c", "jdbc:mysql://localhost/mix",
            "-u", "mix",
            "-p", "password",
            "-d", "java:comp/env/jdbc/mix"
        };
        run(args, 1);
    }

    @Test
    public void testNoUser() throws IOException, InterruptedException
    {
        String[] args = new String[]
        {
            "-c", "jdbc:mysql://localhost/mix",
            "-d", "java:comp/env/jdbc/mix"
        };
        run(args, 8000);
    }
    
    @Test
    public void testBadArgument() throws NamingException
    {
        PrintStream err = System.err;
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        System.setErr(new PrintStream(message));
        Start.main(new String[] { "-x" });
        System.setErr(err);
        String expected = "\"-x\" is not a valid option\n" +
            "java -jar myprogram.jar [options...] arguments...\n" +
            " -c VAL : MySQL connection url.\n" +
            " -d VAL : Datasource named in persistence.xml\n" +
            " -p VAL : Password.\n" +
            " -u VAL : User name.\n";
        assertEquals(message.toString(), expected);
    }
    
    private void run(final String[] args, int sleep) throws IOException, InterruptedException
    {
        PipedOutputStream stop = new PipedOutputStream();
        InputStream in = System.in;
        System.setIn(new PipedInputStream(stop));
        Thread thread = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    Start.main(args);
                }
                catch (NamingException e)
                {
                }
            }
        });
        thread.start();
        Thread.sleep(sleep);
        stop.write(1);
        stop.flush();
        thread.join();
        System.setIn(in);
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */