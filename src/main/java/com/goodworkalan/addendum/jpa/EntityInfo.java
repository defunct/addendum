package com.goodworkalan.addendum.jpa;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Table;

public class EntityInfo
{
    private final String name;
    
    private final String tableName;
    
    private final Class<?> entityClass;
    
    private final Map<String, PropertyInfo> properties;
    
    public EntityInfo(String name, String tableName, Class<?> entityClass, Map<String, PropertyInfo> properties)
    {
        this.name = name;
        this.tableName = tableName;
        this.entityClass = entityClass;
        this.properties = Collections.unmodifiableMap(properties);
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public Map<String, PropertyInfo> getProperties()
    {
        return properties;
    }
    
    private static void addFields(Class<?> entityClass, Map<String, Field> fields)
    {
        if (!entityClass.equals(Object.class))
        {
            for (Field field : entityClass.getFields())
            {
                fields.put(field.getName(), field);
            }
            addFields(entityClass.getSuperclass(), fields);
        }
    }
    
    public static EntityInfo getInstance(Class<?> entityClass)
    {
        BeanInfo beanInfo;
        try
        {
            beanInfo = Introspector.getBeanInfo(entityClass, Object.class);
        }
        catch (IntrospectionException e)
        {
            throw new RuntimeException(e);
        }
        Map<String, PropertyInfo> properties = new HashMap<String, PropertyInfo>();
        Map<String, PropertyDescriptor> descriptors = new HashMap<String, PropertyDescriptor>();
        for (PropertyDescriptor desc : beanInfo.getPropertyDescriptors())
        {
            descriptors.put(desc.getName(), desc);
        }
        Map<String, Field> fields = new HashMap<String, Field>();
        addFields(entityClass, fields);
        for (String name : fields.keySet())
        {
            for (PropertyInfo propertyInfo : PropertyInfo.getInstances(name, fields.get(name), descriptors.get(name)))
            {
                if (propertyInfo != null)
                {
                    properties.put(name, propertyInfo);
                }
            }
        }
        for (String name : descriptors.keySet())
        {
            for (PropertyInfo propertyInfo : PropertyInfo.getInstances(name, fields.get(name), descriptors.get(name)))
            {
                if (propertyInfo != null)
                {
                    properties.put(name, propertyInfo);
                }
            }
        }
        String name = entityClass.getName();
        String tableName = name;
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null)
        {
            tableName = table.name();
        }
        return new EntityInfo(name, tableName, entityClass, properties);
    }
    
    public Class<?> getEntityClass()
    {
        return entityClass;
    }
}
