package com.goodworkalan.addendum.jdbc;

import java.util.ArrayList;
import java.util.List;

public class Table {
    public String name;
    
    public List<Column> columns = new ArrayList<Column>();
    
    public List<String> primaryKey = new ArrayList<String>();
}
