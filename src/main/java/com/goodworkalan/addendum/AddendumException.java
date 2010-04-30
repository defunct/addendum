package com.goodworkalan.addendum;

import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.goodworkalan.danger.CodedDanger;

/**
 * A general purpose exception that indicates that an error occurred in one 
 * of the classes in the sheaf package.
 *   
 * @author Alan Gutierrez
 */
public final class AddendumException
extends CodedDanger {
    /** The cache of resource bundles for <code>CodedDanger</code>. */
    private final static ConcurrentMap<String, ResourceBundle> BUNDLES = new ConcurrentHashMap<String, ResourceBundle>();
    
    /** The serial version id. */
    private static final long serialVersionUID = 20080620L;
    
    /** The requested generator type is not supported by the SQL dialect. */
    public final static int DIALECT_DOES_NOT_SUPPORT_GENERATOR = 101;
    
    /** The dialog does not support a specific SQL type. */
    public final static int DIALECT_DOES_NOT_SUPPORT_TYPE = 102;
    
    /**
     * A specific dialect instance was used, but that dialect does not support
     * the database at the other end of the JDBC connection.
     */
    public final static int DIALECT_DOES_NOT_SUPPORT_CONNECTION = 103;
    
    /** Unable to open an SQL connection due to a JNI naming error. */
    public final static int NAMING_EXCEPTION = 201;
    
    /** Unable to connect to a JDBC data source. */
    public final static int SQL_CONNECT = 301;
    
    /** Unable to create the addenda table to track updates. */
    public final static int SQL_CREATE_ADDENDA = 302;
    
    /** Unable to determine the maximum value of the applied updates. */
    public final static int SQL_ADDENDA_COUNT = 303;
    
    /** Unable to update the addenda table with a new update. */
    public final static int SQL_ADDENDUM = 304;
    
    /** Unable to execute a SQL migration statement. */
    public final static int SQL_EXECUTION = 308;
    
    /** Unable to create database dialect. */
    public final static int SQL_GET_DIALECT = 309;
    
    /** Unable to close a JDBC data source. */
    public final static int SQL_CLOSE = 399;
    
    /** Insert statement DSL values count does not match column count. */
    public final static int INSERT_VALUES = 401;
    
    /** Unable to create an instance of a {@link Definition}. */
    public final static int CREATE_DEFINITION = 402;
    
    /** An entity name already exists in the addendum. */
    public final static int ADDENDUM_ENTITY_EXISTS = 403;
    
    /** A table name already exists in the addendum. */
    public final static int ADDENDUM_TABLE_EXISTS = 404;
    
    /** An entity name already exists in the schema. */
    public final static int ENTITY_EXISTS = 405;
    
    /** A table name already exists in the schema. */
    public final static int TABLE_EXISTS = 406;
    
    /** A property already exists in the entity. */
    public final static int PROPERTY_EXISTS = 407;
    
    /** A column already exists in the table in the schema. */
    public final static int COLUMN_EXISTS = 408;
    
    /** The primary key has already been specified for the entity. */
    public final static int PRIMARY_KEY_EXISTS = 409;
    
    /** The entity cannot be found in the schema. */
    public final static int ENTITY_MISSING = 410;

    /** The table cannot be found in the schema. */
    public final static int TABLE_MISSING = 411;
    
    /** Unable to determine an SQL type for a Java class. */
    public final static int UNMAPPABLE_TYPE = 412;
    
    /** The entity cannot be found in the addendum. */
    public final static int ADDENDUM_ENTITY_MISSING = 413;

    /** The table cannot be found in the addendum. */
    public final static int ADDENDUM_TABLE_MISSING = 414;
    
    /** Unable to create the table for an entity due to an SQL exception. */
    public final static int CANNOT_CREATE_TABLE = 501;
    
    /** Unable to execute arbitrary SQL statements. */
    public final static int CANNOT_EXECUTE_SQL = 502;
    
    /** Unable to alter a column. */
    public final static int CANNOT_ALTER_COLUMN = 503;

    /**
     * Create a Sheaf exception with the given error code.
     * 
     * @param code
     *            The error code.
     */
    public AddendumException(int code, Object...arguments) {
        super(BUNDLES, code, null, arguments);
    }

    /**
     * Wrap the given cause exception in an addendum exception with the given
     * error code.
     * 
     * @param code
     *            The error code.
     * @param cause
     *            The cause exception.
     */
    public AddendumException(int code, Throwable cause, Object... arguments) {
        super(BUNDLES, code, cause, arguments);
    }
}
/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */