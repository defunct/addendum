package com.goodworkalan.addendum.jdbc;

import java.sql.Types;
import java.util.List;

/**
 * A structure to collect the details of the <code>Definition</code> source
 * file that will be generated.
 *
 * @author Alan Gutierrez
 */
public class DefinitionDocument {
    /** The Java source code class name. */
    public String className;

    /** Whether to import <code>com.goodworkalan.addendum.GeneratorType</code>. */
    public boolean needsGeneratorType;
    
    /** The tables. */
    private final List<Table> tables;
    
    /** Print a blank line. */
    private void println() { 
        System.out.println("");
    }

    /**
     * Print the given line with the given indent. An indent is four spaces.
     * 
     * @param depth
     *            The indent depth.
     * @param line
     *            The line.
     */
    private void print(int depth, String line) { 
        for (int i = 0; i < (depth * 4); i++) {
            System.out.print(' ');
        }
        System.out.println(line);
    }

    /**
     * Create a definition document that will generate a Java source file
     * containing a class with the given class name. The tables are populated by
     * the caller before calling the generate method.
     * 
     * @param className
     *            The class name.
     * @param tables
     *            The tables.
     */
    public DefinitionDocument(String className, List<Table> tables) {
        this.className = className;
        this.tables = tables;
    }
    
    /**
     * Print the document to standard out.
     */
    public void print() {
        print(0, "package " + className.substring(0, className.lastIndexOf('.')) + ";");
        println();
        print(0, "import com.goodworkalan.addendum.Addendum;");
        print(0, "import com.goodworkalan.addendum.Definition;");
        if (needsGeneratorType) {
            print(0, "import com.goodworkalan.addendum.GeneratorType;");
        }
        print(0, "import java.sql.Types;");
        println();
        print(0, "public class " + className.substring(className.lastIndexOf('.') + 1));
        print(0, "implements Definition {");
        print(1, "public void define(Addendum addendum) {");
        if (!tables.isEmpty()) {
            print(2, "addendum");
            for (int i = 0, stop = tables.size() - 1; i < stop; i++) {
                print(tables.get(i), "");
            }
            print(tables.get(tables.size() - 1), ";");
        }
        print(1, "}");
        print(0, "}");
    }

    /**
     * Convert the given <code>java.sql.Types</code> code into its source code
     * representation.
     * 
     * @param type
     *            The type.
     * @return The type as it appears in source code.
     */
    private String getColumnType(int type) {
        switch (type) {
        case Types.BIGINT:
            return "Types.BIGINT";
        case Types.INTEGER:
            return "Types.INTEGER";
        case Types.REAL:
            return "Types.REAL";
        case Types.VARCHAR:
            return "Types.VARCHAR";
        case Types.TIMESTAMP:
            return "Types.TIMESTAMP";
        }
        throw new IllegalArgumentException();
    }

    /**
     * Print the column.
     * 
     * @param column
     *            The column.
     */
    private void print(Column column) {
        print(4, ".add(\"" + column.name + "\", \"" + column.name + "\", " + getColumnType(column.type) + ")");
        if (column.autoIncrement) {
            print(5, ".generator(GeneratorType.IDENTITY)");
        }
        switch (column.type){ 
        case Types.VARCHAR:
            if (column.size != 255) {
                print(5, ".length(" + column.size + ")");
            }
            break;
        case Types.DECIMAL:
            print(5, ".precision(" + column.size + ")");
            print(5, ".scale(" + column.scale + ")");
            break;
        }
//        if (property.getPrecision() != null) {
//            print(5, ".precision(" + property.getPrecision() + ")");
//        }
//        if (property.getScale() != null) {
//            print(5, ".scale(" + property.getScale() + ")");
//        }
        if (!column.nullable) {
            print(5, ".notNull()");
        }
        print(5, ".end()");
 
    }

    /**
     * Print the table followed by the given terminator string.
     * 
     * @param table
     *            The table.
     * @param termiante
     *            the end string.
     */
    private void print(Table table, String terminate) {
        print(3, ".define(\"" + table.name + "\")");
        for (Column column : table.columns) {
            print(column);
        }
        if (!table.primaryKey.isEmpty()) {
            StringBuilder pk = new StringBuilder();
            String separator = "";
            for (String name : table.primaryKey) {
                pk.append(separator);
                pk.append("\"" + name + "\"");
                separator = ", ";
            }
            print(4, ".primaryKey(" + pk + ")");
        }
        print(4, ".end()" + terminate);
    }

}
