/* Copyright Alan Gutierrez 2006 */
package com.agtrz.addendum;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Addendums
{
    static final Logger log = LoggerFactory.getLogger(Addendums.class);
    
    private final List<Addendum> listOfBootstraps = new ArrayList<Addendum>();
    
    private final List<Addendum> listOfChanges = new ArrayList<Addendum>();
    
    private final String updateTable;
    
    private final String updateColumn;
    
    public Addendums(String updateTable, String updateColumn)
    {
        this.updateTable = updateTable;
        this.updateColumn = updateColumn;
    }
    
    private String selectVersion()
    {
        return "SELECT " + updateColumn + " FROM " + updateTable;
    }
    
    private String updateVersion()
    {
        return "UPDATE " + updateTable + " SET " + updateColumn + " = ?";
    }
    
    public void change(Connection connection) throws SQLException, AddendumException
    {
        tryChange(connection);
    }
    
    public void tryChange(Connection connection) throws SQLException, AddendumException
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
                for (Addendum addendum : listOfBootstraps)
                {
                    addendum.execute(connection);
                }
                
                tryChange(connection);
            }
        }
    }
    
    public void addBootstrap(Addendum addendum)
    {
        this.listOfBootstraps.add(addendum);
    }
    
    public void add(Addendum addendum)
    {
        listOfChanges.add(addendum);
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */