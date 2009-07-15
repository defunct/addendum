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

import com.goodworkalan.addendum.FreshColumn;
import com.goodworkalan.addendum.GeneratorType;

public class PropertyInfo
{
    private final String name;
    
    private final String columnName;
    
    private final Class<?> type;
    
    private final boolean id;

    private final int length;
    
    private final int precision;
    
    private final int scale;
    
    private final boolean nullable;
    
    private final GenerationType generationType;
    
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

    static PropertyInfo[] getInstances(String name, Field field, PropertyDescriptor desc)
    {
        if (field == null || desc == null || desc.getPropertyType().equals(field.getType()))
        {
            return new PropertyInfo[] { introspect(name, field, desc) };
        }
        return new PropertyInfo[] { introspect(name, field, null), introspect(name, null, desc) };
    }
    
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
        for (Annotation[] annotated : annotations)
        {
            for (Annotation annotation : annotated)
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
                        String referrantName = referrantType.getName();
                        columnName = referrantName.substring(0, 1).toLowerCase();
                        if (referrantName.length() > 1)
                        {
                            columnName += referrantName.substring(1);
                        }
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
                        String referrantName = referrantType.getName();
                        columnName = referrantName.substring(0, 1).toLowerCase();
                        if (referrantName.length() > 1)
                        {
                            columnName += referrantName.substring(1);
                        }
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
    
    public String getName()
    {
        return name;
    }
    
    public String getColumnName()
    {
        return columnName;
    }
    
    public Class<?> getType()
    {
        return type;
    }
    
    public boolean isId()
    {
        return id;
    }
    
    public boolean isNullable()
    {
        return nullable;
    }
    
    public int getLength()
    {
        return length;
    }
    
    public int getPrecision()
    {
        return precision;
    }
    
    public int getScale()
    {
        return scale;
    }
    
    public GenerationType getGenerationType()
    {
        return generationType;
    }
   
    public void setColumn(FreshColumn<?, ?> column)
    {
        column.length(getLength());
        column.precision(getPrecision());
        column.scale(getScale());
        if (getGenerationType() != null)
        {
            switch (getGenerationType())
            {
            case AUTO:
                column.generator(GeneratorType.PREFERRED);
                break;
            case IDENTITY:
                column.generator(GeneratorType.AUTO_INCREMENT);
                break;
            case SEQUENCE:
                column.generator(GeneratorType.SEQUENCE);
                break;
            }
        }
        if (!isNullable())
        {
            column.isNotNull();
        }
    }
    
    public void setColumn(com.goodworkalan.addendum.DefineColumn<?, ?> column)
    {
        column.length(getLength());
        column.precision(getPrecision());
        column.scale(getScale());
    }
}
