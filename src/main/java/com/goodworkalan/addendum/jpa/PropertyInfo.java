package com.goodworkalan.addendum.jpa;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * The database column information for a property in a JPA entity. 
 * 
 * @author Alan Gutierrez
 */
class PropertyInfo
{
    /** The property name. */
    private final String name;
    
    /** The property column name. */
    private final String columnName;
    
    /** The property Java type. */
    private final Class<?> type;
    
    /** If true, the property is an identifier. */
    private final boolean id;

    /** The property length. */
    private final int length;
    
    /** The property precision. */
    private final int precision;
    
    /** The property scale. */
    private final int scale;
    
    /** If true, the property is nullable. */
    private final boolean nullable;
    
    /** The property generation type. */
    private final GenerationType generationType;

    /**
     * Create a property information.
     * 
     * @param name
     *            The property name.
     * @param columnName
     *            The property column name.
     * @param type
     *            The property Java type.
     * @param id
     *            If true, the property is an identifier.
     * @param length
     *            The property length.
     * @param precision
     *            The property precision.
     * @param scale
     *            The property scale.
     * @param nullable
     *            If true, the property is nullable.
     * @param generationType
     *            The property generation type.
     */
    public PropertyInfo(String name, String columnName, Class<?> type, boolean id, int length, int precision, int scale, boolean nullable, GenerationType generationType)
    {
        this.name = name;
        this.columnName = columnName;
        this.type = type;
        this.id = id;
        this.length = length;
        this.precision = precision;
        this.scale = scale;
        this.nullable = nullable;
        this.generationType = generationType;
    }

    /**
     * Get the property name.
     * 
     * @return The property name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the column name.
     * 
     * @return The column name.
     */
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * Get the Java type.
     * 
     * @return The Java type.
     */
    public Class<?> getType()
    {
        return type;
    }

    /**
     * If true, the column is an identifier.
     * 
     * @return True if the column is an identifier.
     */
    public boolean isId()
    {
        return id;
    }

    /**
     * If true, the column is nullable.
     * 
     * @return True if the column is nullable.
     */
    public boolean isNullable()
    {
        return nullable;
    }

    /**
     * Get the column length.
     * 
     * @return The column length.
     */
    public int getLength()
    {
        return length;
    }
    
    /**
     * Get the column precision.
     * 
     * @return The column precision.
     */
    public int getPrecision()
    {
        return precision;
    }

    /**
     * Get the column scale.
     * 
     * @return The column scale.
     */
    public int getScale()
    {
        return scale;
    }

    /**
     * Get the column generation type.
     * 
     * @return The column generation type.
     */
    public GenerationType getGenerationType()
    {
        return generationType;
    }

    /**
     * Return an array of one or two property information instances, two being
     * created when the types for the field and the property descriptor are not
     * equal.
     * 
     * @param name
     *            The property name.
     * @param field
     *            The field or null if there is no field with the property name.
     * @param desc
     *            The property descriptor or null if there is no property
     *            descriptor with the property name.
     * @return An array of one or two property information instances.
     */
    static PropertyInfo[] getInstances(String name, Field field, PropertyDescriptor desc)
    {
        if (field == null || desc == null || desc.getPropertyType().equals(field.getType()))
        {
            return new PropertyInfo[] { introspect(name, field, desc) };
        }
        return new PropertyInfo[] { introspect(name, field, null), introspect(name, null, desc) };
    }

    /**
     * Get the type of the id for a given entity class. Used to determine the
     * types in many-to-one and one-to-one properties.
     * 
     * @param entityClass
     *            The entity class.
     * @return The type of the entity class id, or null if the entity class has
     *         no id.
     */
    private static Class<?> getId(Class<?> entityClass)
    {
        Class<?> current = entityClass;
        while (!current.equals(Object.class))
        {
            for (Field id : entityClass.getFields())
            {
                if (id.getAnnotation(Id.class) != null)
                {
                    return id.getType();
                }
            }
            current = current.getSuperclass();
        }
        BeanInfo beanInfo;
        try
        {
            beanInfo = Introspector.getBeanInfo(entityClass, Object.class);
        }
        catch (IntrospectionException e)
        {
            throw new RuntimeException(e);
        }
        for (PropertyDescriptor desc : beanInfo.getPropertyDescriptors())
        {
            if (desc.getWriteMethod() != null && desc.getWriteMethod().getAnnotation(Id.class) != null)
            {
                return desc.getPropertyType();
            }
            if (desc.getReadMethod() != null && desc.getReadMethod().getAnnotation(Id.class) != null)
            {
                return desc.getPropertyType();
            }
        }
        return null;
    }

