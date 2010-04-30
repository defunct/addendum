package com.goodworkalan.addendum.jpa;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DefinitionGenerator {
    private static void print() { 
        System.out.println("");
    }

    private static void print(int depth, String line) { 
        for (int i = 0; i < (depth * 4); i++) {
            System.out.print(' ');
        }
        System.out.println(line);
    }

    public static void generate(String className) throws IOException, ParserConfigurationException, SAXException, DOMException, ClassNotFoundException {
        print(0, "package " + className.substring(0, className.lastIndexOf('.')) + ";");
        print();
        print(0, "import com.goodworkalan.addendum.Addendum;");
        print(0, "import com.goodworkalan.addendum.Definition;");
        print();
        print(0, "public class " + className.substring(className.lastIndexOf('.') + 1));
        print(0, "implements Definition {");
        print(1, "public void define(Addendum addendum) {");
        print(2, "addendum");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        ClassLoader classLoader =  DefinitionGenerator.class.getClassLoader();
        Enumeration<URL> configs = classLoader.getResources("META-INF/persistence.xml");
        while (configs.hasMoreElements()) {
            URL config = configs.nextElement();
            Document document = db.parse(config.openStream());
            NodeList classNames = document.getElementsByTagNameNS("*", "class");
            for (int i = 0; i < classNames.getLength(); i++) {
                System.out.println(classNames.item(i).getNodeName());
                Class<?> type = classLoader.loadClass(classNames.item(i).getTextContent().trim());
                print(EntityInfo.getInstance(type));
            }
        }
        print(1, "public void define(Addendum addendum) {");
    }
    
    private static void print(PropertyInfo property) {
        print(4, ".column(\"" + property.getName() + "\", " + property.getType().getCanonicalName() + ".class)");
        if (property.getGenerationType() != null) {
            print(5, ".generator(GenerationType." + property.getGenerationType().toString() + ")");
        }
        if (!property.isNullable()) {
            print(5, ".notNull()");
        }
        print(5, ".end()");
    }

    private static void print(EntityInfo entity) {
        print(3, ".entity(\"" + entity.getName() + "\")");
        if (!entity.getName().equals(entity.getTableName())) {
            print(4, ".name(\"" + entity.getTableName() + "\")");
        }
        List<PropertyInfo> properties = new ArrayList<PropertyInfo>(entity.getProperties().values());
        for (int i = 0; i < properties.size(); i++) {
            print(properties.get(i));
        }
        print(4, ".end()");
    }
}
