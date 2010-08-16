package com.goodworkalan.addendum.dialect;

import java.util.ArrayList;
import java.util.List;

/**
 * A mock database that records actions taken by the {@link MockDialect}.
 *
 * @author Alan Gutierrez
 */
public class MockDatabase {
    /** The mock database instance. */
    public static MockDatabase INSTANCE = new MockDatabase();
    
    /** The list of addenda applied. */
    public final List<Integer> addenda = new ArrayList<Integer>();

    /** The create table records. */
    public final List<CreateTable> createTables = new ArrayList<CreateTable>();

    /** The add column records. */
    public final List<AddColumn> addColumns = new ArrayList<AddColumn>();
    
    /** The alter column records. */
    public final List<AlterColumn> alterColumns = new ArrayList<AlterColumn>();

    /** Reset the mock database. */
    public static void clear() {
        INSTANCE = new MockDatabase();
    }
}
