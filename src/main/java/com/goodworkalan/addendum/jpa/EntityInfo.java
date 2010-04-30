package com.goodworkalan.addendum.jpa;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Reflects upon a Java class to determine the entity information as defined by
 * JPA annotations.
 * 
 * @author Alan Gutierrez
 */
class EntityInfo
{
    /** The entity name. */
    private final String name;
    
    /** The table name mapped to the entity. */
    private final String tableName;
    
    /** The discriminator column for the discriminator inheritance strategy. */
    private final DiscriminatorColumn discriminator;
    
    /** The entity class. */
    private final Class<?> entityClass;
    
    /** The entity properties. */
    private final Map<String, PropertyInfo> properties;

    /**
     * Create a new entity information with the given entity name, table name,
     * entity class and properties.
     * 
     * @param name
     *            The entity name.
     * @param tableName
     *            The table name mapped to the entity.
     * @param entityClass
     *            The entity class.
     * @param discriminator
     *            The discriminator column.
     * @param properties
     *            The entity properties.
     */
    public EntityInfo(String name, String tableName, Class<?> entityClass, DiscriminatorColumn discriminator, Map<String, PropertyInfo> properties) {
        this.name = name;
        this.tableName = tableName;
        this.entityClass = entityClass;
        this.discriminator = discriminator;
        this.properties = Collections.unmodifiableMap(properties);
    }

    /**
     * Get the entity class.
     * 
     * @return The entity class.
     */
    public Class<?> getEntityClass()
    {
        return entityClass;
    }

    /**
     * Get the entity name.
     * 
     * @return The entity name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the table name mapped to the entity.
     * 
     * @return The table name mapped to the entity.
     */
    public String getTableName()
    {
        return tableName;
    }

    /**
     * Get the discriminator column for the discriminator inheritance strategy.
     * 
     * @return The discriminator column.
     */
    public DiscriminatorColumn getDiscriminator() {
        return discriminator;
    }

    /**
     * Get an unmodifiable map of the entity properties.
     * 
     * @return An unmodifiable map of the entity properties.
     */
    public Map<String, PropertyInfo> getProperties()
    {
        return properties;
    }

    /**
     * A add the fields in the given entity class to the given map of fields,
     * then call the this method on the super class. If the entity class is the
     * <code>Object</code> class no reflection is performed and recursion
     * terminates.
     * 
     * @param entityClass
     *            The entity class.
     * @param fields
     *            A map of fields indexed by field name.
     */
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

    /**
     * Find the discriminator column annotation for the given entity class, if
     * any.
     * 
     * @param entityClass
     *            The entity class.
     * @return The discriminator column annotation or null if none exists.
     */
    private static DiscriminatorColumn findDiscriminatorColumn(Class<?> entityClass) {
        if (entityClass.getAnnotation(Entity.class) != null) {
            DiscriminatorColumn discriminatorColumn = entityClass.getAnnotation(DiscriminatorColumn.class);
            if (discriminatorColumn == null)
            {
                return findDiscriminatorColumn(entityClass.getSuperclass());
            }
            return discriminatorColumn;
        }
        return null;
    }

    /**
     * Create an instance of entity information for the given entity class.
     * 
     * @param entityClass
     *            The entity class.
     * @return The entity information for the entity class.
     */
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
        Map<String, PropertyInfo> properties = new LinkedHashMap<String, PropertyInfo>();
        Map<String, PropertyDescriptor> descriptors = new LinkedHashMap<String, PropertyDescriptor>();
        for (PropertyDescriptor desc : beanInfo.getPropertyDescriptors())
        {
            descriptors.put(desc.getName(), desc);
        }
        Map<String, Field> fields = new LinkedHashMap<String, Field>();
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
        DiscriminatorColumn discriminatorColumn = findDiscriminatorColumn(entityClass);
        String name = entityClass.getName();
        name = name.substring(name.lastIndexOf('.') + 1);
        String tableName = name;
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null)
        {
            tableName = table.name();
        }
        return new EntityInfo(name, tableName, entityClass, discriminatorColumn, properties);
    }
}
