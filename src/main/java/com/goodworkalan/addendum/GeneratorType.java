package com.goodworkalan.addendum;

/**
 * An enumeration to indicate the type of unique id generator for a key column.
 * 
 * @author Alan Gutierrez
 */
public enum GeneratorType
{
    /** No unique id generation for the column. */
    NONE,
    /** The preferred unique id generation strategy for the SQL dialect. */
    PREFERRED,
    /** Auto increment unique id generation. */
    AUTO_INCREMENT,
    /** Sequence unique id generation. */
    SEQUENCE;
}
