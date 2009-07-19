package com.goodworkalan.addendum;

/**
 * An enumeration to indicate the type of unique id generator for a key column.
 * 
 * @author Alan Gutierrez
 * 
 * FIXME Nix this. USE JTA? No, make just like JTA.
 */
public enum GeneratorType
{
    /** No generator. */
    NONE,
    /** The preferred unique id generation strategy for the SQL dialect. */
    PREFERRED,
    /** Auto increment unique id generation. */
    AUTO_INCREMENT,
    /** Sequence unique id generation. */
    SEQUENCE;
}
