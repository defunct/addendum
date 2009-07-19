package com.goodworkalan.addendum;

/**
 * An enumeration indicating the state of an addendum. Used to assert that the
 * different statement types are called in a particular order.
 * 
 * @author Alan Gutierrez
 */
enum AddendumState
{
    /** No methods have been called on the addendum. */
    WAITING,
    /** A create table element has been created. */
    CREATING,
    /** An alter table element has been created. */
    ALTERING,
    /** An assert table element has been created. */
    ASSERTING,
    /** An insert element has been created. */
    POPULATING,
    /** The addendum has been comitted. */
    COMMITTED
}