    /**
     * Create property information based on the annotations attached to given
     * field and property descriptor.
     * 
     * @param name
     *            The property name.
     * @param field
     *            The field or null if there is no field with the property name.
     * @param desc
     *            The property descriptor or null if there is no property
     *            descriptor with the property name.
     * @return The property information.
     */
    private static PropertyInfo introspect(String name, Field field, PropertyDescriptor desc)
    {
        Class<?> type = null;
        Annotation[][] annotations = new Annotation[3][];
        int index = 0;
        if (field != null)
        {
            type = field.getType();
            annotations[index++] = field.getAnnotations();
        }
        if (desc != null)
        {
            type = desc.getPropertyType();
            if (desc.getReadMethod() != null)
            {
                annotations[index++] = desc.getReadMethod().getAnnotations();
            }
            if (desc.getWriteMethod() != null)
            {
                annotations[index++] = desc.getWriteMethod().getAnnotations();
            }
        }
        String columnName = name;
        int length = 255, precision = 0, scale = 0;
        boolean nullable = true, joinColumnSeen = false, id = false;
        GenerationType generationType = null;
        for (int i = 0; i < annotations.length && annotations[i] != null; i++)
        {
            for (Annotation annotation : annotations[i])
            {
                if (annotation.annotationType().equals(Column.class))
                {
                    Column column = (Column) annotation;
                    if (!column.name().equals(""))
                    {
                        columnName = column.name();
                    }
                    length = column.length();
                    precision = column.precision();
                    scale = column.scale();
                    nullable = column.nullable();
                }
                else if (annotation.annotationType().equals(GeneratedValue.class))
                {
                    GeneratedValue generatedValue = (GeneratedValue) annotation;
                    generationType = generatedValue.strategy();
                }
                else if (annotation.annotationType().equals(Id.class))
                {
                    id = true;
                    nullable = false;
                }
                else if (annotation.annotationType().equals(Transient.class))
                {
                    return null;
                }
                else if (annotation.annotationType().equals(OneToMany.class))
                {
                    return null;
                }
                else if (annotation.annotationType().equals(OneToOne.class))
                {
                    OneToOne oneToOne = (OneToOne) annotation;
                    if (!oneToOne.mappedBy().equals(""))
                    {
                        return null;
                    }
                    if (!joinColumnSeen)
                    {
                        Class<?> referrantType = type;
                        if (oneToOne.targetEntity() != void.class)
                        {
                            referrantType = oneToOne.targetEntity();
                        }
                        columnName = name + "_id";
                        type = getId(referrantType);
                    }
                    if (!oneToOne.optional())
                    {
                        nullable = false;
                    }
                }
                else if (annotation.annotationType().equals(ManyToOne.class))
                {
                    ManyToOne manyToOne = (ManyToOne) annotation;
                    if (!joinColumnSeen)
                    {
                        Class<?> referrantType = type;
                        if (manyToOne.targetEntity() != void.class)
                        {
                            referrantType = manyToOne.targetEntity();
                        }
                        columnName = name + "_id";
                        type = getId(referrantType);
                    }
                    if (!manyToOne.optional())
                    {
                        nullable = false;
                    }
                }
                else if (annotation.annotationType().equals(JoinColumn.class))
                {
                    JoinColumn joinColumn = (JoinColumn) annotation;
                    if (!joinColumn.name().equals(""))
                    {
                        columnName = joinColumn.name();
                    }
                    if (!joinColumn.nullable())
                    {
                        nullable = false;
                    }
                }
            }
        }
        return new PropertyInfo(name, columnName, type, id, length, precision, scale, nullable, generationType);
    }

    /**
     * Return a string representation of this property.
     * 
     * @return The property as a string.
     */
    public String toString() {
        return type.toString() + " " + getName();
    }
}
