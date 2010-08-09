package com.goodworkalan.addendum.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Working document to gather properties prior to printing.
 *
 * @author Alan Gutierrez
 */
class DefinitionDocument {
    /** The class name of the definition to generate. */
    public String className;

    /**
     * The document should generate
     * <code>com.goodworkalan.addendum.GeneratorType</code>.
     */
    public boolean needsGeneratorType;
    
    /** Whether to import <code>java.sql.Types</code>. */
    public boolean needsTypes;
    
    /** The map of entity names to entity information. */
    public Map<String, EntityInfo> entities = new TreeMap<String, EntityInfo>();

    /** Print a blank line to standard out. */
    private void println() { 
        System.out.println("");
    }

    /**
     * Print the line indented to the given depth.
     * 
     * @param depth
     *            The depth of the index.
     * @param line
     *            The line.
     */
    private void print(int depth, String line) { 
        for (int i = 0; i < (depth * 4); i++) {
            System.out.print(' ');
        }
        System.out.println(line);
    }

    /** Print the definition class file. */
    public void print() {
        print(0, "package " + className.substring(0, className.lastIndexOf('.')) + ";");
        println();
        print(0, "import com.goodworkalan.addendum.Addendum;");
        print(0, "import com.goodworkalan.addendum.Definition;");
        if (needsGeneratorType) {
            print(0, "import com.goodworkalan.addendum.GeneratorType;");
        }
        if (needsTypes) {
            print(0, "import java.sql.Types;");
        }
        println();
        print(0, "public class " + className.substring(className.lastIndexOf('.') + 1));
        print(0, "implements Definition {");
        print(1, "public void define(Addendum addendum) {");
        if (!entities.isEmpty()) {
            print(2, "addendum");
            List<EntityInfo> e = new ArrayList<EntityInfo>(entities.values());
            for (int i = 0, stop = e.size() - 1; i < stop; i++) {
                print(e.get(i), "");
            }
            print(e.get(e.size() - 1), ";");
        }
        print(1, "}");
        print(0, "}");
    }
    
    /**
     * Print the property information.
     * 
     * @param property
     *            The property information.
     */
    private void print(PropertyInfo property) {
        if (property.getName().equals(property.getColumnName())) {
            print(4, ".add(\"" + property.getName() + "\", " + property.getType().getCanonicalName() + ".class)");
        } else {
            print(4, ".add(\"" + property.getName() + "\", \"" + property.getColumnName() + "\", " + property.getType().getCanonicalName() + ".class)");
        }
        if (property.getGenerationType() != null) {
            print(5, ".generator(GeneratorType." + property.getGenerationType().toString() + ")");
        }
        if (property.getLength() != null) {
            print(5, ".length(" + property.getLength() + ")");
        }
        if (property.getPrecision() != null) {
            print(5, ".precision(" + property.getPrecision() + ")");
        }
        if (property.getScale() != null) {
            print(5, ".scale(" + property.getScale() + ")");
        }
        if (!property.isNullable()) {
            print(5, ".notNull()");
        }
        print(5, ".end()");
    }

    /**
     * Print the entity definition.
     * 
     * @param entity
     *            The entity information.
     * @param terminate
     *            The string to print after the entity definition, possibly the
     *            empty string, possibly a semi-colon.
     */
    private void print(EntityInfo entity, String terminate) {
        if (!entity.getName().equals(entity.getTableName())) {
            print(3, ".define(\"" + entity.getName() + "\", \"" + entity.getTableName()  + "\")");
        } else {
            print(3, ".define(\"" + entity.getName() + "\")");
        }
        if (entity.getDiscriminator() != null) {
            switch (entity.getDiscriminator().discriminatorType()) {
            case STRING:
                print(4, ".add(\"" + entity.getDiscriminator().name() + "\", Types.VARCHAR)");
                print(5, ".length(" + entity.getDiscriminator().length() + ")");
                print(5, ".end");
                break;
            case CHAR:
                print(4, ".add(\"" + entity.getDiscriminator().name() + "\", Types.CHAR)");
                print(5, ".length(" + entity.getDiscriminator().length() + ")");
                print(5, ".end");
                break;
            default: // INTEGER
                print(4, ".add(\"" + entity.getDiscriminator().name() + "\", Types.INTEGER)");
                print(5, ".end()");
                break;
            }
            
        }
        for (PropertyInfo property : entity.getProperties().values()) {
            print(property);
        }
        List<String> primaryKey = new ArrayList<String>();
        for (PropertyInfo property : entity.getProperties().values()) {
            if (property.isId()) {
                primaryKey.add(property.getName());
            }
        }
        if (!primaryKey.isEmpty()) {
            StringBuilder pk = new StringBuilder();
            String separator = "";
            for (String name : primaryKey) {
                pk.append(separator);
                pk.append("\"" + name + "\"");
                separator = ", ";
            }
            print(4, ".primaryKey(" + pk + ")");
        }
        print(4, ".end()" + terminate);
    }
}
