/* Copyright Alan Gutierrez 2006 */
package com.agtrz.addendum;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Addendum
{
    private static final Logger log = LoggerFactory.getLogger(Addendum.class);
    
    private final List<Patch> listOfChanges = new ArrayList<Patch>();
    
    private final String updateTable;
    
    private final String updateColumn;
    
    private final Patch[] create;
    
    public Addendum(Patch[] create)
    {
        this.updateTable = "Configuration";
        this.updateColumn = "schemaRevision";
        this.create = create;
    }
    
    private String selectVersion()
    {
        return "SELECT " + updateColumn + " FROM " + updateTable;
    }
    
    private String updateVersion()
    {
        return "UPDATE " + updateTable + " SET " + updateColumn + " = ?";
    }
    
    public void change(Connection connection) throws SQLException
    {
        tryChange(connection);
    }
    
    public void tryChange(Connection connection) throws SQLException
    {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet results = metaData.getTables(null, null, updateTable, null);
        if (results.next())
        {
            results.close();

            log.debug("Updating schema using version at " +
                updateTable + "." + updateColumn + " as starting point.");

            Statement st = connection.createStatement();
            ResultSet versions = st.executeQuery(selectVersion());
            if (versions.next())
            {
                int version = versions.getInt(1);
                
                versions.close();

                log.debug("Updating schema starting at version " + version + ".");
                
                log.info("There are currently " + listOfChanges.size() + " patches. Please make sure you start new instances at that number.");
                
                for (int i = version; i < listOfChanges.size(); i++)
                {
                    listOfChanges.get(i).execute(connection);
                    PreparedStatement statement = connection.prepareStatement(updateVersion());
                    statement.setInt(1, i + 1);
                    statement.execute();
                    if (!connection.getAutoCommit())
                    {
                        connection.commit();
                    }
                }
            }
        }
        else
        {
            results.close();
            
            results = metaData.getTables(null, null, null, null);
            
            boolean next = results.next();
            
            results.close();

            if (next)
            {
                log.info("Cannot find upgrade update number table " + updateTable + ".");
            }
            else
            {
                for (int i = 0; i < create.length; i++)
                {
                    create[i].execute(connection);
                }
                
                tryChange(connection);
            }
        }
    }
    
    public void add(Patch change)
    {
        listOfChanges.add(change);
    }
    
    public interface Patch
    {
        public void execute(Connection connnection) throws SQLException;
    }
    
    public final static class SqlPatch
    implements Patch
    {
        private final String sql;
        
        public SqlPatch(String sql)
        {
            if (sql == null)
            {
                throw new IllegalStateException();
            }

            this.sql = sql;
        }
        
        public void execute(Connection connnection) throws SQLException
        {
            log.info("Executing SQL statement <" + sql + ">");
            Statement statement = connnection.createStatement();
            statement.execute(sql);
            statement.close();
        }
    }
    
    public final static class Danger
    extends Exception
    {
        private static final long serialVersionUID = 20080620L;

        private final int code;

        public Danger(int code, Object... arguments)
        {
            super(message(code, arguments));
            this.code = code;
        }

        public Danger(int code, Throwable cause, Object... arguments)
        {
            super(message(code, arguments), cause);
            this.code = code;
        }

        private static String message(Integer code, Object[] arguments)
        {
            ResourceBundle exceptions = ResourceBundle.getBundle("com.agtrz.addendum.exceptions");
            ResourceBundle userExceptions = null; // ResourceBundle.getBundle("/META-INF/addendum/exceptions");
            String format = null;
            if ((format = exceptions.getString(code.toString())) == null)
            {
                if (userExceptions != null && (format = userExceptions.getString(code.toString())) == null)
                {
                    format = exceptions.getString("no-format");
                }
            }
            return MessageFormat.format(format, arguments);
        }

        public int getCode()
        {
            return code;
        }
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */