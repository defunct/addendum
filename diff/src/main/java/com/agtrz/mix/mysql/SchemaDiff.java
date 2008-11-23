/* Copyright Alan Gutierrez 2006 */
package com.agtrz.mix.mysql;

import java.util.HashMap;

import javax.naming.NamingException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.mortbay.jetty.plus.naming.Resource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class SchemaDiff
{
    @Option(name = "-U", usage = "Persistance unit.")
    private String persistanceUnit = "default";

    @Option(name = "-d", usage = "Datasource named in persistence.xml")
    private String dataSourceName;
    
    @Option(name = "-c", usage = "MySQL connection url.")
    private String url;

    @Option(name = "-u", usage = "User name.")
    private String user;

    @Option(name = "-p", usage = "Password.")
    private String password;

    public static void main(String[] args) throws NamingException
    {
        SchemaDiff diff = new SchemaDiff();
        CmdLineParser parser = new CmdLineParser(diff);
        try
        {
            parser.parseArgument(args);
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage());
            System.err.println("java -jar myprogram.jar [options...] arguments...");
            parser.printUsage(System.err);
            return;
        }
        diff.difference();
    }
    
    public void difference() throws NamingException
    {
        Logger.getRootLogger().setLevel(Level.FATAL);
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(url);
        if (user != null)
        {
            dataSource.setUser(user);
        }
        if (password != null)
        {
            dataSource.setPassword(password);
        }
        new Resource(dataSourceName, dataSource);
        Ejb3Configuration ejbc= new Ejb3Configuration();
        ejbc.configure(persistanceUnit, new HashMap<Object, Object>());
        SchemaUpdate se = new SchemaUpdate( ejbc.getHibernateConfiguration() );
        se.execute(true, false);
        if (se.getExceptions().size() != 0)
        {
            throw new IllegalStateException((Throwable) se.getExceptions().get(0));
        }
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */