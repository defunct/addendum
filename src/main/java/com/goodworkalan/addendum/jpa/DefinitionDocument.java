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

    public boolean needsGeneratorType;
    
    public Map<String, EntityInfo> entities = new TreeMap<String, EntityInfo>();

    private void println() { 
        System.out.println("");
    }

    private void print(int depth, String line) { 
        for (int i = 0; i < (depth * 4); i++) {
            System.out.print(' ');
        }
        System.out.println(line);
    }

    public void print() {
        print(0, "package " + className.substring(0, className.lastIndexOf('.')) + ";");
        println();
        print(0, "import com.goodworkalan.addendum.Addendum;");
        print(0, "import com.goodworkalan.addendum.Definition;");
        if (needsGeneratorType) {
            print(0, "import com.goodworkalan.addendum.GeneratorType;");
        }
        println();
        print(0, "public class " + className.substring(className.lastIndexOf('.') + 1));
        print(0, "implements Definition {");
        print(1, "public void define(Addendum addendum) {");
        print(2, "addendum");
        List<EntityInfo> e = new ArrayList<EntityInfo>(entities.values());
        for (int i = 0, stop = e.size() - 1; i < stop; i++) {
            print(e.get(i), "");
        }
        if (!e.isEmpty()) {
            print(e.get(e.size() - 1), ";");
        }
        print(1, "}");
        print(0, "}");
    }
    
    private void print(PropertyInfo property) {
        if (property.getName().equals(property.getColumnName())) {
            print(4, ".add(\"" + property.getName() + "\", " + property.getType().getCanonicalName() + ".class)");
        } else {
            print(4, ".add(\"" + property.getName() + "\", " + property.getColumnName() + "\", " + property.getType().getCanonicalName() + ".class)");
        }
        if (property.getGenerationType() != null) {
            print(5, ".generator(GenerationType." + property.getGenerationType().toString() + ")");
        }
        if (property.getLength() != null) {
            print(5, ".length(" + property.getLength() + ")");
        }
        if (!property.isNullable()) {
            print(5, ".notNull()");
        }
        print(5, ".end()");
    }

    private void print(EntityInfo entity, String terminate) {
        if (!entity.getName().equals(entity.getTableName())) {
            print(3, ".define(\"" + entity.getName() + "\", \"" + entity.getTableName()  + "\")");
        } else {
            print(3, ".define(\"" + entity.getName() + "\")");
        }
        for (PropertyInfo property : entity.getProperties().values()) {
            print(property);
        }
        print(4, ".end()" + terminate);
    }
}