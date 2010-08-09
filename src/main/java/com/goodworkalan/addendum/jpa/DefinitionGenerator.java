package com.goodworkalan.addendum.jpa;

import java.net.URL;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Generate an Addendum definition that describes all of the classes in the
 * <code>META-INF/persistehce.xml</code> definition file.
 *
 * @author Alan Gutierrez
 */
public class DefinitionGenerator {
    /**
     * Generate the definition class.
     * 
     * @param className
     *            The definition class name.
     * @throws Exception
     *             For any number of entirely unrecoverable errors.
     */
    public static void generate(String className) throws Exception {
        try {
        DefinitionDocument definitionDocument = new DefinitionDocument();
        definitionDocument.className = className;
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
                EntityInfo entity = EntityInfo.getInstance(type);
                if (entity.getDiscriminator() != null) {
                    definitionDocument.needsTypes = true;
                }
                for (PropertyInfo property : entity.getProperties().values()) {
                    if (property.getGenerationType() != null) {
                        definitionDocument.needsGeneratorType = true;
                    }
                }
                definitionDocument.entities.put(type.getCanonicalName(), entity);
            }
        }
        definitionDocument.print();
        } catch (Exception e) {
            // Everything here is entirely unrecoverable, simply report the error.
            throw new RuntimeException(e);
        }
    }
}
