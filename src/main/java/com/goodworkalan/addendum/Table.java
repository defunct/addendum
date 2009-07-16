package com.goodworkalan.addendum;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Table
{
    private String name;
    
    private Map<String, Column> columns = new LinkedHashMap<String, Column>();
    
    private List<Map<String, Column>> verifications = new ArrayList<Map<String, Column>>();
    
    public Table(String name)
    {
        this.name = name;
    }

    public Map<String, Column> getColumns()
    {
        return columns;
    }
    
    public List<Map<String, Column>> getVerifications()
    {
        return verifications;
    }

    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
}
