package com.goodworkalan.addendum;

import java.sql.Connection;
import java.sql.SQLException;

public interface Update
{
    public void execute(Connection connection, Dialect dialect) throws SQLException, AddendumException;
}
