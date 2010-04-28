package com.goodworkalan.addendum;

import java.util.HashMap;
import java.util.Map;

/**
 * Psuedo-database to track changes to the schema.
 *
 * @author Alan Gutierrez
 */
class Database {
    public final Map<String, Table> tables = new HashMap<String, Table>();
}
