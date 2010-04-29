package com.goodworkalan.addendum;

import java.util.HashMap;
import java.util.Map;

/**
 * Psuedo-database to track changes to the schema.
 *
 * @author Alan Gutierrez
 */
class Schema {
    public final Map<String, Entity> tables = new HashMap<String, Entity>();
    public final Map<String, String> aliases = new HashMap<String, String>();
}
