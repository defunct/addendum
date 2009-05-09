package com.goodworkalan.addendum;

/**
 * An enumeration to indicate the SQL type of a column for the domain-specific
 * language used by {@link DatabaseAddendum} to define database update actions.
 * 
 * @author Alan Gutierrez
 * 
 */
public enum ColumnType
{
    TEXT, VARCHAR, NUMBER, INTEGER, LONG, DATE
}
