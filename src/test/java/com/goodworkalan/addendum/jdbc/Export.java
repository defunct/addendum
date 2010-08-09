package com.goodworkalan.addendum.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import com.goodworkalan.addendum.connector.Connector;
import com.goodworkalan.addendum.connector.DriverManagerConnector;

/**
 * Export a specific database.
 *
 * @author Alan Gutierrez
 */
public class Export {
    /**
     * Emit an Addendeum <code>Definition</code> to standard from a JDBC
     * connection.
     * 
     * @param args
     *            Driver class name followed by connect string.
     * @throws SQLException
     *             For any SQL error.
     * @throws ClassNotFoundException
     *             If the driver cannot be found.
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName(args[0]); // com.mysql.jdbc.Driver 
        Connector connector = new DriverManagerConnector(args[1], null, null); // jdbc:mysql://localhost/xerox_development?user=xerox
        Connection connection = connector.open();
        DefinitionGenerator.generate("com.goodworkalan.accounts.Accounts", connection);
        connector.close(connection);
    }
}
